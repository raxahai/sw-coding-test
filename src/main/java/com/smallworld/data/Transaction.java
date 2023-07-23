package com.smallworld.data;

import lombok.Data;

@Data
public class Transaction {
    private Long transactionId;
    private Double amount;
    private Sender sender;
    private Beneficiary beneficiary;
    private Issue issue;
}
