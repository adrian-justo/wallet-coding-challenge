package com.wcc.bookkeeping.constant;

public final class AppConstants {

	private AppConstants() {
		// Prevent instantiation
	}

	public static final String MSG_FIELD_BLANK = "This field is required";

	public static final String MSG_NUMBER_INVALID = "Value must be greater than 0";

	public static final String MSG_OK = " found and returned";

	public static final String MSG_CREATED = " created and returned";

	public static final String MSG_BAD_REQUEST = "Details provided is invalid";

	public static final String MSG_NOT_FOUND = " is not found";

	public static final String MSG_CONFLICT = "Cannot transfer to same account";

	public static final String MSG_UNPROCESSABLE_CONTENT = "Insufficient funds on source account";

	public static final String MSG_VALUE_GT_EQ = "Value must be greater than or equal to ";

	public static final int AMOUNT_PRECISION = 19;

	public static final int AMOUNT_SCALE = 2;

	public static final String AMOUNT_DEFAULT = "0.01";

}
