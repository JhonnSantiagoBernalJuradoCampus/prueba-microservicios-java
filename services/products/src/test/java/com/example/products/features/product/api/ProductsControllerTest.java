package com.example.products.features.product.api;

import com.example.products.features.product.api.dto.CreateProductRequest;
import com.example.products.features.product.api.dto.ProductDto;
import com.example.products.features.product.application.ProductService;
import com.example.products.infra.jsonapi.GlobalExceptionHandler;
import com.example.products.infra.jsonapi.JsonApiData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductsControllerTest {

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;
  private ProductService productService;

  @BeforeEach
  void setup() {
    productService = Mockito.mock(ProductService.class);
    ProductsController controller = new ProductsController(productService);
    mockMvc = MockMvcBuilders
      .standaloneSetup(controller)
      .setControllerAdvice(new GlobalExceptionHandler())
      .build();
    objectMapper = new ObjectMapper();
  }

  @Test
  void post_create_product_returns_jsonapi_created() throws Exception {
    CreateProductRequest.Attributes attrs = new CreateProductRequest.Attributes();
    attrs.setName("Laptop");
    attrs.setPrice(new BigDecimal("1299.99"));

    ProductDto dto = new ProductDto();
    dto.setId("p_1");
    dto.setName("Laptop");
    dto.setPrice(new BigDecimal("1299.99"));

    Mockito.when(productService.create(Mockito.any())).thenReturn(dto);

    JsonApiData<CreateProductRequest.Attributes> request = new JsonApiData<>("products", null, attrs);

    mockMvc.perform(post("/products")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(new Object(){ public JsonApiData<CreateProductRequest.Attributes> getData(){ return request; } }))
      )
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.data.type").value("products"))
      .andExpect(jsonPath("$.data.id").value("p_1"))
      .andExpect(jsonPath("$.data.attributes.name").value("Laptop"));
  }
}


