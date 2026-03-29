package com.wcc.bookkeeping.exception;

public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 7302863915107596158L;

	private final String resource;

	public ResourceNotFoundException(final String resource) {
		this.resource = resource;
	}

	public String getResource() {
		return resource;
	}

}