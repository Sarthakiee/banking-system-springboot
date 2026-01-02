package com.bank.util;

import java.util.UUID;

public class TransactionIdGenerator {

    private TransactionIdGenerator() {}

    public static String generate() {
        return "TXN" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
    }
}
