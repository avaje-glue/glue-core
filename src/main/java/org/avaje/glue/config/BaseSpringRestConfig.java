package org.avaje.glue.config;

import org.avaje.glue.core.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Automatically registers all Spring beans with Jax-RS <code>@Path</code> annotation.
 */
public class BaseSpringRestConfig extends BaseRestConfig {

  private static final Logger log = LoggerFactory.getLogger(BaseSpringRestConfig.class);

  public BaseSpringRestConfig() {
    super();
    for (Object object : SpringContext.allRestResources()) {
      log.debug("register spring resource {}", object.getClass());
      register(object);
    }
  }

}
