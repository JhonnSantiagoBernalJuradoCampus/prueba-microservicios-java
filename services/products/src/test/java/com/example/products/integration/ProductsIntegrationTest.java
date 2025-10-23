package com.example.products.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductsIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void create_then_getById_success() throws Exception {
    String body = "{\n" +
      "  \"data\": {\n" +
      "    \"type\": \"products\",\n" +
      "    \"attributes\": {\n" +
      "      \"name\": \"Phone\",\n" +
      "      \"price\": 499.99,\n" +
      "      \"description\": \"Android\"\n" +
      "    }\n" +
      "  }\n" +
      "}";

    MvcResult created = mockMvc.perform(post("/products")
        .contentType(MediaType.APPLICATION_JSON)
        .content(body))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.data.type").value("products"))
      .andReturn();

    String location = created.getResponse().getHeader("Location");
    assertThat(location).isNotBlank();

    mockMvc.perform(get(location))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.data.id").exists())
      .andExpect(jsonPath("$.data.attributes.name").value("Phone"));
  }
}


