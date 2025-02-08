package com.site21.bittermelon.economy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Account {
    private final String name;
    private final int id;
    private float balance = 0;
    private final List<Transaction> transactionHistory;
    private final List<String> requiredPrivileges;
    private static final Random random = new Random();

    public Account(String name, int id) {
        this.name = name;
        this.id = id;
        transactionHistory = new ArrayList<>();
        requiredPrivileges = new ArrayList<>();
    }

    public Account(String name) {
        this(name, random.nextInt((int) Math.pow(10, 6)));
    }
}
