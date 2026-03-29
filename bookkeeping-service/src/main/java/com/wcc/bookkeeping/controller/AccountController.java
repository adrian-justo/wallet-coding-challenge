package com.wcc.bookkeeping.controller;

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
import com.wcc.bookkeeping.dto.AccountResponse;
import com.wcc.bookkeeping.dto.CreateAccountRequest;
import com.wcc.bookkeeping.dto.Paged;
import com.wcc.bookkeeping.service.IAccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Account API", description = "Endpoints for account creation, and viewing of balance and transactions.")
@RestController
@RequestMapping("/api/{version}/accounts")
public class AccountController {

	private final IAccountService service;

	public AccountController(final IAccountService service) {
		this.service = service;
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

}
