package com.site21.bittermelon.common.systems.economy.bank;

import com.site21.bittermelon.common.systems.personnel.registry.PersonnelEntry;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class AccountRegistry extends SavedData {
    private static AccountRegistry clientInstance;
    private final Map<Integer, Account> accounts = new HashMap<>();
    private static final String DATA_NAME = "account_registry";

    public static AccountRegistry get(@NotNull Level level) {
        if (level.isClientSide()) {
            return getClient();
        } else {
            ServerLevel overworld = level.getServer().getLevel(Level.OVERWORLD);
            return overworld.getDataStorage().computeIfAbsent(
                    new SavedData.Factory<>(
                            AccountRegistry::new,
                            AccountRegistry::load,
                            DataFixTypes.LEVEL
                    ),
                    DATA_NAME
            );
        }
    }

    public static @NotNull AccountRegistry get(@NotNull MinecraftServer server) {
        return server.getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(
                        AccountRegistry::new,
                        AccountRegistry::load,
                        DataFixTypes.LEVEL
                ),
                DATA_NAME
        );
    }

    @OnlyIn(Dist.CLIENT)
    private static AccountRegistry getClient() {
        if (clientInstance == null) {
            clientInstance = new AccountRegistry();
        }
        return clientInstance;
    }

    @OnlyIn(Dist.CLIENT)
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

    @OnlyIn(Dist.CLIENT)
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

    public static @NotNull AccountRegistry load(@NotNull CompoundTag tag, HolderLookup.Provider lookupProvider) {
        AccountRegistry registry = new AccountRegistry();
        ListTag accountList = tag.getList("accounts", ListTag.TAG_COMPOUND);

        accountList.forEach(accountTag -> {
            Account.CODEC.parse(NbtOps.INSTANCE, accountTag)
                    .result()
                    .ifPresent(account -> registry.accounts.put(account.getId(), account));
        });

        return registry;
    }


    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        ListTag accountList = new ListTag();

        accounts.values().forEach(account -> {
            Account.CODEC.encodeStart(NbtOps.INSTANCE, account)
                    .result()
                    .ifPresent(accountList::add);
        });

        tag.put("accounts", accountList);
        return tag;
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
}
