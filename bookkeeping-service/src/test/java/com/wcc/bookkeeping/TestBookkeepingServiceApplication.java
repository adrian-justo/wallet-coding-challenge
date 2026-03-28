package com.wcc.bookkeeping;

import org.springframework.boot.SpringApplication;

public class TestBookkeepingServiceApplication {

	public static void main(final String[] args) {
		SpringApplication.from(BookkeepingServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
