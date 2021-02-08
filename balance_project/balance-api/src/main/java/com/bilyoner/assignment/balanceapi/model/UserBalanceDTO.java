package com.bilyoner.assignment.balanceapi.model;

import com.bilyoner.assignment.balanceapi.persistence.entity.UserBalanceEntity;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class UserBalanceDTO {
    private Long userId;
    private BigDecimal amount;

    public static UserBalanceDTO mapEntityToDto(UserBalanceEntity entity) {
        return UserBalanceDTO.builder()
                .userId(entity.getUserId())
                .amount(entity.getAmount())
                .build();
    }
}
