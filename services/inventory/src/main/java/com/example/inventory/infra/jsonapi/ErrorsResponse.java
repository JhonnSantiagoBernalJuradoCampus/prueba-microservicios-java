package com.example.inventory.infra.jsonapi;

import java.util.List;

public class ErrorsResponse {
  private final List<JsonApiError> errors;

  public ErrorsResponse(List<JsonApiError> errors) {
    this.errors = errors;
  }

  public List<JsonApiError> getErrors() {
    return errors;
  }
}


