package com.ravi.couponservice.controller;

import com.ravi.couponservice.modal.Coupon;
import com.ravi.couponservice.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupon_api")
//@CrossOrigin
public class CouponController {

    @Autowired
    CouponRepository couponRepository;

    @PostMapping("/coupons")
    public Coupon createCoupons(@RequestBody Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @GetMapping("/coupons/{code}")
    public Coupon getCoupon(@PathVariable("code") String code) {
        return  couponRepository.findByCode(code);
    }

    @GetMapping("/coupons/allCoupon")
    public List<Coupon> getCoupon() {
        return  couponRepository.findAll();
    }
}
