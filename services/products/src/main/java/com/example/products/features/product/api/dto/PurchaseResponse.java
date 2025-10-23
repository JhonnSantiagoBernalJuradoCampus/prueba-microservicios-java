package com.example.products.features.product.api.dto;

public class PurchaseResponse {
  private String productId;
  private int quantity;
  private double totalPrice;

  public String getProductId() { return productId; }
  public void setProductId(String productId) { this.productId = productId; }
  public int getQuantity() { return quantity; }
  public void setQuantity(int quantity) { this.quantity = quantity; }
  public double getTotalPrice() { return totalPrice; }
  public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
}
