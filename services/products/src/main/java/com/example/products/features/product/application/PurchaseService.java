package com.example.products.features.product.application;

import com.example.products.features.inventoryclient.InventoryClient;
import com.example.products.features.product.api.dto.PurchaseRequest;
import com.example.products.features.product.api.dto.PurchaseResponse;
import com.example.products.features.product.api.dto.ProductDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class PurchaseService {

  private final ProductService productService;
  private final InventoryClient inventoryClient;

  public PurchaseService(ProductService productService, InventoryClient inventoryClient) {
    this.productService = productService;
    this.inventoryClient = inventoryClient;
  }

  @Transactional
  public PurchaseResponse purchase(PurchaseRequest.Attributes attributes) {
    ProductDto product = productService.getById(attributes.getProductId());
    inventoryClient.decrement(attributes.getProductId(), attributes.getQuantity());
    PurchaseResponse resp = new PurchaseResponse();
    resp.setProductId(product.getId());
    resp.setQuantity(attributes.getQuantity());
    BigDecimal total = product.getPrice().multiply(BigDecimal.valueOf(attributes.getQuantity()));
    resp.setTotalPrice(total.doubleValue());
    return resp;
  }
}
