package com.wcc.bookkeeping.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.wcc.bookkeeping.dto.BalanceTransferRequest;
import com.wcc.bookkeeping.exception.InsufficientFundsException;
import com.wcc.bookkeeping.model.Account;
import com.wcc.bookkeeping.model.Transaction;
import com.wcc.bookkeeping.repository.AccountRepository;
import com.wcc.bookkeeping.repository.TransactionRepository;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

	private Account source;

	private Account destination;

	private List<Transaction> transactions;

	@Mock
	private TransactionRepository repository;

	@Mock
	private AccountRepository acctRepository;

	@InjectMocks
	private TransactionService service;

	@BeforeEach
	void setUp() {
		source = new Account("sourceAccount", BigDecimal.TEN, Instant.now());
		destination = new Account("destinationAccount", BigDecimal.ZERO, Instant.now());
		transactions = List.of(new Transaction(source.getId(), source.getBalance().negate(), "groupId", Instant.now()),
				new Transaction(destination.getId(), source.getBalance(), "groupId", Instant.now()));
	}

	@Test
	void shouldReturnPaginatedTransactionsWhenRequested() {
		final var transactionsPage = new PageImpl<>(transactions);
		when(repository.findAll(any(Pageable.class))).thenReturn(transactionsPage);
		assertNotNull(service.getTransactions(PageRequest.ofSize(10)));
	}

	@Test
	void shouldReturnPaginatedTransactionsOfAccountWhenRequested() {
		final var transactionsPage = new PageImpl<>(transactions);
		when(repository.findAllByAccountId(anyString(), any(Pageable.class))).thenReturn(transactionsPage);
		assertNotNull(service.getTransactionsBy(transactions.getFirst().getAccountId(), PageRequest.ofSize(10)));
	}

	@Test
	void shouldReturnTransactionsWhenTransferValid() {
		when(acctRepository.findByIdForUpdate(anyString())).thenReturn(Optional.of(source), Optional.of(destination));
		when(acctRepository.saveAll(ArgumentMatchers.<List<Account>>any())).thenReturn(List.of(source, destination));
		when(repository.saveAll(ArgumentMatchers.<List<Transaction>>any())).thenReturn(transactions);

		final var responseList = service
			.transferBalance(new BalanceTransferRequest(source.getId(), destination.getId(), BigDecimal.TEN));
		assertEquals(transactions.getFirst().getAmount(), responseList.getFirst().amount());
	}

	@Test
	void shouldThrowExceptionWhenInsufficientFunds() {
		when(acctRepository.findByIdForUpdate(anyString())).thenReturn(Optional.of(destination), Optional.of(source));
		assertThrows(InsufficientFundsException.class, () -> service
			.transferBalance(new BalanceTransferRequest(destination.getId(), source.getId(), BigDecimal.TEN)));
	}

}
