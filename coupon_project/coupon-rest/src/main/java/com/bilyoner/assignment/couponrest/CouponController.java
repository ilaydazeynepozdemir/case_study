package com.bilyoner.assignment.couponrest;

import com.bilyoner.assignment.couponapi.model.CouponCreateRequest;
import com.bilyoner.assignment.couponapi.model.CouponDTO;
import com.bilyoner.assignment.couponapi.model.CouponPlayRequest;
import com.bilyoner.assignment.couponapi.model.enums.CouponStatusEnum;
import com.bilyoner.assignment.couponapi.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    /**
     * TODO : Implement missing parts
     */

    public List<CouponDTO> getAllCouponsByCouponStatus(@RequestParam CouponStatusEnum couponStatus) {
        return couponService.getAllCouponsByCouponStatus(couponStatus);
    }

    public CouponDTO createCoupon(@RequestBody @Valid CouponCreateRequest couponCreateRequest) {
        return couponService.createCoupon(couponCreateRequest);
    }

    public List<CouponDTO> getPlayedCoupons(@PathVariable Long userId) {
        return couponService.getPlayedCoupons(userId);
    }

    public List<CouponDTO> playCoupons(@Valid @RequestBody CouponPlayRequest couponPlayRequest) {
        return couponService.playCoupons(couponPlayRequest);
    }

    public CouponDTO cancelCoupon(@PathVariable Long couponId) {
        return couponService.cancelCoupon(couponId);
    }
}