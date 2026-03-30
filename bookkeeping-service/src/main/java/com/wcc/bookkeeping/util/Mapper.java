package com.wcc.bookkeeping.util;

import java.math.BigDecimal;
import java.util.List;

import com.wcc.bookkeeping.dto.AccountResponse;
import com.wcc.bookkeeping.dto.BalanceTransferRequest;
import com.wcc.bookkeeping.dto.CreateAccountRequest;
import com.wcc.bookkeeping.dto.TransactionResponse;
import com.wcc.bookkeeping.model.Account;
import com.wcc.bookkeeping.model.Transaction;

public final class Mapper {

	private Mapper() {
		// Prevent instantiation
	}

	public static Account toEntity(final CreateAccountRequest request) {
		return new Account(request.id(), BigDecimal.ZERO);
	}

	public static AccountResponse toResponse(final Account entity) {
		return new AccountResponse(entity.getId(), entity.getBalance(), entity.getCreatedAt());
	}

	public static AccountResponse toAuditResponse(final Account entity) {
		return new AccountResponse(entity.getId(), entity.getCreatedAt());
	}

	public static List<Transaction> toEntities(final BalanceTransferRequest request, final String groupId) {
		return List.of(new Transaction(request.sourceAccount(), request.amount().negate(), groupId),
				new Transaction(request.destinationAccount(), request.amount(), groupId));
	}

	public static TransactionResponse toResponse(final Transaction entity) {
		return new TransactionResponse(entity.getId(), entity.getAccountId(), entity.getAmount(), entity.getGroupId(),
				entity.getCreatedAt());
	}

}
