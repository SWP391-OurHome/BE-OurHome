package com.javaweb.controller;

import com.javaweb.model.PaymentDTO;
import com.javaweb.repository.entity.*;
import com.javaweb.repository.impl.UserRepositoryImpl;
import com.javaweb.repository.impl.RoleRepositoryImpl;
import com.javaweb.repository.impl.PaymentRepositoryImpl;
import com.javaweb.repository.impl.PackageMemberRepositoryImpl;
import jakarta.annotation.PostConstruct;
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
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private PackageMemberRepositoryImpl packageMemberRepository;

    private PayOS payOS;

    @PostConstruct
    public void init() {
        payOS = new PayOS(
                "fbb5a77e-6d42-44bd-a44f-6324159c1ebf",
                "92507e3a-0030-490d-9719-75212a09d636",
                "dee9853b0cb73699eed0f91d5dfd7ea4c98e55152e420b5fecb12ce115a40b06"
        );
    }

    @PostMapping("/initiate")
    public ResponseEntity<Map<String, String>> initiatePayment(@RequestBody Map<String, Object> payload) {
        try {
            String orderCodeStr = (String) payload.get("orderCode");
            Long orderCode = Long.parseLong(orderCodeStr);
            Number amountNum = (Number) payload.get("amount");
            int amount = amountNum.intValue();
            String description = (String) payload.get("description");
            String name = (String) payload.get("name");
            String email = (String) payload.get("email");
            String membershipType = (String) payload.get("membershipType");

            if (description != null && description.length() > 24) {
                return ResponseEntity.badRequest().body(Map.of("error", "Description must be 24 characters or less"));
            }

            String ngrokUrl = " https://57d1-118-69-34-209.ngrok-free.app";

            PaymentDataBuilder paymentDataBuilder = PaymentData.builder()
                    .orderCode(orderCode)
                    .amount(amount * 100)
                    .description(description != null ? description : "Payment")
                    .cancelUrl(ngrokUrl + "/api/payment/cancel")
                    .returnUrl(ngrokUrl + "/api/payment/callback");

            PaymentData paymentData = paymentDataBuilder.build();

            String signature = SignatureUtils.createSignatureOfPaymentRequest(paymentData, "dee9853b0cb73699eed0f91d5dfd7ea4c98e55152e420b5fecb12ce115a40b06");
            paymentData.setSignature(signature);

            UserEntity user = userRepository.findEmail(email).orElse(null);
            Integer userId = user != null ? user.getUserId() : null;
            System.out.println("Initiate payment for email: " + email + ", found user: " + (user != null ? "yes" : "no"));

            paymentService.createPendingPayment(orderCodeStr, userId, (double) amount, description, membershipType);

            CheckoutResponseData response = payOS.createPaymentLink(paymentData);
            Map<String, String> result = new HashMap<>();
            String redirectUrl = "http://localhost:8082/api/payment/redirect-to-payos?url=" +
                    URLEncoder.encode(response.getCheckoutUrl() + "?name=" + URLEncoder.encode(name, "UTF-8") +
                            "&email=" + URLEncoder.encode(email, "UTF-8") + "&amount=" + amount +
                            (membershipType != null ? "&membershipType=" + URLEncoder.encode(membershipType, "UTF-8") : ""), "UTF-8");
            result.put("paymentUrl", redirectUrl);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/redirect-to-payos")
    public void redirectToPayos(@RequestParam String url, HttpServletResponse response) throws IOException {
        response.setHeader("ngrok-skip-browser-warning", "true");
        response.sendRedirect(url);
    }

    @RequestMapping(value = "/callback", method = {RequestMethod.GET, RequestMethod.POST})
    public void handleCallback(@RequestBody(required = false) Map<String, Object> body,
                               @RequestParam Map<String, String> allParams,
                               HttpServletResponse response) throws IOException {
        System.out.println("Callback received - Params: " + allParams + ", Body: " + body);
        String orderCode = allParams.get("orderCode");
        String name = allParams.get("name");
        String email = allParams.get("email");
        String membershipType = allParams.get("membershipType");
        if (body != null && body.get("orderCode") != null) {
            orderCode = String.valueOf(body.get("orderCode"));
        }
        String status = allParams.get("status");
        if (body != null && body.get("status") != null) {
            status = String.valueOf(body.get("status"));
        }

        if (orderCode != null && status != null) {
            System.out.println("Processing orderCode: " + orderCode + ", status: " + status + ", membershipType: " + membershipType + ", email: " + email);
            if ("PAID".equalsIgnoreCase(status) || "SUCCESS".equalsIgnoreCase(status)) {
                paymentService.updatePaymentStatus(orderCode, "SUCCESS");

                UserEntity user = userRepository.findEmail(email).orElse(null);
                if (user != null) {
                    System.out.println("Found user with email: " + user.getEmail() + ", current RoleID: " + (user.getRoleId() != null ? user.getRoleId().getRoleId() : "null"));
                    RoleEntity sellerRole = roleRepository.findById(3).orElse(null);
                    if (sellerRole != null) {
                        user.setRoleId(sellerRole);
                        userRepository.save(user);
                        System.out.println("Role updated to Seller (RoleID: 3) for user: " + user.getEmail() + ", new RoleID: " + user.getRoleId().getRoleId());
                    } else {
                        System.out.println("Seller role (ID: 3) not found in database");
                    }
                } else {
                    System.out.println("User not found with email: " + email + ". Consider adding this user to the database.");
                }

                if (membershipType != null) {
                    Integer membershipId = null;
                    switch (membershipType.toUpperCase()) {
                        case "BASIC":
                            membershipId = 1;
                            break;
                        case "ADVANCED":
                            membershipId = 2;
                            break;
                        case "PREMIUM":
                            membershipId = 3;
                            break;
                    }
                    if (membershipId != null && user != null) {
                        PaymentEntity payment = paymentRepository.findByTransactionCode(orderCode);
                        if (payment != null) {
                            payment.setMembershipId(membershipId);
                            payment.setUserId(user.getUserId());
                            payment.setNote(membershipType);
                            paymentRepository.save(payment);
                        }

                        PackageMemberEntity packageMember = new PackageMemberEntity();
                        packageMember.setUserId(user.getUserId());
                        packageMember.setMembershipId(membershipId);
                        packageMember.setStartDate(LocalDate.now());
                        packageMember.setEndDate(LocalDate.now().plusMonths(1));
                        packageMember.setStatus("ACTIVE");
                        packageMemberRepository.save(packageMember);
                    }
                }
            } else if ("CANCELLED".equalsIgnoreCase(status)) {
                paymentService.updatePaymentStatus(orderCode, "CANCELLED");
            }
        } else {
            System.out.println("Missing orderCode or status in callback");
        }

        String redirectUrl = "http://localhost:3000/payment"; // Mặc định
        if (orderCode != null && ("PAID".equalsIgnoreCase(status) || "SUCCESS".equalsIgnoreCase(status))) {
            PaymentEntity payment = paymentRepository.findByTransactionCode(orderCode);
            if (payment != null && payment.getNote() != null) {
                String noteMembershipType = payment.getNote().toLowerCase();
                if (noteMembershipType.equals("basic") || noteMembershipType.equals("advanced") || noteMembershipType.equals("premium")) {
                    redirectUrl = "http://localhost:3000/seller/dashboard/" + noteMembershipType;
                }
            } else if (membershipType != null) {
                redirectUrl = "http://localhost:3000/seller/dashboard/" + membershipType.toLowerCase();
            }
        }
        System.out.println("Redirecting to: " + redirectUrl);
        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/cancel")
    public void cancelPayment(@RequestParam Map<String, String> allParams,
                              HttpServletResponse response) throws IOException {
        System.out.println("Cancel received with params: " + allParams);
        String orderCode = allParams.get("orderCode");
        String status = allParams.get("status");
        if (orderCode != null && status != null) {
            System.out.println("Processing cancel orderCode: " + orderCode + ", status: " + status);
            paymentService.updatePaymentStatus(orderCode, "CANCELLED");
        } else {
            System.out.println("Missing orderCode or status in cancel");
        }
        response.sendRedirect("http://localhost:3000/payment");
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


