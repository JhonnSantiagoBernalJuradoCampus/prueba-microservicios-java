package com.example.products.features.product.api;

import com.example.products.features.inventoryclient.InventoryClient;
import com.example.products.features.product.api.dto.ProductDto;
import com.example.products.features.product.application.ProductService;
import com.example.products.features.product.application.PurchaseService;
import com.example.products.infra.jsonapi.GlobalExceptionHandler;
import com.example.products.infra.jsonapi.JsonApiData;
import com.example.products.infra.jsonapi.JsonApiRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PurchaseControllerIntegrationTest {

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;
  private ProductService productService;
  private InventoryClient inventoryClient;

  @BeforeEach
  void setup() {
    productService = Mockito.mock(ProductService.class);
    inventoryClient = Mockito.mock(InventoryClient.class);
    PurchaseService purchaseService = new PurchaseService(productService, inventoryClient);
    PurchaseController controller = new PurchaseController(purchaseService);

    mockMvc = MockMvcBuilders.standaloneSetup(controller)
      .setControllerAdvice(new GlobalExceptionHandler())
      .build();
    objectMapper = new ObjectMapper();
  }

  @Test
  void purchase_ok_returns_total() throws Exception {
    ProductDto dto = new ProductDto();
    dto.setId("p_1");
    dto.setName("Phone");
    dto.setPrice(BigDecimal.valueOf(20));
    Mockito.when(productService.getById("p_1")).thenReturn(dto);

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

  @Test
  void purchase_product_not_found_returns_404() throws Exception {
    Mockito.when(productService.getById("missing")).thenThrow(new com.example.products.infra.jsonapi.ResourceNotFoundException("Product missing not found"));

    var attrs = new com.example.products.features.product.api.dto.PurchaseRequest.Attributes();
    attrs.setProductId("missing");
    attrs.setQuantity(1);
    var req = new JsonApiRequest<com.example.products.features.product.api.dto.PurchaseRequest.Attributes>();
    req.setData(new JsonApiData<>("purchase", null, attrs));

    mockMvc.perform(post("/purchase")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(req)))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.errors[0].status").value("404"));
  }

  @Test
  void purchase_insufficient_stock_propagates_400() throws Exception {
    ProductDto dto = new ProductDto();
    dto.setId("p_2");
    dto.setName("Phone");
    dto.setPrice(BigDecimal.valueOf(20));
    Mockito.when(productService.getById("p_2")).thenReturn(dto);

    String json = "{\"errors\":[{\"status\":\"400\",\"title\":\"Bad Request\",\"detail\":\"Insufficient stock\"}]}";
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    doThrow(org.springframework.web.client.HttpClientErrorException.create(
        org.springframework.http.HttpStatus.BAD_REQUEST,
        "Bad Request",
        headers,
        json.getBytes(StandardCharsets.UTF_8),
        StandardCharsets.UTF_8
      ))
      .when(inventoryClient).decrement("p_2", 100);

    var attrs = new com.example.products.features.product.api.dto.PurchaseRequest.Attributes();
    attrs.setProductId("p_2");
    attrs.setQuantity(100);
    var req = new JsonApiRequest<com.example.products.features.product.api.dto.PurchaseRequest.Attributes>();
    req.setData(new JsonApiData<>("purchase", null, attrs));

    mockMvc.perform(post("/purchase")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(req)))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors[0].status").value("400"))
      .andExpect(jsonPath("$.errors[0].detail").value("Insufficient stock"));
  }
}
