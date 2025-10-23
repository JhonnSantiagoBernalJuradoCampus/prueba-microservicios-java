package com.example.inventory.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class InventoryIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void patch_then_get_ok() throws Exception {
    String payload = "{\n" +
      "  \"data\": {\n" +
      "    \"type\": \"inventory\",\n" +
      "    \"id\": \"p_9\",\n" +
      "    \"attributes\": { \"quantity\": 7 }\n" +
      "  }\n" +
      "}";

    mockMvc.perform(patch("/inventory/p_9")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(payload))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.data.id").value("p_9"))
      .andExpect(jsonPath("$.data.attributes.quantity").value(7));

    mockMvc.perform(get("/inventory/p_9").accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.data.id").value("p_9"))
      .andExpect(jsonPath("$.data.attributes.quantity").value(7));
  }
}


