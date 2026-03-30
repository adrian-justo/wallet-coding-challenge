package com.wcc.bookkeeping.service;

import java.math.BigDecimal;

import org.springframework.data.domain.Pageable;

import com.wcc.bookkeeping.dto.AccountResponse;
import com.wcc.bookkeeping.dto.CreateAccountRequest;
import com.wcc.bookkeeping.dto.Paged;

public interface IAccountService {

	AccountResponse createAccount(CreateAccountRequest request);

	Paged<AccountResponse> getAccounts(Pageable pageable);

	BigDecimal findBalanceBy(String id);

	boolean existsAccount(String id);

	void createAccountsIfNotExists(String sourceAccount, String destinationAccount);

}
