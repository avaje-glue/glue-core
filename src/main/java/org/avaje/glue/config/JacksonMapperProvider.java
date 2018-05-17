package org.avaje.glue.config;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Interface used to provide an ObjectMapper via Service loading.
 */
public interface JacksonMapperProvider {

  /**
   * Return the ObjectMapper to use with JAX-RS.
   */
  ObjectMapper provide();
}
