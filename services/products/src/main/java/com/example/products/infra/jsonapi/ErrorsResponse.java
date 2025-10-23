package com.example.products.infra.jsonapi;

import java.util.List;

public class ErrorsResponse {
  private List<JsonApiError> errors;

  public ErrorsResponse() {
  }

  public ErrorsResponse(List<JsonApiError> errors) {
    this.errors = errors;
  }

  public List<JsonApiError> getErrors() {
    return errors;
  }

  public void setErrors(List<JsonApiError> errors) {
    this.errors = errors;
  }
}
