package com.bilyoner.assignment.couponapi.model.enums;

public enum ValidationTypeEnum {
    FAIL_MBS("Should be paid to the mbs rule"),
    FAIL_FOOTBALL_AND_TENNIS_SAME_COUPON ("Football and Tennis match cannot be included in the same coupon"),
    FAIL_INVALID_DATE("Cannot be created from out-of-date matches"),
    FAIL_CANCELED_COUPON("Coupon has been canceled"),
    FAIL_INSUFFICIENT_BALANCE("Insufficient balance"),
    FAIL_ALREADY_PURCHASE("Only one user should be able to purchase a coupon"),
    SUCCESS("The coupon has been successfully created");

    private String detail;
    ValidationTypeEnum(String detail){
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }
}
