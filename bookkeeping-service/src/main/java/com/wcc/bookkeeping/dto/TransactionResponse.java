package com.wcc.bookkeeping.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionResponse(String id, String accountId, BigDecimal amount, String groupId, Instant createdAt) {

}
