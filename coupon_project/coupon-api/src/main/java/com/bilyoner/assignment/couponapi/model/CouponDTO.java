package com.bilyoner.assignment.couponapi.model;

import com.bilyoner.assignment.couponapi.entity.CouponEntity;
import com.bilyoner.assignment.couponapi.entity.EventEntity;
import com.bilyoner.assignment.couponapi.model.enums.CouponStatusEnum;
import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
public class CouponDTO implements Serializable {

    private Long id;
    private Long userId;
    private CouponStatusEnum status;
    private BigDecimal cost;
    private List<Long> eventIds;
    private LocalDateTime playDate;

    public static CouponDTO mapEntityToDto(CouponEntity couponEntity) {
        return CouponDTO.builder()
                .id(couponEntity.getId())
                .userId(couponEntity.getUserId())
                .status(couponEntity.getStatus())
                .cost(couponEntity.getCost())
                .eventIds(couponEntity.getEvents().stream().map(EventEntity::getId).collect(Collectors.toList()))
                .playDate(couponEntity.getPlayDate())
                .build();
    }

    public static List<CouponDTO> mapEntityListToDto(List<CouponEntity> entityList) {
        List<CouponDTO> couponDtoList = Lists.newArrayList();
        entityList.forEach(couponEntity -> couponDtoList
                .add(mapEntityToDto(couponEntity)));
        return couponDtoList;
    }
}
