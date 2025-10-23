package com.example.products.features.product.application;

import com.example.products.features.product.api.dto.CreateProductRequest;
import com.example.products.features.product.api.dto.ProductDto;
import com.example.products.features.product.api.mapper.ProductMapper;
import com.example.products.features.product.domain.Product;
import com.example.products.features.product.domain.ProductRepository;
import com.example.products.infra.jsonapi.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

  @Mock
  private ProductRepository productRepository;
  @Mock
  private ProductMapper productMapper;

  @InjectMocks
  private ProductService productService;

  private CreateProductRequest.Attributes attrs;

  @BeforeEach
  void setUp() {
    attrs = new CreateProductRequest.Attributes();
    attrs.setName("Laptop");
    attrs.setPrice(new BigDecimal("1299.99"));
    attrs.setDescription("Ultrabook");
  }

  @Test
  void create_shouldPersistAndReturnDto() {
    Product entity = new Product();
    entity.setName(attrs.getName());
    entity.setPrice(attrs.getPrice());
    entity.setDescription(attrs.getDescription());

    Product saved = new Product();
    saved.setId("p_1");
    saved.setName(attrs.getName());
    saved.setPrice(attrs.getPrice());
    saved.setDescription(attrs.getDescription());

    ProductDto dto = new ProductDto();
    dto.setId("p_1");
    dto.setName("Laptop");
    dto.setPrice(new BigDecimal("1299.99"));
    dto.setDescription("Ultrabook");

    when(productMapper.fromCreateAttributes(attrs)).thenReturn(entity);
    when(productRepository.save(entity)).thenReturn(saved);
    when(productMapper.toDto(saved)).thenReturn(dto);

    ProductDto result = productService.create(attrs);
    assertThat(result.getId()).isEqualTo("p_1");
    assertThat(result.getName()).isEqualTo("Laptop");
  }

  @Test
  void getById_shouldReturnDto() {
    Product found = new Product();
    found.setId("p_1");
    found.setName("Laptop");
    found.setPrice(new BigDecimal("1299.99"));

    ProductDto dto = new ProductDto();
    dto.setId("p_1");
    dto.setName("Laptop");
    dto.setPrice(new BigDecimal("1299.99"));

    when(productRepository.findById("p_1")).thenReturn(Optional.of(found));
    when(productMapper.toDto(found)).thenReturn(dto);

    ProductDto result = productService.getById("p_1");
    assertThat(result.getId()).isEqualTo("p_1");
  }

  @Test
  void getById_whenNotFound_shouldThrow() {
    when(productRepository.findById("missing")).thenReturn(Optional.empty());
    assertThatThrownBy(() -> productService.getById("missing"))
      .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void listAll_shouldMapAll() {
    Product e1 = new Product(); e1.setId("p1");
    Product e2 = new Product(); e2.setId("p2");

    ProductDto d1 = new ProductDto(); d1.setId("p1");
    ProductDto d2 = new ProductDto(); d2.setId("p2");

    when(productRepository.findAll()).thenReturn(List.of(e1, e2));
    when(productMapper.toDto(e1)).thenReturn(d1);
    when(productMapper.toDto(e2)).thenReturn(d2);

    List<ProductDto> list = productService.listAll();
    assertThat(list).extracting(ProductDto::getId).containsExactly("p1", "p2");
  }
}


