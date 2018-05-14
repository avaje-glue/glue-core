package org.avaje.glue.config;

import org.avaje.glue.core.HealthResource;
import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class BaseConfig extends ResourceConfig {

  public BaseConfig() {

    register(ObjectMapperProvider.get());
    register(JacksonFeature.class);

    property(CommonProperties.METAINF_SERVICES_LOOKUP_DISABLE, true);
    property(CommonProperties.FEATURE_AUTO_DISCOVERY_DISABLE, true);
    property(CommonProperties.MOXY_JSON_FEATURE_DISABLE, true);

    register(HealthResource.class);

  }
}
