package com.wcc.bookkeeping.model;

import java.math.BigDecimal;
import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Account {

	@Id
	private String id;

	@Column(nullable = false)
	private BigDecimal balance;

	@CreationTimestamp
	private Instant createdAt;

	public Account() {
	}

	public Account(final String id, final BigDecimal balance) {
		this.id = id;
		this.balance = balance;
	}

	public Account(final String id, final BigDecimal balance, final Instant createdAt) {
		this(id, balance);
		this.createdAt = createdAt;
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(final BigDecimal balance) {
		this.balance = balance;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(final Instant createdAt) {
		this.createdAt = createdAt;
	}

}
