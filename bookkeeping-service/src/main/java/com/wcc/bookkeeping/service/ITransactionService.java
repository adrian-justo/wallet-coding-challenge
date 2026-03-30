package com.wcc.bookkeeping.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.wcc.bookkeeping.dto.BalanceTransferRequest;
import com.wcc.bookkeeping.dto.Paged;
import com.wcc.bookkeeping.dto.TransactionResponse;

public interface ITransactionService {

	Paged<TransactionResponse> getTransactions(Pageable pageable);

	Paged<TransactionResponse> getTransactionsBy(String accountId, Pageable pageable);

	List<TransactionResponse> transferBalance(BalanceTransferRequest request);

}
