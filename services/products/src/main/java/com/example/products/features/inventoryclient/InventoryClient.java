package com.example.products.features.inventoryclient;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class InventoryClient {
  private final RestClient restClient;

  public InventoryClient(RestClient inventoryRestClient) {
    this.restClient = inventoryRestClient;
  }

  public void decrement(String productId, int amount) {
    restClient.patch()
      .uri("/inventory/{productId}/decrement?amount={amount}", productId, amount)
      .accept(MediaType.APPLICATION_JSON)
      .retrieve()
      .toBodilessEntity();
  }
}
