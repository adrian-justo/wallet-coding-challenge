package com.wcc.bookkeeping.controller;

import java.math.BigDecimal;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.wcc.bookkeeping.constant.AppConstants;
import com.wcc.bookkeeping.dto.AccountResponse;
import com.wcc.bookkeeping.dto.CreateAccountRequest;
import com.wcc.bookkeeping.dto.Paged;
import com.wcc.bookkeeping.dto.TransactionResponse;
import com.wcc.bookkeeping.exception.ResourceNotFoundException;
import com.wcc.bookkeeping.service.IAccountService;
import com.wcc.bookkeeping.service.ITransactionService;
import com.wcc.bookkeeping.util.PathValidator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Account API", description = "Endpoints for account creation, and viewing of balance and transactions")
@RestController
@RequestMapping("/api/{version}/accounts")
public class AccountController {

	private final IAccountService service;

	private final ITransactionService trxService;

	public AccountController(final IAccountService service, final ITransactionService trxService) {
		this.service = service;
		this.trxService = trxService;
	}

	@Operation(summary = "Create Account", description = "Create an account")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Account" + AppConstants.MSG_CREATED),
			@ApiResponse(responseCode = "400", description = AppConstants.MSG_BAD_REQUEST, content = @Content) })
	@PostMapping(version = "1")
	@ResponseStatus(HttpStatus.CREATED)
	public AccountResponse createAnAccount(@RequestBody @Valid final CreateAccountRequest request) {
		return service.createAccount(request);
	}

	@Operation(summary = "Accounts Audit", description = "View all accounts")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Accounts" + AppConstants.MSG_OK) })
	@GetMapping(version = "1")
	public Paged<AccountResponse> getAllAccounts(
			@ParameterObject @PageableDefault(page = 0, size = 10) final Pageable pageable) {
		return service.getAccounts(pageable);
	}

	@Operation(summary = "Account Balance", description = "View balance of an account")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Balance" + AppConstants.MSG_OK),
			@ApiResponse(responseCode = "400", description = AppConstants.MSG_BAD_REQUEST, content = @Content),
			@ApiResponse(responseCode = "404", description = "Account" + AppConstants.MSG_NOT_FOUND,
					content = @Content) })
	@GetMapping(value = "/{accountId}/balance", version = "1")
	public BigDecimal getBalanceById(@PathVariable final String accountId) {
		PathValidator.accountId(accountId);
		return service.findBalanceBy(accountId);
	}

	@Operation(summary = "Account Transactions", description = "View transactions of an account")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Transactions" + AppConstants.MSG_OK),
			@ApiResponse(responseCode = "400", description = AppConstants.MSG_BAD_REQUEST, content = @Content),
			@ApiResponse(responseCode = "404", description = "Account" + AppConstants.MSG_NOT_FOUND,
					content = @Content) })
	@GetMapping(value = "/{accountId}/transactions", version = "1")
	public Paged<TransactionResponse> getTransactionsById(@PathVariable final String accountId,
			@ParameterObject @PageableDefault(page = 0, size = 10, sort = "createdAt",
					direction = Sort.Direction.DESC) final Pageable pageable) {
		PathValidator.accountId(accountId);
		if (!service.existsAccount(accountId))
			throw new ResourceNotFoundException("Account");
		return trxService.getTransactionsBy(accountId, pageable);
	}

}
