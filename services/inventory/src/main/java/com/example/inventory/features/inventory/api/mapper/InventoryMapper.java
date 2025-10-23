package com.example.inventory.features.inventory.api.mapper;

import com.example.inventory.features.inventory.api.dto.InventoryDto;
import com.example.inventory.features.inventory.api.dto.UpdateInventoryRequest;
import com.example.inventory.features.inventory.domain.InventoryItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
  InventoryDto toDto(InventoryItem item);

  @Mapping(target = "productId", source = "productId")
  @Mapping(target = "quantity", source = "attributes.quantity")
  InventoryItem fromUpdate(String productId, UpdateInventoryRequest.Attributes attributes);
}


