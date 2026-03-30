package com.wcc.bookkeeping.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.wcc.bookkeeping.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

	Page<Transaction> findAllByAccountId(String accountId, Pageable pageable);

}
