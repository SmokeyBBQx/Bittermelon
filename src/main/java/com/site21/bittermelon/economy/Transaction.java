package com.site21.bittermelon.economy;

import java.util.Date;
import java.util.UUID;

public record Transaction(UUID id, int fromAccountID, int toAccountID, float amount, Date timestamp, String description) {
}
