package com.ravi.couponservice.modal;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String code;
    @Column
    private BigDecimal discount;
    @Column(name = "exp_date")
    private String expDate;

    public Coupon() {}

    public Coupon(String code, BigDecimal discount, String exp_date) {
        this.code = code;
        this.discount = discount;
        this.expDate = exp_date;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getExp_date() {
        return expDate;
    }

    public void setExp_date(String exp_date) {
        this.expDate = exp_date;
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", discount=" + discount +
                ", exp_date='" + expDate + '\'' +
                '}';
    }
}
