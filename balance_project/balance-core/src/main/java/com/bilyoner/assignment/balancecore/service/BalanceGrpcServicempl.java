package com.bilyoner.assignment.balancecore.service;

import com.bilyoner.assignment.balance.api.BalanceRequest;
import com.bilyoner.assignment.balance.api.BalanceResponse;
import com.bilyoner.assignment.balance.api.BalanceServiceGrpc;
import com.bilyoner.assignment.balance.api.BalanceUpdateRequest;
import com.bilyoner.assignment.balance.api.BalanceUpdateResponse;
import com.bilyoner.assignment.balanceapi.model.UpdateBalanceRequest;
import com.bilyoner.assignment.balanceapi.model.UserBalanceDTO;
import com.bilyoner.assignment.balanceapi.service.BalanceService;
import com.bilyoner.assignment.balanceapi.util.ConverterUtil;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
@RequiredArgsConstructor
@Slf4j
public class BalanceGrpcServicempl extends BalanceServiceGrpc.BalanceServiceImplBase {
    private final BalanceService balanceService;

    @Override
    public void updateUserBalance(BalanceUpdateRequest request, StreamObserver<BalanceUpdateResponse> responseObserver) {
        try {
            balanceService.updateBalance(UpdateBalanceRequest.builder()
                    .userId(request.getUserId())
                    .amount(ConverterUtil.bdecimalToBigDecimal(request.getAmount()))
                    .transactionId(request.getTransactionId())
                    .transactionType(request.getTransactionType().name())
                    .build());
            responseObserver.onNext(BalanceUpdateResponse.newBuilder()
                    .setResult(BalanceUpdateResponse.BalanceUpdateResult.SUCCESSFUL).build());
        } catch (Exception e) {
            responseObserver.onError(e);
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public void getUserBalance(BalanceRequest request, StreamObserver<BalanceResponse> responseObserver) {
        try {
            UserBalanceDTO userBalanceDTO = balanceService.getBalance(request.getUserId());
            responseObserver.onNext(BalanceResponse.newBuilder()
                    .setUserId(userBalanceDTO.getUserId())
                    .setAmount(ConverterUtil.bigDecimalToBdecimal(userBalanceDTO.getAmount()))
                    .build());
        } catch (Exception e) {
            responseObserver.onError(e);
        } finally {
            responseObserver.onCompleted();
        }
    }
}
