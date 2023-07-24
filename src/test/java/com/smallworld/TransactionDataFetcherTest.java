package com.smallworld;

import com.smallworld.data.Transaction;
import com.smallworld.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TransactionDataFetcherTest {
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private  TransactionDataFetcher transactionDataFetcher;

    @Test
    void shouldGetTotalTransactionAmount(){
        Mockito.when(transactionRepository.findAll()).thenReturn(getTransactions());
        Assertions.assertEquals(25.0, transactionDataFetcher.getTotalTransactionAmount());
    }

    /**
     * Mocking transactional data
     *
     * @return transactions
     */
    private List<Transaction> getTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1L);
        transaction.setAmount(12.5);
        transactions.add(transaction);
        transaction = new Transaction();
        transaction.setTransactionId(2L);
        transaction.setAmount(12.5);
        transactions.add(transaction);
        return transactions;
    }
}