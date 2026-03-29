package com.wcc.bookkeeping.util;

import com.wcc.bookkeeping.dto.AccountResponse;
import com.wcc.bookkeeping.dto.CreateAccountRequest;
import com.wcc.bookkeeping.model.Account;

public final class Mapper {

	private Mapper() {
		// Prevent instantiation
	}

	public static Account toEntity(final CreateAccountRequest request) {
		return new Account(request.id());
	}

	public static AccountResponse toResponse(final Account entity) {
		return new AccountResponse(entity.getId(), entity.getCreatedAt());
	}

}
