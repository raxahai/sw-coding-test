package com.smallworld;

import com.smallworld.data.Transaction;
import com.smallworld.repository.TransactionRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TransactionDataFetcher {

    private final TransactionRepository transactionRepository;

    public TransactionDataFetcher(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Returns the sum of the amounts of all transactions
     */
    public double getTotalTransactionAmount() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream().mapToDouble(Transaction::getAmount).sum();
    }

    /**
     * Returns the sum of the amounts of all transactions sent by the specified client
     */
    public double getTotalTransactionAmountSentBy(String senderFullName) {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .filter(transaction -> transaction.getSender().getFullName().equals(senderFullName))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
     * Returns the highest transaction amount
     */
    public double getMaxTransactionAmount() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .max(Comparator.comparingDouble(Transaction::getAmount))
                .get().getAmount();
    }

    /**
     * Counts the number of unique clients that sent or received a transaction
     */
    public long countUniqueClients() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .flatMap(transaction -> Stream.of(transaction.getSender().getFullName(), transaction.getBeneficiary().getFullName()))
                .distinct().count();
    }

    /**
     * Returns whether a client (sender or beneficiary) has at least one transaction with a compliance
     * issue that has not been solved
     */
    public boolean hasOpenComplianceIssues(String clientFullName) {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .anyMatch(transaction -> (transaction.getSender().getFullName().equals(clientFullName) ||
                        transaction.getBeneficiary().getFullName().equals(clientFullName)) &&
                        transaction.getIssue().getSolved().equals(Boolean.FALSE));
    }

    /**
     * Returns all transactions indexed by beneficiary name
     */
    public Map<String, List<Transaction>> getTransactionsByBeneficiaryName() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .collect(Collectors.groupingBy(transaction -> transaction.getBeneficiary().getFullName()));
    }

    /**
     * Returns the identifiers of all open compliance issues
     */
    public Set<Long> getUnsolvedIssueIds() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .filter(transaction -> transaction.getIssue().getSolved().equals(Boolean.FALSE))
                .map(transaction -> transaction.getIssue().getId())
                .collect(Collectors.toSet());
    }

    /**
     * Returns a list of all solved issue messages
     */
    public List<String> getAllSolvedIssueMessages() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .filter(transaction -> transaction.getIssue().getSolved().equals(Boolean.TRUE))
                .map(transaction -> transaction.getIssue().getMessage())
                .collect(Collectors.toList());
    }

    /**
     * Returns the 3 transactions with the highest amount sorted by amount descending
     */
    public List<Transaction> getTop3TransactionsByAmount() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .sorted(Comparator.comparing(Transaction::getAmount).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Returns the senderFullName of the sender with the most total sent amount
     */
    public Optional<String> getTopSender() {
        List<Transaction> transactions = transactionRepository.findAll();
        Map<String, Double> senderWithTotalAmountMap =
                transactions.stream()
                        .collect(Collectors.groupingBy(
                                transaction -> transaction.getSender().getFullName(),
                                Collectors.summingDouble(Transaction::getAmount)));

        return transactions.isEmpty() ? Optional.empty() :
                Optional.ofNullable(Collections.max(senderWithTotalAmountMap.entrySet(), Map.Entry.comparingByValue()).getKey());
    }

}
