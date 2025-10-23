package com.example.products.features.product.api;

import com.example.products.features.inventoryclient.InventoryClient;
import com.example.products.features.product.api.dto.CreateProductRequest;
import com.example.products.features.product.api.dto.ProductDto;
import com.example.products.features.product.application.ProductService;
import com.example.products.features.product.application.PurchaseService;
import com.example.products.infra.jsonapi.JsonApiData;
import com.example.products.infra.jsonapi.JsonApiRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PurchaseControllerIntegrationTest {

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @BeforeEach
  void setup() {
    ProductService productService = Mockito.mock(ProductService.class);
    InventoryClient inventoryClient = Mockito.mock(InventoryClient.class);
    PurchaseService purchaseService = new PurchaseService(productService, inventoryClient);
    PurchaseController controller = new PurchaseController(purchaseService);

    ProductDto dto = new ProductDto();
    dto.setId("p_1");
    dto.setName("Phone");
    dto.setPrice(BigDecimal.valueOf(20));
    Mockito.when(productService.getById("p_1")).thenReturn(dto);

    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    objectMapper = new ObjectMapper();
  }

  @Test
  void purchase_ok_returns_total() throws Exception {
    var attrs = new com.example.products.features.product.api.dto.PurchaseRequest.Attributes();
    attrs.setProductId("p_1");
    attrs.setQuantity(2);
    var req = new JsonApiRequest<com.example.products.features.product.api.dto.PurchaseRequest.Attributes>();
    req.setData(new JsonApiData<>("purchase", null, attrs));

    mockMvc.perform(post("/purchase")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(req)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.data.type").value("purchase"))
      .andExpect(jsonPath("$.data.attributes.totalPrice").value(40.0));
  }
}
