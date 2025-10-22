package com.example.products.infra.jsonapi;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonApiResponse<T> {
  private JsonApiData<T> data;

  public JsonApiResponse() {}

  public JsonApiResponse(JsonApiData<T> data) {
    this.data = data;
  }

  public JsonApiData<T> getData() {
    return data;
  }

  public void setData(JsonApiData<T> data) {
    this.data = data;
  }
}


