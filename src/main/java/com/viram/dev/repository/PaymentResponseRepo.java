package com.viram.dev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viram.dev.dto.PaymentResponse;

@Repository
public interface PaymentResponseRepo extends JpaRepository<PaymentResponse, Integer> {

}
