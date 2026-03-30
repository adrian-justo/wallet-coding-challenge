package com.wcc.bookkeeping.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wcc.bookkeeping.dto.CreateAccountRequest;
import com.wcc.bookkeeping.model.Account;
import com.wcc.bookkeeping.repository.AccountRepository;
import com.wcc.bookkeeping.util.Mapper;

@Component
class AccountCreator {

	private final AccountRepository repository;

	public AccountCreator(final AccountRepository repository) {
		this.repository = repository;
	}

	// absorbs rollback-only marking by Spring’s transaction interceptor
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Account save(final CreateAccountRequest request) {
		return repository.saveAndFlush(Mapper.toEntity(request));
	}

}
