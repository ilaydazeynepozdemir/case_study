package com.bilyoner.assignment.balanceapi.persistence.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Builder
@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserBalanceHistoryEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column()
    private String transactionId;

    @Column()
    private String transactionType;

    @ManyToOne
    private UserBalanceEntity userBalance;
}
