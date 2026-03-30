package com.wcc.bookkeeping.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.client.ApiVersionInserter;

import com.wcc.bookkeeping.dto.AccountResponse;
import com.wcc.bookkeeping.dto.CreateAccountRequest;
import com.wcc.bookkeeping.dto.Paged;
import com.wcc.bookkeeping.dto.TransactionResponse;
import com.wcc.bookkeeping.service.IAccountService;
import com.wcc.bookkeeping.service.ITransactionService;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

	private AccountResponse account;

	private static final String ACCOUNTS_PATH = "/accounts";

	private RestTestClient client;

	@Autowired
	private MockMvc mvc;

	@MockitoBean
	private IAccountService service;

	@MockitoBean
	private ITransactionService trxService;

	@BeforeEach
	void setUp() {
		account = new AccountResponse("id", BigDecimal.TEN, Instant.now());
		client = RestTestClient.bindTo(mvc)
			.baseUrl("/api")
			.apiVersionInserter(ApiVersionInserter.usePathSegment(1))
			.defaultApiVersion("v1")
			.build();
	}

	@Test
	void shouldReturnAccountWhenCreated() {
		when(service.createAccount(any(CreateAccountRequest.class))).thenReturn(account);
		client.post()
			.uri(ACCOUNTS_PATH)
			.body(new CreateAccountRequest(account.id()))
			.exchange()
			.expectStatus()
			.isCreated()
			.expectBody(AccountResponse.class)
			.isEqualTo(account);
	}

	@Test
	void shouldReturnPaginatedAccountsWhenCalled() {
		final var pagedAccounts = new Paged<>(new PageImpl<>(List.of(account)));
		when(service.getAccounts(any(Pageable.class))).thenReturn(pagedAccounts);
		client.get()
			.uri(ACCOUNTS_PATH)
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody(new ParameterizedTypeReference<Paged<AccountResponse>>() {
			})
			.isEqualTo(pagedAccounts);
	}

	@Test
	void shouldReturnBalanceWhenAccountExists() {
		when(service.findBalanceBy(anyString())).thenReturn(account.balance());
		client.get()
			.uri(ACCOUNTS_PATH + '/' + account.id() + "/balance")
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody(BigDecimal.class)
			.isEqualTo(account.balance());
	}

	@Test
	void shouldReturnTransactionsWhenAccountExists() {
		final var pagedTransactions = new Paged<>(new PageImpl<>(
				List.of(new TransactionResponse("tx1", account.id(), BigDecimal.ONE, "grp1", Instant.now()))));
		when(service.existsAccount(anyString())).thenReturn(true);
		when(trxService.getTransactionsBy(anyString(), any(Pageable.class))).thenReturn(pagedTransactions);
		client.get()
			.uri(ACCOUNTS_PATH + '/' + account.id() + "/transactions")
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody(new ParameterizedTypeReference<Paged<TransactionResponse>>() {
			})
			.isEqualTo(pagedTransactions);
	}

	@Test
	void shouldThrowExceptionWhenAccountNotExists() {
		when(service.existsAccount(anyString())).thenReturn(false);
		client.get()
			.uri(ACCOUNTS_PATH + '/' + account.id() + "/transactions")
			.exchange()
			.expectStatus()
			.isNotFound()
			.expectBody(ProblemDetail.class);
	}

}
