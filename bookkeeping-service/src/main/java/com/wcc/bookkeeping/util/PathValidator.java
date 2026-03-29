package com.wcc.bookkeeping.util;

import org.apache.commons.lang3.StringUtils;

import com.wcc.bookkeeping.exception.ResourceNotFoundException;

public final class PathValidator {

	private PathValidator() {
		// Prevent instantiation
	}

	public static void accountId(final String s) {
		validate(s, "Account");
	}

	public static void transactionId(final String s) {
		validate(s, "Transaction");
	}

	private static void validate(final String s, final String resource) {
		if (StringUtils.isBlank(s))
			throw new ResourceNotFoundException(resource);
	}

}
