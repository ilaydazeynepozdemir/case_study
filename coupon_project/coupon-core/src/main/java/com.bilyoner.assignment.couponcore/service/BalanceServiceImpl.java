package com.bilyoner.assignment.couponcore.service;

import com.bilyoner.assignment.balanceapi.BalanceServiceGrpc;
import com.bilyoner.assignment.couponapi.service.BalanceService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

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
    public void updateBalance() {
        try {
            if (balanceStub == null) {
                refreshStub();
            }


        } catch (Exception e) {
            log.error("Balance did not update.");
            shutdownChannel();
        }
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
