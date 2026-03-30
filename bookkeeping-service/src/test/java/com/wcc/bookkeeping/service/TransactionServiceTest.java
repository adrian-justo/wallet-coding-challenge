package com.wcc.bookkeeping.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.wcc.bookkeeping.dto.TransactionResponse;
import com.wcc.bookkeeping.model.Transaction;
import com.wcc.bookkeeping.repository.TransactionRepository;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

	private Transaction transaction;

	private TransactionResponse response;

	@Mock
	private TransactionRepository repository;

	@InjectMocks
	private TransactionService service;

	@BeforeEach
	void setUp() {
		transaction = new Transaction("accountId", BigDecimal.TEN, "groupId", Instant.now());
		response = new TransactionResponse(transaction.getId(), transaction.getAccountId(), transaction.getAmount(),
				transaction.getGroupId(), transaction.getCreatedAt());
	}

	@Test
	void shouldReturnPaginatedTransactionsWhenRequested() {
		final var transactionsPage = new PageImpl<>(List.of(transaction));
		when(repository.findAll(any(Pageable.class))).thenReturn(transactionsPage);
		assertNotNull(service.getTransactions(PageRequest.ofSize(10)));
	}

	@Test
	void shouldReturnPaginatedTransactionsOfAccountWhenRequested() {
		final var transactionsPage = new PageImpl<>(List.of(transaction));
		when(repository.findAllByAccountId(anyString(), any(Pageable.class))).thenReturn(transactionsPage);
		assertNotNull(service.getTransactionsBy(transaction.getAccountId(), PageRequest.ofSize(10)));
	}

}
