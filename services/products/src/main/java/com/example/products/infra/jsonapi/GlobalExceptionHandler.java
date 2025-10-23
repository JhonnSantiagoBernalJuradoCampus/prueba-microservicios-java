package com.example.products.infra.jsonapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorsResponse> handleValidation(MethodArgumentNotValidException ex) {
    List<JsonApiError> errors = ex.getBindingResult().getFieldErrors().stream()
      .map(this::toJsonApiError)
      .toList();
    return ResponseEntity.unprocessableEntity().body(new ErrorsResponse(errors));
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorsResponse> handleNotFound(ResourceNotFoundException ex) {
    JsonApiError error = new JsonApiError(String.valueOf(HttpStatus.NOT_FOUND.value()), "Not Found", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorsResponse(List.of(error)));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorsResponse> handleIllegalArgument(IllegalArgumentException ex) {
    JsonApiError error = new JsonApiError(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Bad Request", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorsResponse(List.of(error)));
  }

  @ExceptionHandler(HttpClientErrorException.class)
  public ResponseEntity<?> handleHttpClientError(HttpClientErrorException ex) {
    String body = ex.getResponseBodyAsString();
    // Si inventory ya envió JSON:API, devolvemos tal cual, preservando status
    try {
      // Validar que es JSON y devolverlo parseado para no romper content-negotiation
      ErrorsResponse parsed = objectMapper.readValue(body, ErrorsResponse.class);
      return ResponseEntity.status(ex.getStatusCode()).body(parsed);
    } catch (JsonProcessingException ignored) {
      // Si no es JSON:API, degradamos a error genérico
      JsonApiError error = new JsonApiError(String.valueOf(ex.getStatusCode().value()), ex.getStatusText(), body);
      return ResponseEntity.status(ex.getStatusCode()).body(new ErrorsResponse(List.of(error)));
    }
  }

  private JsonApiError toJsonApiError(FieldError fe) {
    String detail = fe.getField() + " " + fe.getDefaultMessage();
    return new JsonApiError("422", "Validation Error", detail);
  }
}


