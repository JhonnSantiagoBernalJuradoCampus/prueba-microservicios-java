package com.example.products.features.product.application;

import com.example.products.features.product.api.dto.CreateProductRequest;
import com.example.products.features.product.api.dto.ProductDto;
import com.example.products.features.product.api.mapper.ProductMapper;
import com.example.products.features.product.domain.Product;
import com.example.products.features.product.domain.ProductRepository;
import com.example.products.infra.jsonapi.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

  private final ProductRepository productRepository;
  private final ProductMapper productMapper;

  public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
    this.productRepository = productRepository;
    this.productMapper = productMapper;
  }

  @Transactional
  public ProductDto create(CreateProductRequest.Attributes attributes) {
    Product toCreate = productMapper.fromCreateAttributes(attributes);
    Product created = productRepository.save(toCreate);
    return productMapper.toDto(created);
  }

  @Transactional(readOnly = true)
  public ProductDto getById(String id) {
    Product product = productRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Product " + id + " not found"));
    return productMapper.toDto(product);
  }

  @Transactional(readOnly = true)
  public List<ProductDto> listAll() {
    return productRepository.findAll().stream().map(productMapper::toDto).toList();
  }
}


