package com.example.products.features.inventoryclient;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
public class InventoryClient {

  private final RestClient inventoryRestClient;

  public InventoryClient(RestClient inventoryRestClient) {
    this.inventoryRestClient = inventoryRestClient;
  }

  public void decrement(String productId, int amount) {
    try {
      inventoryRestClient.patch()
        .uri("/inventory/{productId}/decrement?amount={amount}", productId, amount)
        .retrieve()
        .toBodilessEntity();
    } catch (HttpClientErrorException ex) {
      // Dejar que el GlobalExceptionHandler traduzca a JSON:API preservando status y detalle
      throw ex;
    }
  }
}
