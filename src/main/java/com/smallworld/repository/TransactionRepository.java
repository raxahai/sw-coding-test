package com.smallworld.repository;

import com.smallworld.data.Transaction;

import java.util.List;

public interface TransactionRepository {
    List<Transaction> findAll();
}
