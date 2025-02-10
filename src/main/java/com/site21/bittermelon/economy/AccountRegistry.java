package com.site21.bittermelon.economy;


import com.site21.bittermelon.database.PersonnelEntry;
import com.site21.bittermelon.util.DataManager;
import net.neoforged.fml.loading.FMLPaths;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class AccountRegistry extends DataManager<Integer, Account> {
    private static AccountRegistry instance = null;

    protected AccountRegistry() {
        super(FMLPaths.GAMEDIR.get().resolve("bank_account/").toString(), Account.class);
        Account account = new Account("test");
        account.setBalance(100);
        addData(account.getId(), account);
    }

    public static synchronized AccountRegistry getInstance() {
        if (instance == null) {
            instance = new AccountRegistry();
        }
        return instance;
    }

    public void makeTransfer(int fromAccountID, int toAccountID, float amount, Date timestamp, String description) {
        UUID id = UUID.randomUUID();
        Transaction transaction = new Transaction(id, fromAccountID, toAccountID, amount, timestamp, description);
        Account sender = dataMap.get(fromAccountID);
        Account receiver = dataMap.get(toAccountID);

        sender.modifyBalance(-amount);
        receiver.modifyBalance(amount);
        sender.addTransaction(transaction);
        receiver.addTransaction(transaction);
    }

    public boolean makeSafeTransfer(List<String> privileges, int fromAccountID, int toAccountID, float amount, Date timestamp, String description) {
        Account sender = dataMap.get(fromAccountID);
        if (sender.canAccess(privileges)) {
            makeTransfer(fromAccountID, toAccountID, amount, timestamp, description);
        }
        return false;
    }

    public List<Account> getPermittedAccounts(@NotNull PersonnelEntry entry) {
        List<String> privileges = entry.getPrivileges();
        List<Account> permittedAccounts = new ArrayList<>();

        for (Account account : dataMap.values()) {
            if (account.canAccess(privileges)) {
                permittedAccounts.add(account);
            }
        }

        permittedAccounts.sort(Comparator.comparing(Account::getId));

        return permittedAccounts;
    }

    public void makeAccount(String name) {
        Account account = new Account(name);
        addData(account.getId(), account);
    }

    public boolean doesAccountExist(int accountNumber) {
        return dataMap.containsKey(accountNumber);
    }

    public Collection<Account> getAccounts() {
        return dataMap.values();
    }

    public List<Account> getSortedAccounts() {
        return dataMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    @Override
    protected String getFileName(@NotNull Account data) {
        return String.valueOf(data.getId());
    }

    @Override
    protected Integer getKey(@NotNull Account data) {
        return data.getId();
    }
}
