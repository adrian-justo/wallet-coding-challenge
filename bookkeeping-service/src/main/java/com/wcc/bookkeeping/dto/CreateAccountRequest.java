package com.wcc.bookkeeping.dto;

import com.wcc.bookkeeping.constant.AppConstants;

import jakarta.validation.constraints.NotBlank;

public record CreateAccountRequest(@NotBlank(message = AppConstants.MSG_FIELD_BLANK) String id) {

}
