package com.bilyoner.assignment.couponcore.service;

import com.bilyoner.assignment.couponapi.entity.CouponEntity;
import com.bilyoner.assignment.couponapi.entity.EventEntity;
import com.bilyoner.assignment.couponapi.exception.CustomException;
import com.bilyoner.assignment.couponapi.exception.ErrorCodeEnum;
import com.bilyoner.assignment.couponapi.model.CouponCreateRequest;
import com.bilyoner.assignment.couponapi.model.CouponDTO;
import com.bilyoner.assignment.couponapi.model.CouponPlayRequest;
import com.bilyoner.assignment.couponapi.model.EventDTO;
import com.bilyoner.assignment.couponapi.model.enums.CouponStatusEnum;
import com.bilyoner.assignment.couponapi.repository.CouponRepository;
import com.bilyoner.assignment.couponapi.repository.EventRepository;
import com.bilyoner.assignment.couponapi.service.CouponService;
import com.bilyoner.assignment.couponapi.service.EventService;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponServiceImpl implements CouponService {

    private final static BigDecimal COUPON_COST = new BigDecimal(5);

    CouponServiceImpl(EventRepository eventRepository, CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
        this.eventRepository = eventRepository;
    }

    private final CouponRepository couponRepository;
    private final EventRepository eventRepository;

    public List<CouponDTO> getAllCouponsByCouponStatus(CouponStatusEnum couponStatus) {
        return CouponDTO.mapEntityListToDto(couponRepository.findByStatus(couponStatus));
    }

    public CouponDTO createCoupon(CouponCreateRequest couponCreateRequest) {
        try {
            List<EventEntity> eventEntities = Lists.newArrayList();
            for (Long id : couponCreateRequest.getEventIds()) {
                EventEntity eventEntity = eventRepository.findById(id).orElse(null);
                Preconditions.checkNotNull(eventEntity, "Event not found with id {}", id);
                eventEntities.add(eventEntity);
            }
            return CouponDTO.mapEntityToDto(couponRepository.save(
                    CouponEntity.builder()
                            .cost(COUPON_COST)
                            .events(eventEntities)
                            .status(CouponStatusEnum.CREATED)
                            .createDate(LocalDateTime.now())
                            .build()));
        } catch (NullPointerException e) {
            log.error("", e);
            return null;
        }
    }

    public List<CouponDTO> playCoupons(CouponPlayRequest couponPlayRequest) {

        /**
         * TODO : Implement play coupons
         */
        return null;
    }

    public CouponDTO cancelCoupon(Long couponId) {
        /**
         * TODO : Implement cancel coupon
         */
        return null;
    }

    public List<CouponDTO> getPlayedCoupons(Long userId) {
        /**
         * TODO : Implement get played coupons
         */
        return null;
    }


}
