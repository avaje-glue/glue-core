package org.avaje.glue.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.avaje.glue.core.HealthResource;
import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Jersey ResourceConfig that registers common features including Jackson and Health endpoint.
 */
public class BaseRestConfig extends ResourceConfig {

  private static final Logger log = LoggerFactory.getLogger(BaseRestConfig.class);

  public BaseRestConfig() {
    log.debug("Registering Jersey resources");

    registerObjectMapper();

    property(CommonProperties.METAINF_SERVICES_LOOKUP_DISABLE, true);
    property(CommonProperties.FEATURE_AUTO_DISCOVERY_DISABLE, true);
    property(CommonProperties.MOXY_JSON_FEATURE_DISABLE, true);

    register(HealthResource.class);
  }

  /**
   * Can override ObjectMapper registration if desired.
   */
  protected void registerObjectMapper() {

    ObjectMapper mapper = JacksonMapperRegistry.get();

    // create JsonProvider to provide custom ObjectMapper
    JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider(mapper, JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS);
    //provider.setMapper(mapper);

    register(provider);
  }
}
