package org.avaje.glue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used on the main application class to specify the configuration.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Glue {

  /**
   * The Spring or Jersey Application configuration class.
   * <p>
   * For a Spring Jersey application this should refer to a Spring <code>@Configuration</code> bean
   * that specifies the wiring of the Spring context.
   * </p>
   * <p>
   * For a Jersey application without Spring this should refer to a class that extends JAX-RS ResourceConfig.
   * </p>
   */
  Class<?> config();

  /**
   * The HTTP port that should be used. Will default to 8080.
   */
  int port() default 0;


  /**
   * The path spec mapping for the Jersey filter.
   */
  String pathSpec() default "/*";

}
