package com.example.products.infra.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

  @Bean
  public RestClient inventoryRestClient(
      @Value("${inventory.base-url:http://localhost:8082}") String baseUrl,
      @Value("${inventory.api-key.header:X-API-Key}") String apiKeyHeader,
      @Value("${inventory.api-key.value:dev-secret}") String apiKeyValue
  ) {
    return RestClient.builder()
      .baseUrl(baseUrl)
      .requestInterceptor((request, body, execution) -> {
        request.getHeaders().add(apiKeyHeader, apiKeyValue);
        return execution.execute(request, body);
      })
      .build();
  }
}
