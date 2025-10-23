package com.example.products.features.product.api.mapper;

import com.example.products.features.product.api.dto.CreateProductRequest;
import com.example.products.features.product.api.dto.ProductDto;
import com.example.products.features.product.domain.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

  ProductDto toDto(Product product);

  @Mapping(target = "id", ignore = true)
  Product fromCreateAttributes(CreateProductRequest.Attributes attrs);
}
