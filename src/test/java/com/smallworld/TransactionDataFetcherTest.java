package com.smallworld;

import com.smallworld.data.Beneficiary;
import com.smallworld.data.Issue;
import com.smallworld.data.Sender;
import com.smallworld.data.Transaction;
import com.smallworld.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.unitils.reflectionassert.ReflectionAssert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class TransactionDataFetcherTest {
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private  TransactionDataFetcher transactionDataFetcher;

    @Test
    void shouldGetTotalTransactionAmount(){
        Mockito.when(transactionRepository.findAll()).thenReturn(getTransactions());
        Assertions.assertEquals(33.0, transactionDataFetcher.getTotalTransactionAmount());
    }

    @Test
    void shouldGetTotalTransactionAmountSentBySender(){
        Mockito.when(transactionRepository.findAll()).thenReturn(getTransactions());
        Assertions.assertEquals(33.0, transactionDataFetcher.getTotalTransactionAmountSentBy("raza"));
    }

    @Test
    void shouldGetMaxTransactionAmount(){
        Mockito.when(transactionRepository.findAll()).thenReturn(getTransactions());
        Assertions.assertEquals(20.5, transactionDataFetcher.getMaxTransactionAmount());
    }

    @Test
    void shouldCountUniqueClients(){
        Mockito.when(transactionRepository.findAll()).thenReturn(getTransactions());
        Assertions.assertEquals(2, transactionDataFetcher.countUniqueClients());
    }

    @Test
    void shouldCheckComplianceIssueOpened(){
        Mockito.when(transactionRepository.findAll()).thenReturn(getTransactions());
        Assertions.assertTrue(transactionDataFetcher.hasOpenComplianceIssues("raza"));
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
        Sender sender = new Sender();
        sender.setFullName("raza");
        sender.setAge(25);
        transaction.setSender(sender);
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setFullName("haider");
        beneficiary.setAge(23);
        transaction.setBeneficiary(beneficiary);
        Issue issue = new Issue();
        issue.setId(1L);
        issue.setSolved(Boolean.TRUE);
        issue.setMessage("Not good");
        transaction.setIssue(issue);
        transactions.add(transaction);

        // new transaction
        transaction = new Transaction();
        transaction.setTransactionId(2L);
        transaction.setAmount(20.5);
        sender = new Sender();
        sender.setFullName("raza");
        sender.setAge(25);
        transaction.setSender(sender);
        beneficiary = new Beneficiary();
        beneficiary.setFullName("haider");
        beneficiary.setAge(23);
        transaction.setBeneficiary(beneficiary);
        issue = new Issue();
        issue.setId(2L);
        issue.setSolved(Boolean.FALSE);
        issue.setMessage("Not good");
        transaction.setIssue(issue);
        transactions.add(transaction);
        return transactions;
    }
}
