package com.bilyoner.assignment.couponcore.service;

import com.bilyoner.assignment.couponapi.entity.CouponEntity;
import com.bilyoner.assignment.couponapi.entity.EventEntity;
import com.bilyoner.assignment.couponapi.model.BalanceUpdateDTO;
import com.bilyoner.assignment.couponapi.model.CouponCreateRequest;
import com.bilyoner.assignment.couponapi.model.CouponDTO;
import com.bilyoner.assignment.couponapi.model.CouponPlayRequest;
import com.bilyoner.assignment.couponapi.model.enums.CouponStatusEnum;
import com.bilyoner.assignment.couponapi.model.enums.TransactionTypeEnum;
import com.bilyoner.assignment.couponapi.model.enums.ValidationTypeEnum;
import com.bilyoner.assignment.couponapi.repository.CouponRepository;
import com.bilyoner.assignment.couponapi.repository.EventRepository;
import com.bilyoner.assignment.couponapi.service.BalanceService;
import com.bilyoner.assignment.couponapi.service.CouponService;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponServiceImpl implements CouponService {

    private static AtomicLong transactionId = new AtomicLong(0);

    private static final BigDecimal COUPON_COST = new BigDecimal(5);

    private final CouponRepository couponRepository;
    private final EventRepository eventRepository; // TODO EventService

    private final BalanceService balanceService;

    public List<CouponDTO> getAllCouponsByCouponStatus(CouponStatusEnum couponStatus) {
        return CouponDTO.mapEntityListToDto(couponRepository.findByStatus(couponStatus));
    }

    public CouponDTO createCoupon(CouponCreateRequest couponCreateRequest) {
        List<EventEntity> eventEntities = Lists.newArrayList();
        for (Long id : couponCreateRequest.getEventIds()) {
            EventEntity eventEntity = eventRepository.findById(id).orElse(null);
            Preconditions.checkNotNull(eventEntity, "Event not found with id {}", id);
            eventEntities.add(eventEntity);
        }
        CouponEntity couponEntity = CouponEntity.builder()
                .cost(COUPON_COST)
                .events(eventEntities)
                .status(CouponStatusEnum.CREATED)
                .createDate(LocalDateTime.now())
                .build();
        ValidationTypeEnum check = ValidationUtil.checkValidForCreation(couponEntity);
        Preconditions.checkArgument(check.equals(ValidationTypeEnum.SUCCESS), check.getDetail());
        return CouponDTO.mapEntityToDto(couponRepository.save(couponEntity));
    }

    public List<CouponDTO> playCoupons(CouponPlayRequest couponPlayRequest) {
        synchronized (this) {
            BigDecimal totalCouponCost = COUPON_COST.multiply(new BigDecimal(couponPlayRequest.getCouponIds().size()));
            BigDecimal amountOfUSer = balanceService.getAmount(couponPlayRequest.getUserId());
            ValidationTypeEnum check = ValidationUtil.checkBalance(totalCouponCost, amountOfUSer);
            Preconditions.checkArgument(check.equals(ValidationTypeEnum.SUCCESS), check.getDetail());
            //BLOCKED BALANCE
            balanceService.updateBalance(BalanceUpdateDTO.builder()
                    .userId(couponPlayRequest.getUserId())
                    .transactionId(String.valueOf(transactionId.get()))
                    .transactionType(TransactionTypeEnum.BLOCKED)
                    .amount(amountOfUSer)
                    .build());
            List<CouponEntity> updated = Lists.newArrayList();
            for (Long couponId : couponPlayRequest.getCouponIds()) {
                Optional<CouponEntity> couponEntity = couponRepository.findById(couponId);
                if (couponEntity.isPresent()) {
                    log.error("Undefined coupon {} Transaction failed {}", couponId, transactionId);
                    //ROLLBACK COUPON
                    rollback(updated);
                    throw new IllegalArgumentException("Undefined coupon. ");
                } else {
                    //UPDATED COUPON
                    CouponEntity coupon = couponEntity.get();
                    coupon.setStatus(CouponStatusEnum.PLAYED);
                    coupon.setPlayDate(LocalDateTime.now());
                    coupon.setUpdateDate(LocalDateTime.now());
                    couponRepository.save(coupon);
                    updated.add(coupon);
                }
            }
            //UPDATED BALANCE
            balanceService.updateBalance(BalanceUpdateDTO.builder()
                    .userId(couponPlayRequest.getUserId())
                    .transactionId(String.valueOf(transactionId.get()))
                    .transactionType(TransactionTypeEnum.UPDATED)
                    .amount(amountOfUSer.subtract(totalCouponCost))
                    .build());
            log.info("user-id: {} transaction id: {}", couponPlayRequest.getUserId(), transactionId.get());
            //INCREMENT TRANSACTION ID
            transactionId.getAndIncrement();
            return CouponDTO.mapEntityListToDto(updated);
        }
    }

    public void rollback(List<CouponEntity> updatedCoupons) {
        for (CouponEntity couponEntity : updatedCoupons) {
            couponEntity.setPlayDate(null);
            couponEntity.setUpdateDate(LocalDateTime.now());
            couponEntity.setStatus(CouponStatusEnum.CREATED);
            couponRepository.save(couponEntity);
        }
        log.debug("Rollback for coupons: {}", updatedCoupons);
    }

    public CouponDTO cancelCoupon(Long couponId) {
        CouponEntity couponEntity = couponRepository.findById(couponId).orElse(null);
        if (couponEntity != null) {
            ValidationTypeEnum check = ValidationUtil.checkCanceledDate(LocalDateTime.now(), couponEntity.getPlayDate());
            Preconditions.checkArgument(check.equals(ValidationTypeEnum.SUCCESS), check.getDetail());

            // cancelled: 04.50
            // played: 04.45,...,04.50
            BigDecimal amountOfBalance = balanceService.getAmount(couponEntity.getUserId());
            log.info("Cancel coupon {} transaction {}", couponId, transactionId.get());
            balanceService.updateBalance(BalanceUpdateDTO.builder()
                    .userId(couponEntity.getUserId())
                    .amount(amountOfBalance.add(COUPON_COST))
                    .transactionType(TransactionTypeEnum.UPDATED)
                    .transactionId(String.valueOf(transactionId.getAndIncrement()))
                    .build());
            return CouponDTO.mapEntityToDto(couponEntity);
        } else {
            throw new IllegalArgumentException("Undefined coupon");
        }
    }

    public List<CouponDTO> getPlayedCoupons(Long userId) {
        return CouponDTO.mapEntityListToDto(couponRepository.findByStatus(CouponStatusEnum.PLAYED));
    }
}
