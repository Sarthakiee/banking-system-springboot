package com.bank.service;

import org.springframework.stereotype.Service;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.InsufficientBalanceException;
import com.bank.exception.InvalidPinException;
import com.bank.model.Transaction;
import com.bank.model.User;
import com.bank.repository.TransactionRepository;
import com.bank.repository.UserRepository;
import com.bank.util.AccountNumberGenerator;
import com.bank.util.TransactionIdGenerator;

import java.util.List;
@Service
public class BankServiceImpl implements BankService {

    private final UserRepository userRepository = new UserRepository();
    private final TransactionRepository transactionRepository = new TransactionRepository();

    @Override
    public User registerUser(String name, String pin) {
        String accountNumber = AccountNumberGenerator.generate();
        User user = new User(accountNumber, name, pin, 0.0);
        userRepository.save(user);
        return user;
    }

    @Override
    public User login(String accountNumber, String pin) {
        User user = userRepository.findByAccountNumber(accountNumber);
        if (user == null) {
            throw new AccountNotFoundException("Account not found");
        }
        if (!user.getPin().equals(pin)) {
            throw new InvalidPinException("Invalid PIN");
        }
        return user;
    }

    @Override
    public double checkBalance(User user) {
        return user.getBalance();
    }

    @Override
    public void deposit(User user, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        user.setBalance(user.getBalance() + amount);

        transactionRepository.save(
                new Transaction(
                        TransactionIdGenerator.generate(),
                        user.getAccountNumber(),
                        "DEPOSIT",
                        amount
                )
        );
    }

    @Override
    public void withdraw(User user, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (user.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        user.setBalance(user.getBalance() - amount);

        transactionRepository.save(
                new Transaction(
                        TransactionIdGenerator.generate(),
                        user.getAccountNumber(),
                        "WITHDRAW",
                        amount
                )
        );
    }

    @Override
    public void transfer(User fromUser, String toAccountNumber, double amount) {
        User toUser = userRepository.findByAccountNumber(toAccountNumber);
        if (toUser == null) {
            throw new AccountNotFoundException("Target account not found");
        }

        withdraw(fromUser, amount);
        deposit(toUser, amount);

        transactionRepository.save(
                new Transaction(
                        TransactionIdGenerator.generate(),
                        fromUser.getAccountNumber(),
                        "TRANSFER",
                        amount
                )
        );
    }

    @Override
    public List<Transaction> getTransactionHistory(String accountNumber) {
        return transactionRepository.findByAccountNumber(accountNumber);
    }
}
