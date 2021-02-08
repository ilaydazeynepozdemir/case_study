package com.bilyoner.assignment.couponcore.service;

import com.bilyoner.assignment.balance.api.BDecimal;
import com.bilyoner.assignment.balance.api.BalanceRequest;
import com.bilyoner.assignment.balance.api.BalanceServiceGrpc;
import com.bilyoner.assignment.balance.api.BalanceUpdateRequest;
import com.bilyoner.assignment.balance.api.BalanceUpdateResponse;
import com.bilyoner.assignment.couponapi.model.BalanceUpdateDTO;
import com.bilyoner.assignment.couponapi.service.BalanceService;
import com.bilyoner.assignment.couponapi.util.ConverterUtil;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

@Service
@Slf4j
public class BalanceServiceImpl implements BalanceService {

    @Value("${balance.service.ip:localhost}")
    private String balanceServiceIp;

    @Value("${balance.service.port:6565}")
    private int balanceServicePort;

    private BalanceServiceGrpc.BalanceServiceBlockingStub balanceStub;
    private ManagedChannel channel = null;

    @PostConstruct
    public void init() {
        refreshStub();
    }

    @PreDestroy
    public void destroy() {
        shutdownChannel();
    }

    @Override
    public void updateBalance(BalanceUpdateDTO balanceUpdateDTO) { // WARNING: I changed the input value of this function !!!
        try {
            if (balanceStub == null) {
                refreshStub();
            }
            BalanceUpdateResponse response = balanceStub.updateUserBalance(BalanceUpdateRequest.newBuilder()
                    .setUserId(balanceUpdateDTO.getUserId())
                    .setAmount(ConverterUtil.bigDecimalToBdecimal(balanceUpdateDTO.getAmount()))
                    .setTransactionId(balanceUpdateDTO.getTransactionId())
                    .setTransactionType(BalanceUpdateRequest.TransactionType
                            .valueOf(balanceUpdateDTO.getTransactionType().name()))
                    .build());
            log.info("Update balance response: {}", response.getResult());
        } catch (Exception e) {
            log.error("Balance did not update.");
            shutdownChannel();
        }
    }

    @Override
    public BigDecimal getAmount(Long userId) {
        BDecimal amount = balanceStub.getUserBalance(BalanceRequest.newBuilder()
                .setUserId(userId).build()).getAmount();
        MathContext mc = new MathContext(amount.getPrecision());
        return new java.math.BigDecimal(
                new BigInteger(amount.getValue().toByteArray()),
                amount.getScale(),
                mc);
    }

    public void refreshStub() {
        if (channel == null) {
            channel = getChannel();
            log.info("Channel created for {}:{}", balanceServiceIp, balanceServicePort);
        }
        if (balanceStub != null) {
            balanceStub = BalanceServiceGrpc.newBlockingStub(channel);
        }
    }

    public ManagedChannel getChannel() {
        synchronized (this) {
            if (channel == null) {
                try {
                    return ManagedChannelBuilder.forAddress(balanceServiceIp, balanceServicePort).usePlaintext().build();
                } catch (RuntimeException var5) {
                    log.warn("Channel is not created for ip:{} port:{}", balanceServiceIp, balanceServicePort);
                    return null;
                }
            }
            return channel;
        }
    }

    public void shutdownChannel() {
        if (channel != null) {
            channel.shutdown();
            channel = null;
            balanceStub = null;
        }
    }
}
