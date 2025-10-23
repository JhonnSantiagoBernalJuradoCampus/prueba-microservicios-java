package com.example.inventory.features.inventory.api.dto;

public class InventoryDto {
  private String productId;
  private Integer quantity;

  public String getProductId() { return productId; }
  public void setProductId(String productId) { this.productId = productId; }
  public Integer getQuantity() { return quantity; }
  public void setQuantity(Integer quantity) { this.quantity = quantity; }
}


