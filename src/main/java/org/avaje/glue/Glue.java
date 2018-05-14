package org.avaje.glue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specify the default cache use specific entity type.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Glue {

  /**
   * The Jersey Application configuration class.
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
