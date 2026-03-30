package com.wcc.bookkeeping.controller;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.wcc.bookkeeping.constant.AppConstants;
import com.wcc.bookkeeping.dto.BalanceTransferRequest;
import com.wcc.bookkeeping.dto.Paged;
import com.wcc.bookkeeping.dto.TransactionResponse;
import com.wcc.bookkeeping.service.IAccountService;
import com.wcc.bookkeeping.service.ITransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Transaction API", description = "Endpoints for viewing and creating transactions")
@RestController
@RequestMapping("/api/{version}/transactions")
public class TransactionController {

	private final ITransactionService service;

	private final IAccountService acctService;

	public TransactionController(final ITransactionService service, final IAccountService acctService) {
		this.service = service;
		this.acctService = acctService;
	}

	@Operation(summary = "Transactions Audit", description = "View all transactions")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Transactions" + AppConstants.MSG_OK) })
	@GetMapping(version = "1")
	public Paged<TransactionResponse> getAllTransactions(
			@ParameterObject @PageableDefault(page = 0, size = 10) final Pageable pageable) {
		return service.getTransactions(pageable);
	}

	@Operation(summary = "Balance Transfer", description = "Transfer balance to another account")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Account" + AppConstants.MSG_CREATED),
			@ApiResponse(responseCode = "400", description = AppConstants.MSG_BAD_REQUEST, content = @Content),
			@ApiResponse(responseCode = "409", description = AppConstants.MSG_CONFLICT, content = @Content),
			@ApiResponse(responseCode = "422", description = AppConstants.MSG_UNPROCESSABLE_CONTENT,
					content = @Content) })
	@PostMapping(value = "/transfer", version = "1")
	@ResponseStatus(HttpStatus.CREATED)
	public List<TransactionResponse> transferBalanceToAccount(
			@RequestBody @Valid final BalanceTransferRequest request) {
		request.validate();
		acctService.createAccountsIfNotExists(request.sourceAccount(), request.destinationAccount());
		return service.transferBalance(request);
	}

}
