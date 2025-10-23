package com.example.inventory.features.inventory.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventory")
public class InventoryItem {

  @Id
  @Column(name = "product_id", nullable = false, length = 100)
  private String productId;

  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  public String getProductId() { return productId; }
  public void setProductId(String productId) { this.productId = productId; }
  public Integer getQuantity() { return quantity; }
  public void setQuantity(Integer quantity) { this.quantity = quantity; }
}


