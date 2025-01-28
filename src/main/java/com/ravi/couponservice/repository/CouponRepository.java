package com.ravi.couponservice.repository;

import com.ravi.couponservice.modal.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

   Coupon findByCode(String code);
}
