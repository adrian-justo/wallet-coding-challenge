package com.wcc.bookkeeping.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public record AccountResponse(String id, BigDecimal balance, Instant createdAt) {

	public AccountResponse(final String id, final Instant createdAt) {
		this(id, null, createdAt);
	}

}
