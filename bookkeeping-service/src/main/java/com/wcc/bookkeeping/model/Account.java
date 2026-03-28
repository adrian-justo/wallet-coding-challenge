package com.wcc.bookkeeping.model;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Account {

	@Id
	private String id;

	@CreationTimestamp
	private Instant createdAt;

	public Account() {
	}

	public Account(final String id, final Instant createdAt) {
		this.id = id;
		this.createdAt = createdAt;
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(final Instant createdAt) {
		this.createdAt = createdAt;
	}

}
