package com.example.products.features.product.application;

import com.example.products.features.inventoryclient.InventoryClient;
import com.example.products.features.product.api.dto.ProductDto;
import com.example.products.features.product.api.dto.PurchaseRequest;
import com.example.products.features.product.api.dto.PurchaseResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PurchaseServiceTest {

  private ProductService productService;
  private InventoryClient inventoryClient;
  private PurchaseService purchaseService;

  @BeforeEach
  void setup() {
    productService = Mockito.mock(ProductService.class);
    inventoryClient = Mockito.mock(InventoryClient.class);
    purchaseService = new PurchaseService(productService, inventoryClient);
  }

  @Test
  void purchase_ok_decrements_and_returns_total() {
    PurchaseRequest.Attributes attrs = new PurchaseRequest.Attributes();
    attrs.setProductId("p_1");
    attrs.setQuantity(3);

    ProductDto dto = new ProductDto();
    dto.setId("p_1");
    dto.setName("X");
    dto.setPrice(BigDecimal.valueOf(10.0));
    when(productService.getById("p_1")).thenReturn(dto);

    PurchaseResponse resp = purchaseService.purchase(attrs);

    verify(inventoryClient).decrement("p_1", 3);
    assertThat(resp.getProductId()).isEqualTo("p_1");
    assertThat(resp.getQuantity()).isEqualTo(3);
    assertThat(resp.getTotalPrice()).isEqualTo(30.0);
  }
}
