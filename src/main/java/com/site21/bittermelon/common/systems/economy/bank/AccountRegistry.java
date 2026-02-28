package com.site21.bittermelon.common.systems.economy.bank;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.common.systems.personnel.registry.PersonnelEntry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class AccountRegistry extends SavedData {
    public static final SavedDataType<AccountRegistry> TYPE;

    private static AccountRegistry clientInstance;
    private final Map<Integer, Account> accounts = new HashMap<>();

    public static AccountRegistry get(@NotNull Level level) {
        if (level.isClientSide()) {
            return getClient();
        } else {
            ServerLevel overworld = level.getServer().getLevel(Level.OVERWORLD);
            return overworld.getDataStorage().computeIfAbsent(TYPE);
        }
    }

    
    private static AccountRegistry getClient() {
        if (clientInstance == null) {
            clientInstance = new AccountRegistry();
        }
        return clientInstance;
    }

    
    public static void clearClientData() {
        if (clientInstance != null) {
            clientInstance.accounts.clear();
        }
    }

    public Account getAccount(Integer id) {
        return accounts.get(id);
    }

    public void addAccount(Account account) {
        accounts.put(account.getId(), account);
        setDirty();
    }

    public void removeAccount(int id) {
        accounts.remove(id);
        setDirty();
    }

    public void updateAccount(int id, Consumer<Account> updater) {
        Account account = accounts.get(id);
        if (account != null) {
            updater.accept(account);
            setDirty();
        }
    }

    
    public void updateCharacterFromServer(Account account) {
        accounts.put(account.getId(), account);
    }

    public void modifyBalance(int id, float amount) {
        updateAccount(id, account -> account.modifyBalance(amount));
    }

    public void addTransaction(int id, Transaction transaction) {
        updateAccount(id, account -> account.addTransaction(transaction));
    }

    public void makeTransaction(int fromAccountID, int toAccountID, float amount, Date timestamp, String description) {
        UUID id = UUID.randomUUID();
        Transaction transaction = new Transaction(id, fromAccountID, toAccountID, amount, timestamp, description);

        modifyBalance(fromAccountID, -amount);
        modifyBalance(toAccountID, amount);
        addTransaction(fromAccountID, transaction);
        addTransaction(toAccountID, transaction);
    }

    public Collection<Account> getAccounts() {
        return new ArrayList<>(accounts.values());
    }

    public List<Account> getSortedAccounts() {
        return accounts.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public List<Account> getPermittedAccounts(@NotNull PersonnelEntry entry) {
//        List<String> privileges = entry.getPrivileges();
        List<Account> permittedAccounts = new ArrayList<>();

//        for (Account account : accounts.values()) {
//            if (account.canAccess(privileges)) {
//                permittedAccounts.add(account);
//            }
//        }
//
//        permittedAccounts.sort(Comparator.comparing(Account::getId));

        return permittedAccounts;
    }

    public boolean doesAccountExist(int id) {
        return accounts.containsKey(id);
    }

    static {
        TYPE = new SavedDataType<>(
                "accounts",
                AccountRegistry::new,
                RecordCodecBuilder.create(instance -> instance.group(
                        Account.CODEC.listOf().fieldOf("accounts")
                                .forGetter(cm -> new ArrayList<>(cm.accounts.values()))
                ).apply(instance, (List<Account> accounts) -> {
                            AccountRegistry cm = new AccountRegistry();
                            for (Account account : accounts) {
                                cm.accounts.put(account.getId(), account);
                            }
                            return cm;
                        }
                ))
        );
    }
}
