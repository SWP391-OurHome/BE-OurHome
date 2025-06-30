package com.javaweb.service.impl;

import com.javaweb.model.PaymentDTO;
import com.javaweb.repository.entity.PaymentEntity;
import com.javaweb.repository.impl.PaymentRepositoryImpl;
import com.javaweb.service.PaymentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        PaymentEntity payment = paymentRepository.findByTransactionCode(orderCode);
        if (payment != null) {
            payment.setStatus("CANCELLED".equalsIgnoreCase(status) ? "CANCELLED" : "SUCCESS");
            payment.setPaymentDate(LocalDateTime.now());
            paymentRepository.save(payment);
        }
    }
}

