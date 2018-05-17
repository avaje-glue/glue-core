package org.avaje.glue;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.avaje.glue.config.BaseSpringRestConfig;
import org.avaje.glue.config.JacksonMapperRegistry;
import org.avaje.glue.core.SpringContext;
import org.avaje.glue.jetty.JettyRun;
import org.avaje.glue.properties.PropertiesLoader;
import org.eclipse.jetty.servlet.FilterHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.LogManager;

/**
 * Used to bootstrap a Glue of Jetty + Jersey + Jackson + optionally Spring.
 *
 * <pre>{@code
 *
 * // where AppConfig is a spring @Configuration bean
 * // or a JAX-RS ResourceConfig bean
 *
 * @Glue(config = AppConfig.class, port = 8090)
 * public class Application {
 *
 *   public static void main(String[] args) {
 *
 *     new GlueAppBuilder(Application.class).run(args);
 *   }
 * }
 *
 * }</pre>
 */
public class GlueAppBuilder {

  private static final Logger log = LoggerFactory.getLogger(GlueAppBuilder.class);

  private final JettyRun jettyRun = new JettyRun();

  private final Class<?> configClass;

  private final Class<?> appClass;
  private final String pathSpec;

  private AnnotationConfigApplicationContext springAppContext;

  private int port;

  /**
   * Create with the given class annotated with <code>@Glue</code>.
   */
  public GlueAppBuilder(Class<?> appClass) {

    this.appClass = appClass;
    Glue glueApp = appClass.getAnnotation(Glue.class);
    if (glueApp == null) {
      throw new IllegalStateException("Missing @Glue annotation on " + appClass);
    }
    this.port = glueApp.port();
    this.configClass = glueApp.config();
    this.pathSpec = glueApp.pathSpec();

    LogManager.getLogManager().reset();
    SLF4JBridgeHandler.install();
  }

  /**
   * Run the Jetty based application passing the command line arguments.
   *
   * @param args The command line arguments.
   */
  public void run(String[] args) {

    // load properties and yml files including ones passed as command line args
    Properties props = PropertiesLoader.load(args);

    String restConfigClassName = configClass.getName();

    if (!configClass.isAssignableFrom(ResourceConfig.class)) {
      restConfigClassName = configureWithSpring(props);
    }

    log.info("Wiring Jersey filter using ResourceConfig {}", restConfigClassName);

    FilterHolder holder = jerseyFilter(restConfigClassName);
    jettyRun.getContext().addFilter(holder, pathSpec, null);

    if (port > 0) {
      jettyRun.setHttpPort(port);
    }

    jettyRun.run();
  }

  /**
   * Configure the application using Spring.
   */
  private String configureWithSpring(Properties props) {

    log.info("Wiring using spring configuration {}", configClass);

    PropertiesPropertySource source = new PropertiesPropertySource("appConfig", props);

    springAppContext = new AnnotationConfigApplicationContext();
    springAppContext.getEnvironment().getPropertySources().addLast(source);
    springAppContext.register(configClass);
    springAppContext.refresh();

    SpringContext.set(springAppContext);

    springRegisterJacksonMapper();

    // search for a spring bean that configures JAX-RS
    Collection<ResourceConfig> resourceConfigs = springAppContext.getBeansOfType(ResourceConfig.class).values();
    int size = resourceConfigs.size();
    if (size > 1) {
      throw new IllegalStateException("More that one Spring bean that is a ResourceConfig? " + resourceConfigs);
    }
    if (size == 1) {
      return first(resourceConfigs);

    } else {
      // just use our default BaseSpringRestConfig
      String restConfigClassName = BaseSpringRestConfig.class.getName();
      springAppContext.registerBean(restConfigClassName, BaseSpringRestConfig.class);
      return restConfigClassName;
    }
  }

  /**
   * Register a spring context provided ObjectMapper to use with JAX-RS if it is available.
   */
  private void springRegisterJacksonMapper() {
    try {
      JacksonMapperRegistry.set(springAppContext.getBean(ObjectMapper.class));
    } catch (NoSuchBeanDefinitionException e) {
      log.debug("use default or service loaded Jackson ObjectMapper");
    }
  }

  /**
   * Return the class name of the resourceConfig bean (used to add the Jersey filter).
   */
  private String first(Collection<ResourceConfig> resourceConfigs) {
    Iterator<ResourceConfig> iterator = resourceConfigs.iterator();
    if (iterator.hasNext()) {
      return iterator.next().getClass().getName();
    }
    throw new IllegalStateException("No ResourceConfig?");
  }

  /**
   * Build and return the Jetty FilterHolder for the Jersey servlet filter.
   */
  private FilterHolder jerseyFilter(String applicationClassName) {
    FilterHolder holder = new FilterHolder();
    holder.setName("jersey");
    holder.setClassName("org.glassfish.jersey.servlet.ServletContainer");
    holder.getInitParameters().put("jersey.config.servlet.filter.staticContentRegex", "(/favicon.ico|/(assets|images|fonts|css|js)/.*)");
    holder.getInitParameters().put("javax.ws.rs.Application", applicationClassName);
    return holder;
  }
}
