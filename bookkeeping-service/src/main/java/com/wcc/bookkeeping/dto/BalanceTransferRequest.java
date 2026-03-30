package com.wcc.bookkeeping.dto;

import java.math.BigDecimal;

import com.wcc.bookkeeping.constant.AppConstants;
import com.wcc.bookkeeping.exception.SameAccountTransferException;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;

public record BalanceTransferRequest(@NotBlank(message = AppConstants.MSG_FIELD_BLANK) String sourceAccount,
		@NotBlank(message = AppConstants.MSG_FIELD_BLANK) String destinationAccount,
		@Digits(integer = AppConstants.AMOUNT_PRECISION, fraction = AppConstants.AMOUNT_SCALE) @DecimalMin(
				value = AppConstants.AMOUNT_DEFAULT,
				message = AppConstants.MSG_VALUE_GT_EQ + AppConstants.AMOUNT_DEFAULT) BigDecimal amount) {

	public void validate() {
		if (sourceAccount.equals(destinationAccount))
			throw new SameAccountTransferException();
	}

}
