package com.wcc.bookkeeping.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.wcc.bookkeeping.dto.AccountResponse;
import com.wcc.bookkeeping.dto.CreateAccountRequest;
import com.wcc.bookkeeping.dto.Paged;
import com.wcc.bookkeeping.model.Account;
import com.wcc.bookkeeping.repository.AccountRepository;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

	private Account account;

	private AccountResponse response;

	@Mock
	private AccountRepository repository;

	@InjectMocks
	private AccountService service;

	@BeforeEach
	void setUp() {
		account = new Account("id", Instant.now());
		response = new AccountResponse(account.getId(), account.getCreatedAt());
	}

	@Test
	void shouldReturnNewAccountWhenCreateSuccessful() {
		when(repository.findById(anyString())).thenReturn(Optional.empty());
		when(repository.save(any(Account.class))).thenReturn(account);
		assertEquals(response, service.createAccount(new CreateAccountRequest(account.getId())));
	}

	@Test
	void shouldReturnSameAccountWhenAlreadyCreated() {
		when(repository.findById(anyString())).thenReturn(Optional.of(account));
		assertEquals(response, service.createAccount(new CreateAccountRequest(account.getId())));
	}

	@Test
	void shouldReturnPaginatedAccountsWhenRequested() {
		final var accountPage = new PageImpl<>(List.of(account));
		when(repository.findAll(any(Pageable.class))).thenReturn(accountPage);
		assertEquals(new Paged<>(new PageImpl<>(List.of(response))), service.getAccounts(PageRequest.ofSize(10)));
	}

}
