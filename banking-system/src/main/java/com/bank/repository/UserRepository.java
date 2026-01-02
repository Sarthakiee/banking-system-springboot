package com.bank.repository;

import com.bank.model.User;
import com.bank.util.FileConstants;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UserRepository {

    private Map<String, User> users = new HashMap<>();

    public UserRepository() {
        load();
    }

    public void save(User user) {
        users.put(user.getAccountNumber(), user);
        persist();
    }

    public User findByAccountNumber(String accountNumber) {
        return users.get(accountNumber);
    }

    public Collection<User> findAll() {
        return users.values();
    }

    private void persist() {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FileConstants.USER_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save users", e);
        }
    }

    @SuppressWarnings("unchecked")
    private void load() {
        File file = new File(FileConstants.USER_FILE);
        if (!file.exists()) return;

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(file))) {
            users = (Map<String, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load users", e);
        }
    }
}
