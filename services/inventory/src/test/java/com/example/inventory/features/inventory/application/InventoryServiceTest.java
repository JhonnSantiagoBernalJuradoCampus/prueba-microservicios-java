package com.example.inventory.features.inventory.application;

import com.example.inventory.features.inventory.api.dto.InventoryDto;
import com.example.inventory.features.inventory.api.dto.UpdateInventoryRequest;
import com.example.inventory.features.inventory.api.mapper.InventoryMapper;
import com.example.inventory.features.inventory.domain.InventoryItem;
import com.example.inventory.features.inventory.domain.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

  @Mock
  private InventoryRepository inventoryRepository;
  @Mock
  private InventoryMapper inventoryMapper;

  @InjectMocks
  private InventoryService inventoryService;

  private UpdateInventoryRequest.Attributes attrs;

  @BeforeEach
  void setup() {
    attrs = new UpdateInventoryRequest.Attributes();
    attrs.setQuantity(10);
  }

  @Test
  void getByProductId_whenNotExists_returnsZeroQuantityDto() {
    String productId = "p_1";
    when(inventoryRepository.findById(productId)).thenReturn(Optional.empty());
    InventoryDto dto = new InventoryDto();
    dto.setProductId(productId);
    dto.setQuantity(0);
    when(inventoryMapper.toDto(any(InventoryItem.class))).thenReturn(dto);

    InventoryDto result = inventoryService.getByProductId(productId);
    assertThat(result.getProductId()).isEqualTo(productId);
    assertThat(result.getQuantity()).isEqualTo(0);
  }

  @Test
  void setQuantity_updatesAndReturnsDto() {
    String productId = "p_2";
    InventoryItem existing = new InventoryItem();
    existing.setProductId(productId);
    existing.setQuantity(5);
    when(inventoryRepository.findById(productId)).thenReturn(Optional.of(existing));

    InventoryItem saved = new InventoryItem();
    saved.setProductId(productId);
    saved.setQuantity(10);
    when(inventoryRepository.save(existing)).thenReturn(saved);

    InventoryDto dto = new InventoryDto();
    dto.setProductId(productId);
    dto.setQuantity(10);
    when(inventoryMapper.toDto(saved)).thenReturn(dto);

    InventoryDto result = inventoryService.setQuantity(productId, attrs);
    assertThat(result.getQuantity()).isEqualTo(10);
    verify(inventoryRepository).save(existing);
  }
}


