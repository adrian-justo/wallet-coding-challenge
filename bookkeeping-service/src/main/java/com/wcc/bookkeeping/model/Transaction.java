package com.wcc.bookkeeping.model;

import java.math.BigDecimal;
import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Transaction {

	@Id
	private String id;

	@Column(nullable = false)
	private String accountId;

	@Column(nullable = false)
	private BigDecimal amount;

	@CreationTimestamp
	private Instant createdAt;

	public Transaction() {
	}

	public Transaction(final String id, final String accountId, final BigDecimal amount, final Instant createdAt) {
		this.id = id;
		this.accountId = accountId;
		this.amount = amount;
		this.createdAt = createdAt;
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(final String accountId) {
		this.accountId = accountId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(final BigDecimal amount) {
		this.amount = amount;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(final Instant createdAt) {
		this.createdAt = createdAt;
	}

}
