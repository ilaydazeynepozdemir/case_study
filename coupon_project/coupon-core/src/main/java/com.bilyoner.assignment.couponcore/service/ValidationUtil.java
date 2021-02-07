package com.bilyoner.assignment.couponcore.service;

import com.bilyoner.assignment.couponapi.entity.CouponEntity;
import com.bilyoner.assignment.couponapi.entity.EventEntity;
import com.bilyoner.assignment.couponapi.model.enums.EventTypeEnum;
import com.bilyoner.assignment.couponapi.model.enums.ValidationTypeEnum;

import java.time.LocalDateTime;

public class ValidationUtil {
    private static final boolean VALID = true;

    private ValidationUtil() {
    }

    public static ValidationTypeEnum isValidCoupon(CouponEntity couponEntity) {
        ValidationTypeEnum eventResponse = eventCheck(couponEntity);
        if(eventResponse.equals(ValidationTypeEnum.SUCCESS)){

        }

        return ValidationTypeEnum.SUCCESS;
    }

    private static ValidationTypeEnum eventCheck(CouponEntity couponEntity) { // mbs, football&tennis, invalid-date
        boolean footballEvent = false;
        boolean tennisEvent = false;
        int maxMbs = 0;

        for (EventEntity eventEntity : couponEntity.getEvents()) {
            if(eventEntity.getEventDate().isBefore(LocalDateTime.now())){
                return ValidationTypeEnum.FAIL_INVALID_DATE; // invalid date
            }

            if(eventEntity.getMbs() > maxMbs){ // find max mbs
                maxMbs = eventEntity.getMbs();
            }

            if (eventEntity.getType().equals(EventTypeEnum.FOOTBALL)) {
                footballEvent = true;
            } else if (eventEntity.getType().equals(EventTypeEnum.TENNIS)) {
                tennisEvent = true;
            }
        }
        if (footballEvent && tennisEvent) {
            return ValidationTypeEnum.FAIL_FOOTBALL_AND_TENNIS_SAME_COUPON;
        } else {
            if(couponEntity.getEvents().size() < maxMbs){ // number of events < max-mbs
                return ValidationTypeEnum.FAIL_MBS;
            }
            //valid for rule2
            return ValidationTypeEnum.SUCCESS;
        }
    }

}
