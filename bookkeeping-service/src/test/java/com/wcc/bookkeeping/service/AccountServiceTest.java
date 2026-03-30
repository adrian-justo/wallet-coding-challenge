package com.wcc.bookkeeping.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.wcc.bookkeeping.dto.AccountResponse;
import com.wcc.bookkeeping.dto.CreateAccountRequest;
import com.wcc.bookkeeping.exception.ResourceNotFoundException;
import com.wcc.bookkeeping.model.Account;
import com.wcc.bookkeeping.repository.AccountRepository;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

	private Account account;

	private AccountResponse response;

	@Mock
	private AccountRepository repository;

	@Mock
	private AccountCreator creator;

	@InjectMocks
	private AccountService service;

	@BeforeEach
	void setUp() {
		account = new Account("id", BigDecimal.TEN, Instant.now());
		response = new AccountResponse(account.getId(), account.getBalance(), account.getCreatedAt());
	}

	@Test
	void shouldReturnNewAccountWhenCreateSuccessful() {
		when(creator.save(any(CreateAccountRequest.class))).thenReturn(account);
		assertEquals(response, service.createAccount(new CreateAccountRequest(account.getId())));
	}

	@Test
	void shouldReturnSameAccountWhenAlreadyCreated() {
		when(creator.save(any(CreateAccountRequest.class))).thenThrow(DataIntegrityViolationException.class);
		when(repository.findByIdForUpdate(anyString())).thenReturn(Optional.of(account));
		assertEquals(response, service.createAccount(new CreateAccountRequest(account.getId())));
	}

	@Test
	void shouldReturnPaginatedAccountsWhenRequested() {
		final var accountsPage = new PageImpl<>(List.of(account));
		when(repository.findAll(any(Pageable.class))).thenReturn(accountsPage);
		assertNotNull(service.getAccounts(PageRequest.ofSize(10)));
	}

	@Test
	void shouldReturnBalanceWhenFound() {
		when(repository.findByIdForUpdate(anyString())).thenReturn(Optional.of(account));
		assertEquals(account.getBalance(), service.findBalanceBy(account.getId()));
	}

	@Test
	void shouldThrowExceptionDuringReturnBalanceWhenNotFound() {
		when(repository.findByIdForUpdate(anyString())).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> service.findBalanceBy("nonExistent"));
	}

	@Test
	void shouldReturnTrueWhenAccountExists() {
		when(repository.existsById(anyString())).thenReturn(true);
		assertEquals(true, service.existsAccount(account.getId()));
	}

	@Test
	void shouldSaveWhenAccountsNotExists() {
		service.createAccountsIfNotExists(account.getId(), account.getId());
		verify(creator, times(2)).save(any(CreateAccountRequest.class));
	}

}
