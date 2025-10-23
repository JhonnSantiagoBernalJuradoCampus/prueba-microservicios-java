package com.example.products.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

  @Value("${security.api-key.enabled:false}")
  private boolean enabled;

  @Value("${security.api-key.header:X-API-Key}")
  private String headerName;

  @Value("${security.api-key.value:}")
  private String expectedValue;

  private boolean isWhitelisted(String path) {
    return path.startsWith("/v3/api-docs")
      || path.startsWith("/swagger-ui")
      || path.equals("/swagger-ui.html")
      || path.equals("/actuator")
      || path.startsWith("/actuator/")
      || path.equals("/error");
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (!enabled || isWhitelisted(request.getRequestURI())) {
      filterChain.doFilter(request, response);
      return;
    }
    String header = request.getHeader(headerName);
    if (expectedValue != null && !expectedValue.isBlank() && expectedValue.equals(header)) {
      filterChain.doFilter(request, response);
    } else {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      response.getWriter().write("{\"errors\":[{\"status\":401,\"title\":\"Unauthorized\",\"detail\":\"Invalid API Key\"}]}\n");
    }
  }
}
