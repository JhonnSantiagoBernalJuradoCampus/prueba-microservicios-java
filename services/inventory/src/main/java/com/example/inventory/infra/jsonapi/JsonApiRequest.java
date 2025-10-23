package com.example.inventory.infra.jsonapi;

public class JsonApiRequest<T> {
  private JsonApiData<T> data;

  public JsonApiData<T> getData() { return data; }
  public void setData(JsonApiData<T> data) { this.data = data; }
}


