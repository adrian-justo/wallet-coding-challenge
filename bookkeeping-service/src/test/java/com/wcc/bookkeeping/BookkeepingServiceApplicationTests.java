package com.wcc.bookkeeping;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.client.ApiVersionInserter;
import org.testcontainers.mysql.MySQLContainer;

import com.wcc.bookkeeping.dto.AccountResponse;
import com.wcc.bookkeeping.dto.BalanceTransferRequest;
import com.wcc.bookkeeping.dto.CreateAccountRequest;
import com.wcc.bookkeeping.dto.Paged;
import com.wcc.bookkeeping.dto.TransactionResponse;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class BookkeepingServiceApplicationTests {

	private static final String ACCOUNTS_PATH = "/accounts";

	private static final String TRANSACTIONS_PATH = "/transactions";

	private static final String ACCOUNT_ID_PATH = "/Joemar";

	private RestTestClient client;

	@LocalServerPort
	private int port;

	@Autowired
	private MySQLContainer db;

	@BeforeEach
	void setup() {
		client = RestTestClient.bindToServer()
			.baseUrl("http://localhost:%d/api".formatted(port))
			.apiVersionInserter(ApiVersionInserter.usePathSegment(1))
			.defaultApiVersion("v1")
			.build();
	}

	@Test
	void connectionEstablished() {
		assertTrue(db.isRunning());
	}

	@Test
	void shouldCreateAccountSuccessfully() {
		final var id = "new";
		client.post()
			.uri(ACCOUNTS_PATH)
			.body(new CreateAccountRequest(id))
			.exchange()
			.expectStatus()
			.isCreated()
			.expectBody(AccountResponse.class)
			.consumeWith(result -> assertEquals(id, result.getResponseBody().id()));
	}

	@Test
	void shouldReturnPaginatedAccounts() {
		client.get()
			.uri(ACCOUNTS_PATH)
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody(new ParameterizedTypeReference<Paged<AccountResponse>>() {
			})
			.consumeWith(result -> assertNotNull(result.getResponseBody()));
	}

	@Test
	void shouldReturnBalance() {
		client.get()
			.uri(ACCOUNTS_PATH + ACCOUNT_ID_PATH + "/balance")
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody(BigDecimal.class);
	}

	@Test
	void shouldReturnPaginatedTransactionsByAccount() {
		client.get()
			.uri(ACCOUNTS_PATH + ACCOUNT_ID_PATH + TRANSACTIONS_PATH)
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody(new ParameterizedTypeReference<Paged<TransactionResponse>>() {
			})
			.consumeWith(result -> assertNotNull(result.getResponseBody()));
	}

	@Test
	void shouldReturnPaginatedTransactions() {
		client.get()
			.uri(TRANSACTIONS_PATH)
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody(new ParameterizedTypeReference<Paged<TransactionResponse>>() {
			})
			.consumeWith(result -> assertNotNull(result.getResponseBody()));
	}

	@Test
	void shouldReturnTransferTransactions() {
		client.post()
			.uri(TRANSACTIONS_PATH + "/transfer")
			.body(new BalanceTransferRequest("Joemar", "new", BigDecimal.TEN))
			.exchange()
			.expectStatus()
			.isCreated()
			.expectBody(new ParameterizedTypeReference<List<TransactionResponse>>() {
			})
			.consumeWith(result -> assertNotNull(result.getResponseBody()));
	}

	@Test
	void shouldReturnCorrectBalanceOnConcurrentTransfers() throws Exception {
		final var source = "Adrian";
		final var sourceBalStart = client.get()
			.uri(ACCOUNTS_PATH + '/' + source + "/balance")
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody(BigDecimal.class)
			.returnResult()
			.getResponseBody();
		final var destination = "destinationAccount";

		final var calls = 5;
		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
			final var latch = new CountDownLatch(calls);
			final Callable<Integer> task = () -> {
				try {
					return client.post()
						.uri(TRANSACTIONS_PATH + "/transfer")
						.body(new BalanceTransferRequest(source, destination, BigDecimal.TEN))
						.exchange()
						.returnResult()
						.getStatus()
						.value();
				}
				finally {
					latch.countDown();
				}
			};

			IntStream.range(0, calls).forEach(_ -> executor.submit(task));
			latch.await();
		}

		client.get()
			.uri(ACCOUNTS_PATH + '/' + source + "/balance")
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody(BigDecimal.class)
			.isEqualTo(sourceBalStart.subtract(BigDecimal.TEN.multiply(BigDecimal.valueOf(calls))));
		client.get()
			.uri(ACCOUNTS_PATH + '/' + destination + "/balance")
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody(BigDecimal.class)
			.isEqualTo(BigDecimal.TEN.multiply(BigDecimal.valueOf(calls)).setScale(2));
	}

}
