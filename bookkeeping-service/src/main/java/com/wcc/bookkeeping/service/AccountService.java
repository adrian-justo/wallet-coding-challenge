package com.wcc.bookkeeping.service;

import java.math.BigDecimal;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wcc.bookkeeping.dto.AccountResponse;
import com.wcc.bookkeeping.dto.CreateAccountRequest;
import com.wcc.bookkeeping.dto.Paged;
import com.wcc.bookkeeping.exception.ResourceNotFoundException;
import com.wcc.bookkeeping.repository.AccountRepository;
import com.wcc.bookkeeping.util.Mapper;

@Service
@Transactional
class AccountService implements IAccountService {

	private final AccountRepository repository;

	public AccountService(final AccountRepository repository) {
		this.repository = repository;
	}

	@Override
	public AccountResponse createAccount(final CreateAccountRequest request) {
		final var account = repository.findById(request.id())
			.orElseGet(() -> repository.save(Mapper.toEntity(request)));
		return Mapper.toResponse(account);
	}

	@Override
	@Transactional(readOnly = true)
	public Paged<AccountResponse> getAccounts(final Pageable pageable) {
		return new Paged<>(repository.findAll(pageable).map(Mapper::toAuditResponse));
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal findBalanceBy(final String id) {
		return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account")).getBalance();
	}

	@Override
	@Transactional(readOnly = true)
	public boolean existsAccount(final String id) {
		return repository.existsById(id);
	}

}
