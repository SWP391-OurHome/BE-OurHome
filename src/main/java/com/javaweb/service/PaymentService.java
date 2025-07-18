
package com.javaweb.service;

import com.javaweb.model.PaymentDTO;
import java.util.List;

public interface PaymentService {
    List<PaymentDTO> getTransactionHistory(Integer userId);
    List<PaymentDTO> getTransactionHistory(Integer userId, String transactionCode, String status, String paymentDate);
    List<PaymentDTO> getAllTransactionHistory(String transactionCode, String status, String paymentDate);

    void updateTransactionStatus(String orderCode, String status);
    void createPendingPayment(String orderCode, Integer userId, Double amount, String description, String membershipType);
    void updatePaymentStatus(String orderCode, String status);

    //PayOS check somthing
    boolean checkAndCreatePendingPayment(String transactionCode, Integer userId, Double amount, String description, Integer membershipId);
    boolean isValidMembershipId(Integer membershipId);
    Integer getUserIdByEmail(String email);
    Integer getUserIdByTransactionCode(String transactionCode);
    void cancelPayment(String transactionCode);
    void updateUserRoleToSeller(Integer userId);
    Integer getUserRole(Integer userId);
}

