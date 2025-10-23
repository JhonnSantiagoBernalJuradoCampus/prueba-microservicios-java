package com.example.inventory.features.inventory.api;

import com.example.inventory.features.inventory.api.dto.InventoryDto;
import com.example.inventory.features.inventory.api.dto.UpdateInventoryRequest;
import com.example.inventory.features.inventory.application.InventoryService;
import com.example.inventory.infra.jsonapi.GlobalExceptionHandler;
import com.example.inventory.infra.jsonapi.JsonApiData;
import com.example.inventory.infra.jsonapi.JsonApiRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class InventoryControllerTest {

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;
  private InventoryService inventoryService;

  @BeforeEach
  void setup() {
    inventoryService = Mockito.mock(InventoryService.class);
    InventoryController controller = new InventoryController(inventoryService);
    mockMvc = MockMvcBuilders
      .standaloneSetup(controller)
      .setControllerAdvice(new GlobalExceptionHandler())
      .build();
    objectMapper = new ObjectMapper();
  }

  @Test
  void get_inventory_ok() throws Exception {
    InventoryDto dto = new InventoryDto();
    dto.setProductId("p_1");
    dto.setQuantity(0);
    Mockito.when(inventoryService.getByProductId("p_1")).thenReturn(dto);

    mockMvc.perform(get("/inventory/p_1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.data.type").value("inventory"))
      .andExpect(jsonPath("$.data.id").value("p_1"))
      .andExpect(jsonPath("$.data.attributes.quantity").value(0));
  }

  @Test
  void patch_inventory_updates_ok() throws Exception {
    UpdateInventoryRequest.Attributes attrs = new UpdateInventoryRequest.Attributes();
    attrs.setQuantity(15);
    JsonApiRequest<UpdateInventoryRequest.Attributes> req = new JsonApiRequest<>();
    req.setData(new JsonApiData<>("inventory", "p_1", attrs));

    InventoryDto dto = new InventoryDto();
    dto.setProductId("p_1");
    dto.setQuantity(15);
    Mockito.when(inventoryService.setQuantity(Mockito.eq("p_1"), Mockito.any())).thenReturn(dto);

    mockMvc.perform(patch("/inventory/p_1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(req)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.data.attributes.quantity").value(15));
  }
}


