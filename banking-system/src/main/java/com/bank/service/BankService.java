package com.bank.service;

import java.util.List;

import com.bank.model.Transaction;
import com.bank.model.User;



public interface BankService {

    User registerUser(String name, String pin);

    User login(String accountNumber, String pin);

    double checkBalance(User user);

    void deposit(User user, double amount);

    void withdraw(User user, double amount);

    void transfer(User fromUser, String toAccountNumber, double amount);
    
    List<Transaction> getTransactionHistory(String accountNumber);
}
