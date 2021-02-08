package com.bilyoner.assignment.couponapi.model;

import com.bilyoner.assignment.couponapi.model.enums.TransactionTypeEnum;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class BalanceUpdateDTO {
    private Long userId;
    private BigDecimal amount;
    private TransactionTypeEnum transactionType;
    private String transactionId;
}
