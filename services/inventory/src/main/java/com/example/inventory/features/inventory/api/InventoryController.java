package com.example.inventory.features.inventory.api;

import com.example.inventory.features.inventory.api.dto.InventoryDto;
import com.example.inventory.features.inventory.api.dto.UpdateInventoryRequest;
import com.example.inventory.features.inventory.application.InventoryService;
import com.example.inventory.infra.jsonapi.JsonApiData;
import com.example.inventory.infra.jsonapi.JsonApiRequest;
import com.example.inventory.infra.jsonapi.JsonApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

  private final InventoryService inventoryService;

  public InventoryController(InventoryService inventoryService) {
    this.inventoryService = inventoryService;
  }

  @GetMapping("/{productId}")
  public ResponseEntity<JsonApiResponse<InventoryDto>> get(@PathVariable String productId) {
    InventoryDto dto = inventoryService.getByProductId(productId);
    return ResponseEntity.ok(new JsonApiResponse<>(new JsonApiData<>("inventory", dto.getProductId(), dto)));
  }

  @PatchMapping("/{productId}")
  public ResponseEntity<JsonApiResponse<InventoryDto>> setQuantity(@PathVariable String productId,
                                                                    @Valid @RequestBody JsonApiRequest<UpdateInventoryRequest.Attributes> body) {
    InventoryDto updated = inventoryService.setQuantity(productId, body.getData().getAttributes());
    return ResponseEntity.ok(new JsonApiResponse<>(new JsonApiData<>("inventory", updated.getProductId(), updated)));
  }

  @PatchMapping("/{productId}/decrement")
  public ResponseEntity<JsonApiResponse<InventoryDto>> decrement(@PathVariable String productId,
                                                                 @RequestParam("amount") int amount) {
    InventoryDto updated = inventoryService.decrement(productId, amount);
    return ResponseEntity.ok(new JsonApiResponse<>(new JsonApiData<>("inventory", updated.getProductId(), updated)));
  }
}


