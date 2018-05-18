package org.avaje.glue.core;

import org.springframework.context.ApplicationContext;

import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holds the Spring context if created.
 */
public class SpringContext {

  private static ApplicationContext context;

  /**
   * Set the ApplicationContext that has been created.
   */
  public static synchronized void set(ApplicationContext ctx) {
    context = ctx;
  }

  /**
   * Return the Spring application context (if set).
   * <p>
   * Can be used by plugins (like the WebSocket support) to access/find Spring beans.
   * </p>
   */
  public static synchronized ApplicationContext get() {
    return context;
  }

  /**
   * Return the JAX-RS Resources that should be registered (with Jersey or RestEasy etc).
   */
  public static List<Object> allRestResources() {
    if (context == null) {
      return Collections.emptyList();
    }
    return new ArrayList<>(context.getBeansWithAnnotation(Path.class).values());
  }

}
