package com.example.products.features.product.api;

import com.example.products.features.product.api.dto.CreateProductRequest;
import com.example.products.features.product.api.dto.ProductDto;
import com.example.products.features.product.application.ProductService;
import com.example.products.infra.jsonapi.JsonApiData;
import com.example.products.infra.jsonapi.JsonApiRequest;
import com.example.products.infra.jsonapi.JsonApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductsController {

  private final ProductService productService;

  public ProductsController(ProductService productService) {
    this.productService = productService;
  }

  @PostMapping
  public ResponseEntity<JsonApiResponse<ProductDto>> create(@Valid @RequestBody JsonApiRequest<CreateProductRequest.Attributes> body) {
    ProductDto created = productService.create(body.getData().getAttributes());
    JsonApiData<ProductDto> data = new JsonApiData<>("products", created.getId(), created);
    return ResponseEntity.created(URI.create("/products/" + created.getId())).body(new JsonApiResponse<>(data));
  }

  @GetMapping("/{id}")
  public ResponseEntity<JsonApiResponse<ProductDto>> getById(@PathVariable String id) {
    ProductDto product = productService.getById(id);
    return ResponseEntity.ok(new JsonApiResponse<>(new JsonApiData<>("products", product.getId(), product)));
  }

  @GetMapping
  public ResponseEntity<?> list() {
    List<ProductDto> products = productService.listAll();
    List<JsonApiData<ProductDto>> data = products.stream()
      .map(p -> new JsonApiData<>("products", p.getId(), p))
      .toList();
    return ResponseEntity.ok(new Object() { public List<JsonApiData<ProductDto>> getData(){ return data; } });
  }
}


