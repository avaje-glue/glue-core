package org.avaje.glue.config;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperProvider {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static ObjectMapper get() {
    return objectMapper;
  }
}
