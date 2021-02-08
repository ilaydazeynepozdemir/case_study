package com.bilyoner.assignment.balancecore.service;

import com.bilyoner.assignment.balanceapi.model.UserBalanceDTO;
import com.bilyoner.assignment.balanceapi.model.UpdateBalanceRequest;
import com.bilyoner.assignment.balanceapi.persistence.entity.UserBalanceEntity;
import com.bilyoner.assignment.balanceapi.persistence.entity.UserBalanceHistoryEntity;
import com.bilyoner.assignment.balanceapi.persistence.repository.UserBalanceHistoryRepository;
import com.bilyoner.assignment.balanceapi.persistence.repository.UserBalanceRepository;
import com.bilyoner.assignment.balanceapi.service.BalanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class BalanceServiceImpl implements BalanceService {
    private final UserBalanceHistoryRepository userBalanceHistoryRepository;
    private final UserBalanceRepository userBalanceRepository;

    @Override
    public void createBalance(UserBalanceDTO userBalanceDTO) {
        UserBalanceEntity userBalanceEntity = UserBalanceEntity.builder()
                .userId(userBalanceDTO.getUserId())
                .amount(userBalanceDTO.getAmount())
                .updateDate(null) // created
                .build();
        userBalanceRepository.save(userBalanceEntity);
        userBalanceHistoryRepository.save(UserBalanceHistoryEntity.builder()
                .userBalance(userBalanceEntity)
                .build());
    }

    @Override
    public void updateBalance(UpdateBalanceRequest updateBalanceRequest) {
        UserBalanceEntity userBalanceEntity = UserBalanceEntity.builder()
                .userId(updateBalanceRequest.getUserId())
                .amount(updateBalanceRequest.getAmount())
                .updateDate(LocalDateTime.now())
                .build();
        userBalanceRepository.save(userBalanceEntity);
        userBalanceHistoryRepository.save(UserBalanceHistoryEntity.builder()
                .transactionId(updateBalanceRequest.getTransactionId())
                .transactionType(updateBalanceRequest.getTransactionType())
                .userBalance(userBalanceEntity)
                .build());
    }

    @Override
    public UserBalanceDTO getBalance(Long userId) {
        Optional<UserBalanceEntity> userBalanceEntity = userBalanceRepository.findById(userId);
        return userBalanceEntity.map(UserBalanceDTO::mapEntityToDto).orElse(null);
    }

}
