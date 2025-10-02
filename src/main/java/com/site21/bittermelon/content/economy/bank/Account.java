package com.site21.bittermelon.content.economy.bank;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Account {
    public static final Codec<Account> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(Account::getName),
            Codec.INT.fieldOf("id").forGetter(Account::getId),
            Codec.FLOAT.fieldOf("balance").forGetter(Account::getBalance),
            ExtraCodecs.nonEmptyList(Codec.STRING.listOf()).optionalFieldOf("allowed_privileges", new ArrayList<>()).forGetter(Account::getAllowedPrivileges)
    ).apply(instance, Account::new));

    private final String name;
    private final int id;
    private float balance = 0;
    private final List<Transaction> transactionHistory; // TODO: Add transaction history to account registry instead of saving them in accounts
    private final List<String> allowedPrivileges; // TODO: Different permissions? How should it even work for people outside the foundation
    private static final Random random = new Random();

    public Account(String name, int id, float balance, List<String> allowedPrivileges) {
        this.name = name;
        this.id = id;
        this.balance = balance;
        this.allowedPrivileges = allowedPrivileges;
        this.transactionHistory = new ArrayList<>();
    }

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
        return allowedPrivileges.stream().anyMatch(privileges::contains) || allowedPrivileges.isEmpty();
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
