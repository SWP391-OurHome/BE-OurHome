package com.javaweb.controller;

import com.javaweb.model.PaymentDTO;
import com.javaweb.repository.impl.UserRepositoryImpl;
import com.javaweb.repository.impl.RoleRepositoryImpl;
import com.javaweb.repository.impl.PaymentRepositoryImpl;
import com.javaweb.repository.impl.MembersRepositoryImpl;
import com.javaweb.service.MembersService;
import com.javaweb.service.MembershipService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.transaction.annotation.Transactional;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.PaymentData;
import vn.payos.type.PaymentData.PaymentDataBuilder;
import vn.payos.util.SignatureUtils;
import com.javaweb.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"}, allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private RoleRepositoryImpl roleRepository;

    @Autowired
    private PaymentRepositoryImpl paymentRepository;

    @Autowired
    private MembersRepositoryImpl packageMemberRepository;

    @Autowired
    private MembersService membersService;

    private PayOS payOS;
    private static final ConcurrentHashMap<String, Boolean> processingOrders = new ConcurrentHashMap<>();
    private static final ReentrantLock lock = new ReentrantLock();

    @PostConstruct
    public void init() {
        payOS = new PayOS(
                "fbb5a77e-6d42-44bd-a44f-6324159c1ebf",
                "92507e3a-0030-490d-9719-75212a09d636",
                "dee9853b0cb73699eed0f91d5dfd7ea4c98e55152e420b5fecb12ce115a40b06"
        );
    }

    @PostMapping("/initiate")
    public ResponseEntity<Map<String, String>> initiatePayment(@RequestBody Map<String, Object> payload, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession(true);
        System.out.println("Received payload from FE: " + payload); // Log toàn bộ payload nhận được
        try {
            String orderCodeStr = (String) payload.get("orderCode");
            Long orderCode = Long.parseLong(orderCodeStr);
            Number amountNum = (Number) payload.get("amount");
            int amount = amountNum.intValue();
            String description = (String) payload.get("description");
            String email = (String) payload.get("email");
            Integer membershipId = Integer.parseInt((String) payload.get("membershipId"));

            System.out.println("DEBUG: Initiating payment with email: " + email + ", orderCode: " + orderCodeStr + ", membershipId: " + membershipId);

            if (description != null && description.length() > 24) {
                description = description.substring(0, 24);
            }

            String ngrokUrl = "https://f585-2402-800-6205-71c6-6427-79af-8047-169b.ngrok-free.app";

            if (!lock.tryLock()) {
                System.out.println("OrderCode " + orderCodeStr + " is locked, continuing anyway");
            }

            try {
                if (processingOrders.putIfAbsent(orderCodeStr, true) != null) {
                    System.out.println("OrderCode " + orderCodeStr + " is already being processed, continuing anyway");
                }

                if (!paymentService.isValidMembershipId(membershipId)) {
                    throw new IllegalArgumentException("Invalid membershipId: " + membershipId);
                }

                Integer userId = paymentService.getUserIdByEmail(email);
                if (userId == null) {
                    throw new IllegalArgumentException("User not found for email: " + email);
                }

                boolean isNewPayment = paymentService.checkAndCreatePendingPayment(orderCodeStr, userId, (double) amount, description, membershipId);
                if (!isNewPayment) {
                    System.out.println("OrderCode " + orderCodeStr + " already exists, reusing existing payment");
                } else {
                    System.out.println("New payment created for orderCode: " + orderCodeStr + ", role updated if applicable");
                    // Gọi createMembershipAfterPayment sau khi checkAndCreatePendingPayment thành công
                    membersService.createMembershipAfterPayment(userId, membershipId);
                }

                PaymentDataBuilder paymentDataBuilder = PaymentData.builder()
                        .orderCode(orderCode)
                        .amount(amount)
                        .description(description != null ? description : "Membership Payment")
                        .cancelUrl(ngrokUrl + "/api/payment/cancel")
                        .returnUrl(ngrokUrl + "/api/payment/callback");

                PaymentData paymentData = paymentDataBuilder.build();

                String signature = SignatureUtils.createSignatureOfPaymentRequest(paymentData, "dee9853b0cb73699eed0f91d5dfd7ea4c98e55152e420b5fecb12ce115a40b06");
                paymentData.setSignature(signature);

                session.setAttribute("lastOrderCode", orderCodeStr);
                session.setAttribute("lastUserId", userId);
                System.out.println("DEBUG: Saved lastOrderCode to session: " + orderCodeStr + ", lastUserId: " + userId);

                CheckoutResponseData response = payOS.createPaymentLink(paymentData);
                Map<String, String> result = new HashMap<>();
                String redirectUrl = response.getCheckoutUrl() + "?orderCode=" + orderCodeStr + "&email=" + URLEncoder.encode(email, "UTF-8") +
                        "&amount=" + amount + "&membershipId=" + membershipId;
                result.put("paymentUrl", redirectUrl);
                return ResponseEntity.ok(result);
            } finally {
                processingOrders.remove(orderCodeStr);
                if (lock.isLocked()) lock.unlock();
            }
        } catch (Exception e) {
            System.out.println("Error in initiatePayment: " + e.getMessage());
            Map<String, String> errorResult = new HashMap<>();
            errorResult.put("error", "Payment initiation failed due to: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResult);
        }
    }

    @GetMapping("/redirect-to-payos")
    public void redirectToPayos(@RequestParam String url, HttpServletResponse response) throws IOException {
        response.setHeader("ngrok-skip-browser-warning", "true");
        response.sendRedirect(url);
    }

    @Transactional
    @RequestMapping(value = "/callback", method = {RequestMethod.GET, RequestMethod.POST})
    public void handleCallback(@RequestBody(required = false) Map<String, Object> body,
                               @RequestParam Map<String, String> allParams,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        System.out.println("Callback received - Params: " + allParams + ", Body: " + body);
        String orderCode = allParams.get("orderCode");
        String email = allParams.get("email");
        String membershipIdStr = allParams.get("membershipId");
        String status = allParams.get("status");

        if (body != null && body.get("orderCode") != null) {
            orderCode = String.valueOf(body.get("orderCode"));
        }
        if (body != null && body.get("status") != null) {
            status = String.valueOf(body.get("status"));
        }

        Integer membershipId = membershipIdStr != null ? Integer.parseInt(membershipIdStr) : null;

        if (orderCode != null && status != null) {
            System.out.println("DEBUG: Processing orderCode: " + orderCode + ", status: " + status + ", membershipId: " + membershipId + ", email: " + email);
            String normalizedStatus = "SUCCESS".equalsIgnoreCase(status) || "PAID".equalsIgnoreCase(status) ? "SUCCESS" : status.toUpperCase();
            System.out.println("DEBUG: Normalized status to: " + normalizedStatus);

            paymentService.updatePaymentStatus(orderCode, normalizedStatus);

            Integer userId = null;
            if (email != null) {
                userId = paymentService.getUserIdByEmail(email);
            }
            if (userId == null && session != null) {
                userId = (Integer) session.getAttribute("lastUserId");
                System.out.println("DEBUG: Email is null, fetched userId from session: " + userId);
                if (userId == null) {
                    String sessionOrderCode = (String) session.getAttribute("lastOrderCode");
                    if (sessionOrderCode != null) {
                        userId = paymentService.getUserIdByTransactionCode(sessionOrderCode);
                        System.out.println("DEBUG: Fetched userId from payment: " + userId);
                    }
                }
            }

            if (userId != null) {
                System.out.println("[CALLBACK] Processed callback for userId: " + userId + ", orderCode: " + orderCode + ", status: " + normalizedStatus);
            } else {
                System.out.println("DEBUG: User not found. orderCode=" + orderCode + ", status=" + normalizedStatus);
            }
        } else {
            System.out.println("DEBUG: Missing orderCode or status in callback");
        }

        String redirectUrl = "http://localhost:3000/membership";
        if (orderCode != null && ("SUCCESS".equalsIgnoreCase(status) || "PAID".equalsIgnoreCase(status))) {
            redirectUrl = "http://localhost:3000/membership?roleId=3";
        } else if ("CANCELLED".equalsIgnoreCase(status)) {
            redirectUrl = "http://localhost:3000/membership";
        }
        System.out.println("DEBUG: Redirecting to: " + redirectUrl);
        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/cancel")
    public void cancelPayment(@RequestParam Map<String, String> allParams, HttpServletResponse response) throws IOException {
        System.out.println("Cancel received with params: " + allParams);
        String orderCode = allParams.get("orderCode");
        try {
            if (orderCode != null) {
                paymentService.cancelPayment(orderCode);
                System.out.println("Updated payment status to CANCELLED for transactionCode: " + orderCode);
            } else {
                System.out.println("No orderCode found in cancel request");
                throw new IllegalStateException("No transactionCode available to cancel payment");
            }
            response.sendRedirect("http://localhost:3000/membership");
        } catch (Exception e) {
            System.out.println("Error in cancelPayment: " + e.getMessage() + ", stack trace: " + e.getStackTrace());
            response.sendRedirect("http://localhost:3000/membership");
        }
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<PaymentDTO>> getTransactionHistory(
            @PathVariable Integer userId,
            @RequestParam(required = false) String transactionCode,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String paymentDate) {
        List<PaymentDTO> transactions = paymentService.getTransactionHistory(userId, transactionCode, status, paymentDate);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/history/all")
    public ResponseEntity<List<PaymentDTO>> getAllTransactionHistory(
            @RequestParam(required = false) String transactionCode,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String paymentDate) {
        List<PaymentDTO> transactions = paymentService.getAllTransactionHistory(transactionCode, status, paymentDate);
        return ResponseEntity.ok(transactions);
    }
}