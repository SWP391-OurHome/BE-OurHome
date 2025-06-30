
package com.javaweb.repository.impl;

import com.javaweb.repository.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepositoryImpl extends JpaRepository<PaymentEntity, Integer> {
    List<PaymentEntity> findByUserId(Integer userId);
    PaymentEntity findByTransactionCode(String transactionCode);
}
