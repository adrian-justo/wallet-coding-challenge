package com.wcc.bookkeeping.service;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wcc.bookkeeping.dto.BalanceTransferRequest;
import com.wcc.bookkeeping.dto.Paged;
import com.wcc.bookkeeping.dto.TransactionResponse;
import com.wcc.bookkeeping.exception.InsufficientFundsException;
import com.wcc.bookkeeping.model.Account;
import com.wcc.bookkeeping.repository.AccountRepository;
import com.wcc.bookkeeping.repository.TransactionRepository;
import com.wcc.bookkeeping.util.Mapper;

@Service
@Transactional
class TransactionService implements ITransactionService {

	private final TransactionRepository repository;

	private final AccountRepository acctRepository;

	public TransactionService(final TransactionRepository repository, final AccountRepository acctRepository) {
		this.repository = repository;
		this.acctRepository = acctRepository;
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

	@Override
	public List<TransactionResponse> transferBalance(final BalanceTransferRequest request) {
		// Lock accounts consistently, this prevents deadlocks
		final var accountToLock = request.sourceAccount().compareTo(request.destinationAccount()) < 0
				? request.sourceAccount() : request.destinationAccount();
		final var first = lock(accountToLock);
		final var second = lock(
				accountToLock.equals(request.sourceAccount()) ? request.destinationAccount() : request.sourceAccount());

		// Check balance
		final var source = request.sourceAccount().equals(first.getId()) ? first : second;
		if (source.getBalance().compareTo(request.amount()) < 0)
			throw new InsufficientFundsException();

		// Update balances
		source.setBalance(source.getBalance().subtract(request.amount()));
		final var destination = request.destinationAccount().equals(first.getId()) ? first : second;
		destination.setBalance(destination.getBalance().add(request.amount()));

		acctRepository.saveAll(List.of(source, destination));

		final var groupId = "trx-" + request.sourceAccount() + '+' + request.destinationAccount() + '-'
				+ Instant.now().toEpochMilli();
		return repository.saveAll(Mapper.toEntities(request, groupId)).stream().map(Mapper::toResponse).toList();
	}

	private Account lock(final String accountId) {
		return acctRepository.findByIdForUpdate(accountId).orElseThrow();
	}

}
