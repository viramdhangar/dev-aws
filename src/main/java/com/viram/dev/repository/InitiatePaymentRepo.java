package com.viram.dev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viram.dev.dto.InitiatePaymentDTO;

@Repository
public interface InitiatePaymentRepo extends JpaRepository<InitiatePaymentDTO, Integer>{

}
