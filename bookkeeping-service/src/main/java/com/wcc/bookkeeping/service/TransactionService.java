package com.wcc.bookkeeping.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wcc.bookkeeping.dto.Paged;
import com.wcc.bookkeeping.dto.TransactionResponse;
import com.wcc.bookkeeping.repository.TransactionRepository;
import com.wcc.bookkeeping.util.Mapper;

@Service
@Transactional
class TransactionService implements ITransactionService {

	private final TransactionRepository repository;

	public TransactionService(final TransactionRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional(readOnly = true)
	public Paged<TransactionResponse> getTransactions(final Pageable pageable) {
		return new Paged<>(repository.findAll(pageable).map(Mapper::toResponse));
	}

	@Override
	@Transactional(readOnly = true)
	public Paged<TransactionResponse> getTransactionsBy(final String accountId, final Pageable pageable) {
		return new Paged<>(repository.findAllByAccountId(accountId, pageable).map(Mapper::toResponse));
	}

}
