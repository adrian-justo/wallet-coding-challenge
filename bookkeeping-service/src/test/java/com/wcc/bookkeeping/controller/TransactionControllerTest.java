package com.wcc.bookkeeping.controller;

import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.client.ApiVersionInserter;

import com.wcc.bookkeeping.dto.Paged;
import com.wcc.bookkeeping.dto.TransactionResponse;
import com.wcc.bookkeeping.service.ITransactionService;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

	private TransactionResponse transaction;

	private static final String TRANSACTIONS_PATH = "/transactions";

	private RestTestClient client;

	@Autowired
	private MockMvc mvc;

	@MockitoBean
	private ITransactionService service;

	@BeforeEach
	void setUp() {
		transaction = new TransactionResponse("id", "accountId", BigDecimal.TEN, "groupId", Instant.now());
		client = RestTestClient.bindTo(mvc)
			.baseUrl("/api")
			.apiVersionInserter(ApiVersionInserter.usePathSegment(1))
			.defaultApiVersion("v1")
			.build();
	}

	@Test
	void shouldReturnPaginatedTransactionsWhenCalled() {
		final var pagedTransactions = new Paged<>(new PageImpl<>(List.of(transaction)));
		when(service.getTransactions(any(Pageable.class))).thenReturn(pagedTransactions);
		client.get()
			.uri(TRANSACTIONS_PATH)
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody(new ParameterizedTypeReference<Paged<TransactionResponse>>() {
			})
			.isEqualTo(pagedTransactions);
	}

}
