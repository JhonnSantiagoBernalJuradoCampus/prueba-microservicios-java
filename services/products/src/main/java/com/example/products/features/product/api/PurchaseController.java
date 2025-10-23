package com.example.products.features.product.api;

import com.example.products.features.product.api.dto.PurchaseRequest;
import com.example.products.features.product.api.dto.PurchaseResponse;
import com.example.products.features.product.application.PurchaseService;
import com.example.products.infra.jsonapi.JsonApiData;
import com.example.products.infra.jsonapi.JsonApiRequest;
import com.example.products.infra.jsonapi.JsonApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

  private final PurchaseService purchaseService;

  public PurchaseController(PurchaseService purchaseService) {
    this.purchaseService = purchaseService;
  }

  @PostMapping
  public ResponseEntity<JsonApiResponse<PurchaseResponse>> purchase(@Valid @RequestBody JsonApiRequest<PurchaseRequest.Attributes> body) {
    PurchaseResponse resp = purchaseService.purchase(body.getData().getAttributes());
    return ResponseEntity.ok(new JsonApiResponse<>(new JsonApiData<>("purchase", resp.getProductId(), resp)));
  }
}
