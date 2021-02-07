package com.bilyoner.assignment.couponapi.service;

import com.bilyoner.assignment.couponapi.model.CouponCreateRequest;
import com.bilyoner.assignment.couponapi.model.CouponDTO;
import com.bilyoner.assignment.couponapi.model.CouponPlayRequest;
import com.bilyoner.assignment.couponapi.model.enums.CouponStatusEnum;

import java.util.List;

public interface CouponService {

    List<CouponDTO> getAllCouponsByCouponStatus(CouponStatusEnum couponStatus);

    CouponDTO createCoupon(CouponCreateRequest couponCreateRequest);

    List<CouponDTO> playCoupons(CouponPlayRequest couponPlayRequest);

    CouponDTO cancelCoupon(Long couponId);

    List<CouponDTO> getPlayedCoupons(Long userId);
}
