package org.avaje.glue.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Provides the Jackson ObjectMapper to use with JAX-RS.
 * <p>
 * ObjectMapper can be explicitly set or loaded via service loader or defaults.
 * </p>
 * <p>
 * With Spring if the spring context provides an ObjectMapper that will be used.
 * </p>
 */
public class JacksonMapperRegistry {

  private static ObjectMapper objectMapper;

  /**
   * Explicitly set the ObjectMapper to use.
   */
  public static synchronized void set(ObjectMapper mapper) {
    objectMapper = mapper;
  }

  /**
   * Return the ObjectMapper to use.
   */
  public static synchronized ObjectMapper get() {

    if (objectMapper == null) {
      objectMapper = serviceLoadOrDefault();
    }
    return objectMapper;
  }

  private static ObjectMapper serviceLoadOrDefault() {

    Iterator<JacksonMapperProvider> iterator = ServiceLoader.load(JacksonMapperProvider.class).iterator();
    if (iterator.hasNext()) {
      return iterator.next().provide();
    }

    return createDefault();
  }

  /**
   * Create the default ObjectMapper to use if none has been set or found via service loading.
   */
  private static ObjectMapper createDefault() {

    ObjectMapper mapper = new ObjectMapper();
    mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY);
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    mapper.enable(SerializationFeature.INDENT_OUTPUT);

    return mapper;
  }
}
