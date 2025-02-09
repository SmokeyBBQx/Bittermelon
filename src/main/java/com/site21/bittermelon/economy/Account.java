package com.site21.bittermelon.economy;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Account {
    private final String name;
    private final int id;
    private float balance = 0;
    private final List<Transaction> transactionHistory; // TODO: Add transaction history to account registry instead of saving them in accounts
    private final List<String> allowedPrivileges; // TODO: Different permissions? How should it even work for people outside the foundation
    private static final Random random = new Random();

    public Account(String name, int id) {
        this.name = name;
        this.id = id;
        transactionHistory = new ArrayList<>();
        allowedPrivileges = new ArrayList<>();
    }

    public Account(String name) {
        this(name, random.nextInt((int) Math.pow(10, 6)));
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public float getBalance() {
        return balance;
    }

    public List<String> getAllowedPrivileges() {
        return allowedPrivileges;
    }

    public boolean canAccess(@NotNull List<String> privileges) {
        return allowedPrivileges.stream().anyMatch(privileges::contains);
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public void modifyBalance(float balance) {
        this.balance += balance;
    }

    public void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }
}
