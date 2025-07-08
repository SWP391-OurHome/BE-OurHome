package com.javaweb.service.impl;

import com.javaweb.model.PaymentDTO;
import com.javaweb.repository.entity.PaymentEntity;
import com.javaweb.repository.entity.RoleEntity;
import com.javaweb.repository.entity.UserEntity;
import com.javaweb.repository.impl.MembershipRepositoryImpl;
import com.javaweb.repository.impl.PaymentRepositoryImpl;
import com.javaweb.repository.impl.RoleRepositoryImpl;
import com.javaweb.repository.impl.UserRepositoryImpl;
import com.javaweb.service.PaymentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepositoryImpl paymentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private RoleRepositoryImpl roleRepository;

    @Autowired
    private MembershipRepositoryImpl membershipRepo;

    @Override
    @Transactional
    public boolean checkAndCreatePendingPayment(String transactionCode, Integer userId, Double amount, String description, Integer membershipId) {
        PaymentEntity existingPayment = paymentRepository.findByTransactionCode(transactionCode);
        if (existingPayment != null) {
            return false;
        }

        PaymentEntity payment = new PaymentEntity();
        payment.setTransactionCode(transactionCode);
        payment.setUserId(userId);
        payment.setAmount(amount);
        payment.setNote(description);
        payment.setMembershipId(membershipId);
        payment.setStatus("SUCCESS");
        payment.setPaymentDate(LocalDateTime.now());
        paymentRepository.save(payment);

        // Check and update user role from 2 to 3
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user != null && user.getRole() != null && user.getRole().getRoleId() == 2) {
            updateUserRoleToSeller(userId);
            System.out.println("[checkAndCreatePendingPayment] Updated role for userId=" + userId + " to roleId=3 after creating new transaction");
        } else {
            System.out.println("[checkAndCreatePendingPayment] User role not updated. Current role=" +
                    (user != null && user.getRole() != null ? user.getRole().getRoleId() : "null") + ", userId=" + userId);
        }

        return true;
    }


    @Override
    public boolean isValidMembershipId(Integer membershipId) {
        boolean isValid = membershipRepo.findById(membershipId).isPresent();
        return isValid;
    }

    @Override
    public Integer getUserIdByEmail(String email) {
        UserEntity user = userRepository.findEmail(email).orElse(null);
        if (user == null) {
            return null;
        }
        return user.getUserId();
    }

    @Override
    public Integer getUserIdByTransactionCode(String transactionCode) {
        PaymentEntity payment = paymentRepository.findByTransactionCode(transactionCode);
        if (payment == null) {
            return null;
        }
        return payment.getUserId();
    }

    @Override
    @Transactional
    public void cancelPayment(String transactionCode) {
        PaymentEntity payment = paymentRepository.findByTransactionCode(transactionCode);
        if (payment != null) {
            payment.setStatus("CANCELLED");
            paymentRepository.save(payment);
        }
    }

    @Override
@Transactional
public void updateUserRoleToSeller(Integer userId) {
    UserEntity user = userRepository.findById(userId).orElse(null);
    if (user == null) {
        System.out.println("[updateUserRoleToSeller] User not found for userId=" + userId);
        return;
    }

    RoleEntity beforeRole = user.getRole();
    int beforeRoleId = beforeRole != null ? beforeRole.getRoleId() : -1;
    System.out.println("[updateUserRoleToSeller] Before update: userId=" + userId + ", roleId=" + beforeRoleId);

    RoleEntity sellerRole = roleRepository.findById(3).orElse(null);
    if (sellerRole == null) {
        System.out.println("[updateUserRoleToSeller] Seller role (id=3) not found!");
        return;
    }

    user.setRole(sellerRole);
    userRepository.save(user);
    System.out.println("[updateUserRoleToSeller] After update: userId=" + userId + ", roleId=" + user.getRole().getRoleId());
}

    @Override
    public List<PaymentDTO> getTransactionHistory(Integer userId) {
        List<PaymentEntity> payments = paymentRepository.findByUserId(userId);
        return payments.stream()
                .map(payment -> modelMapper.map(payment, PaymentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentDTO> getTransactionHistory(Integer userId, String transactionCode, String status, String paymentDate) {
        List<PaymentEntity> payments = paymentRepository.findByUserId(userId);
        return payments.stream()
                .filter(payment -> {
                    boolean matches = true;
                    if (transactionCode != null && !transactionCode.isEmpty()) {
                        matches = matches && payment.getTransactionCode().toLowerCase().contains(transactionCode.toLowerCase());
                    }
                    if (status != null && !status.isEmpty()) {
                        matches = matches && payment.getStatus().equalsIgnoreCase(status);
                    }
                    if (paymentDate != null && !paymentDate.isEmpty()) {
                        try {
                            LocalDateTime searchDateTime = LocalDate.parse(paymentDate).atStartOfDay();
                            LocalDateTime paymentDateTime = payment.getPaymentDate();
                            matches = matches && paymentDateTime.toLocalDate().isEqual(searchDateTime.toLocalDate());
                        } catch (Exception e) {
                            System.err.println("Lỗi phân tích ngày: " + paymentDate + " - " + e.getMessage());
                            matches = false;
                        }
                    }
                    return matches;
                })
                .map(payment -> modelMapper.map(payment, PaymentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentDTO> getAllTransactionHistory(String transactionCode, String status, String paymentDate) {
        List<PaymentEntity> payments = paymentRepository.findAll();
        return payments.stream()
                .filter(payment -> {
                    boolean matches = true;
                    if (transactionCode != null && !transactionCode.isEmpty()) {
                        matches = matches && payment.getTransactionCode().toLowerCase().contains(transactionCode.toLowerCase());
                    }
                    if (status != null && !status.isEmpty()) {
                        matches = matches && payment.getStatus().equalsIgnoreCase(status);
                    }
                    if (paymentDate != null && !paymentDate.isEmpty()) {
                        try {
                            LocalDateTime searchDateTime = LocalDate.parse(paymentDate).atStartOfDay();
                            LocalDateTime paymentDateTime = payment.getPaymentDate();
                            matches = matches && paymentDateTime.toLocalDate().isEqual(searchDateTime.toLocalDate());
                        } catch (Exception e) {
                            System.err.println("Lỗi phân tích ngày: " + paymentDate + " - " + e.getMessage());
                            matches = false;
                        }
                    }
                    return matches;
                })
                .map(payment -> modelMapper.map(payment, PaymentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void updateTransactionStatus(String orderCode, String status) {
        PaymentEntity payment = paymentRepository.findByTransactionCode(orderCode);
        if (payment != null) {
            payment.setStatus(status);
            paymentRepository.save(payment);
        }
    }

    @Override
    public void createPendingPayment(String orderCode, Integer userId, Double amount, String description, String membershipType) {
        PaymentEntity payment = new PaymentEntity();
        payment.setTransactionCode(orderCode);
        payment.setStatus("PENDING");
        payment.setPaymentDate(LocalDateTime.now());
        payment.setUserId(userId);
        payment.setAmount(amount);
        payment.setReceivedBy("System");
        payment.setNote(membershipType != null ? membershipType : description); // Lưu membershipType trực tiếp
        paymentRepository.save(payment);
    }

    @Override
    public void updatePaymentStatus(String orderCode, String status) {

    }

    public Integer getUserRole(Integer userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null || user.getRole() == null) return null;
        return user.getRole().getRoleId();
    }

}

