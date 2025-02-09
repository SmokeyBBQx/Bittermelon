package com.site21.bittermelon.economy;


import com.site21.bittermelon.database.PersonnelEntry;
import com.site21.bittermelon.util.DataManager;
import net.neoforged.fml.loading.FMLPaths;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class AccountRegistry extends DataManager<Integer, Account> {

    protected AccountRegistry() {
        super(FMLPaths.GAMEDIR.get().resolve("bank_account/").toString(), Account.class);
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

    @Override
    protected String getFileName(@NotNull Account data) {
        return data.getName();
    }

    @Override
    protected Integer getKey(@NotNull Account data) {
        return data.getId();
    }
}
