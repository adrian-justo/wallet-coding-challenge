package com.wcc.bookkeeping.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.wcc.bookkeeping.constant.AppConstants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Transaction {

	@Id
	private final String id;

	@Column(nullable = false)
	private String accountId;

	@Column(nullable = false, precision = AppConstants.AMOUNT_PRECISION, scale = AppConstants.AMOUNT_SCALE)
	private BigDecimal amount;

	@Column(nullable = false)
	private String groupId;

	@CreationTimestamp
	private Instant createdAt;

	public Transaction() {
		id = UUID.randomUUID().toString();
	}

	public Transaction(final String accountId, final BigDecimal amount, final String groupId) {
		this();
		this.accountId = accountId;
		this.amount = amount;
		this.groupId = groupId;

	}

	public Transaction(final String accountId, final BigDecimal amount, final String groupId, final Instant createdAt) {
		this(accountId, amount, groupId);
		this.createdAt = createdAt;
	}

	public String getId() {
		return id;
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

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(final String groupId) {
		this.groupId = groupId;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(final Instant createdAt) {
		this.createdAt = createdAt;
	}

}
