package com.site21.bittermelon.economy;


import java.util.Date;
import java.util.UUID;

public class AccountRegistry {
    public void makeTransfer(int fromAccountID, int toAccountID, float amount, Date timestamp, String description) {
        UUID id = UUID.randomUUID();
        Transaction transaction = new Transaction(id, fromAccountID, toAccountID, amount, timestamp, description);
    }


}
