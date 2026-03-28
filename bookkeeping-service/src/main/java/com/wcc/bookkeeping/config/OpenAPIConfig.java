package com.wcc.bookkeeping.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfig {

	@Bean
	OpenAPI openAPI(@Value("${spring.application.name}") final String title,
			@Value("${spring.application.version}") final String version,
			@Value("${application.description}") final String description,
			@Value("${server.url}") final String serverUrl) {
		return new OpenAPI().info(new Info().title(title).version(version).description(description))
			.servers(List.of(new Server().url(serverUrl)));
	}

}
