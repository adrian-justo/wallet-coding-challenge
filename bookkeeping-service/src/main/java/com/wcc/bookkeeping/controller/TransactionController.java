package com.wcc.bookkeeping.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wcc.bookkeeping.constant.AppConstants;
import com.wcc.bookkeeping.dto.Paged;
import com.wcc.bookkeeping.dto.TransactionResponse;
import com.wcc.bookkeeping.service.ITransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Transaction API", description = "Endpoints for viewing and creating transactions")
@RestController
@RequestMapping("/api/{version}/transactions")
public class TransactionController {

	private final ITransactionService service;

	public TransactionController(final ITransactionService service) {
		this.service = service;
	}

	@Operation(summary = "Transactions Audit", description = "View all transactions")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Transactions" + AppConstants.MSG_OK) })
	@GetMapping(version = "1")
	public Paged<TransactionResponse> getAllTransactions(
			@ParameterObject @PageableDefault(page = 0, size = 10) final Pageable pageable) {
		return service.getTransactions(pageable);
	}

}
