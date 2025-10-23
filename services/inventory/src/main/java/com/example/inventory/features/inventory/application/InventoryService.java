package com.example.inventory.features.inventory.application;

import com.example.inventory.features.inventory.api.dto.InventoryDto;
import com.example.inventory.features.inventory.api.dto.UpdateInventoryRequest;
import com.example.inventory.features.inventory.api.mapper.InventoryMapper;
import com.example.inventory.features.inventory.domain.InventoryItem;
import com.example.inventory.features.inventory.domain.InventoryRepository;
import com.example.inventory.infra.jsonapi.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

  private final InventoryRepository inventoryRepository;
  private final InventoryMapper inventoryMapper;

  public InventoryService(InventoryRepository inventoryRepository, InventoryMapper inventoryMapper) {
    this.inventoryRepository = inventoryRepository;
    this.inventoryMapper = inventoryMapper;
  }

  @Transactional(readOnly = true)
  public InventoryDto getByProductId(String productId) {
    InventoryItem item = inventoryRepository.findById(productId)
      .orElseGet(() -> {
        InventoryItem created = new InventoryItem();
        created.setProductId(productId);
        created.setQuantity(0);
        return created;
      });
    return inventoryMapper.toDto(item);
  }

  @Transactional
  public InventoryDto setQuantity(String productId, UpdateInventoryRequest.Attributes attributes) {
    InventoryItem item = inventoryRepository.findById(productId)
      .orElseGet(() -> {
        InventoryItem created = new InventoryItem();
        created.setProductId(productId);
        created.setQuantity(0);
        return created;
      });
    item.setQuantity(attributes.getQuantity());
    InventoryItem saved = inventoryRepository.save(item);
    return inventoryMapper.toDto(saved);
  }
}


