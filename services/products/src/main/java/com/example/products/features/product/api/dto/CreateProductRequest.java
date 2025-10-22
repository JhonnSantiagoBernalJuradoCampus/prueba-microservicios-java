package com.example.products.features.product.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CreateProductRequest {

  public static class Attributes {
    @NotBlank
    private String name;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;

    private String description;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
  }

  private String type;
  private Attributes attributes;

  public String getType() { return type; }
  public void setType(String type) { this.type = type; }
  public Attributes getAttributes() { return attributes; }
  public void setAttributes(Attributes attributes) { this.attributes = attributes; }
}


