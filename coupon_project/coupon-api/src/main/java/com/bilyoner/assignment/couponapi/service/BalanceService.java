package com.bilyoner.assignment.couponapi.service;

import com.bilyoner.assignment.couponapi.model.BalanceUpdateDTO;

import java.math.BigDecimal;

public interface BalanceService {

    void updateBalance(BalanceUpdateDTO balanceUpdateDTO);// WARNING: I changed the input value of this function !!!

    BigDecimal getAmount(Long userId);
}
