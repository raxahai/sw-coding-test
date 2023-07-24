package com.smallworld.repository.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smallworld.data.Beneficiary;
import com.smallworld.data.Issue;
import com.smallworld.data.Sender;
import com.smallworld.data.Transaction;
import com.smallworld.exception.ServiceException;
import com.smallworld.repository.TransactionRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private static List<Transaction> transactions;

    @Value("${datasource.json.file-location}")
    private String jsonFile;

    private final ObjectMapper objectMapper;

    @Autowired
    public TransactionRepositoryImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        findAll(jsonFile);
    }

    @Override
    public List<Transaction> findAll(){
        return transactions;
    }

    private void findAll(String file){
        transactions = new ArrayList<>();
        try {
            JsonNode rootNode = objectMapper.readTree(new File(file));
            rootNode.forEach(transaction -> {
                Transaction t = new Transaction();
                t.setTransactionId(transaction.path("mtn").asLong());
                t.setAmount(transaction.path("amount").asDouble());
                Sender sender = new Sender();
                sender.setFullName(transaction.path("senderFullName").asText());
                sender.setAge(transaction.path("senderAge").asInt());
                t.setSender(sender);
                Beneficiary beneficiary = new Beneficiary();
                beneficiary.setFullName(transaction.path("beneficiaryFullName").asText());
                beneficiary.setAge(transaction.path("beneficiaryAge").asInt());
                t.setBeneficiary(beneficiary);
                Issue issue = new Issue();
                issue.setId(transaction.path("issueId").asLong());
                issue.setSolved(transaction.path("issueSolved").asBoolean());
                issue.setMessage(transaction.path("issueMessage").asText());
                t.setIssue(issue);
                transactions.add(t);
            });
        } catch (Exception e) {
            throw new ServiceException("Invalid Transactions");
        }
    }
}
