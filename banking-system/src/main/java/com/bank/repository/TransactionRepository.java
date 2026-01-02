package com.bank.repository;

import com.bank.model.Transaction;
import com.bank.util.FileConstants;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionRepository {

    private List<Transaction> transactions = new ArrayList<>();

    public TransactionRepository() {
        load();
    }

    public void save(Transaction transaction) {
        transactions.add(transaction);
        persist();
    }

    public List<Transaction> findByAccountNumber(String accountNumber) {
        return transactions.stream()
                .filter(t -> t.getAccountNumber().equals(accountNumber))
                .collect(Collectors.toList());
    }

    private void persist() {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FileConstants.TRANSACTION_FILE))) {
            oos.writeObject(transactions);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save transactions", e);
        }
    }

    @SuppressWarnings("unchecked")
    private void load() {
        File file = new File(FileConstants.TRANSACTION_FILE);
        if (!file.exists()) return;

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(file))) {
            transactions = (List<Transaction>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load transactions", e);
        }
    }
}
