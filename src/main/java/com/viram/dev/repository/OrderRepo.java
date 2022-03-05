package com.viram.dev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viram.dev.dto.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order, Integer>{

}
