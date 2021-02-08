package com.bilyoner.assignment.balanceapi.service;

import com.bilyoner.assignment.balanceapi.model.UpdateBalanceRequest;
import com.bilyoner.assignment.balanceapi.model.UserBalanceDTO;

public interface BalanceService {
    void createBalance(UserBalanceDTO userBalanceDTO);

    void updateBalance(UpdateBalanceRequest updateBalanceRequest);

    UserBalanceDTO getBalance(Long userId);
}
