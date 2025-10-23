package com.example.inventory.infra.jsonapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
    List<JsonApiError> errors = ex.getBindingResult().getFieldErrors().stream()
      .map(this::toJsonApiError)
      .toList();
    return ResponseEntity.unprocessableEntity().body(new ErrorsResponse(errors));
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<?> handleNotFound(ResourceNotFoundException ex) {
    JsonApiError error = new JsonApiError(String.valueOf(HttpStatus.NOT_FOUND.value()), "Not Found", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorsResponse(List.of(error)));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {
    JsonApiError error = new JsonApiError(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Bad Request", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorsResponse(List.of(error)));
  }

  private JsonApiError toJsonApiError(FieldError fe) {
    String detail = fe.getField() + " " + fe.getDefaultMessage();
    return new JsonApiError("422", "Validation Error", detail);
  }
}


