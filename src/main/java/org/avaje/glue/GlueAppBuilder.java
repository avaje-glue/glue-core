package org.avaje.glue;

import org.avaje.glue.jetty.JettyRun;
import org.avaje.glue.properties.PropertiesLoader;
import org.eclipse.jetty.servlet.FilterHolder;

public class GlueAppBuilder {

  private final JettyRun jettyRun = new JettyRun();

  private final Class<?> configClass;

  private final Class<?> appClass;
  private final String pathSpec;
  private int port;

  public GlueAppBuilder(Class<?> appClass) {

    this.appClass = appClass;
    Glue glueApp = appClass.getAnnotation(Glue.class);
    if (glueApp == null) {
      throw new IllegalStateException("Missing @Glue annotation on " + appClass);
    }
    this.port = glueApp.port();
    this.configClass = glueApp.config();
    this.pathSpec = glueApp.pathSpec();
  }

  /**
   * Run the Jetty based application passing the command line arguments.
   *
   * @param args The command line arguments.
   */
  public void run(String[] args) {

    // load properties and yml files including ones passed as command line args
    PropertiesLoader.load(args);

    FilterHolder holder = jerseyFilter(configClass.getName());
    jettyRun.getContext().addFilter(holder, pathSpec, null);

    if (port > 0) {
      jettyRun.setHttpPort(port);
    }

    jettyRun.run();
  }

  private FilterHolder jerseyFilter(String applicationClassName) {
    FilterHolder holder = new FilterHolder();
    holder.setName("jersey");
    holder.setClassName("org.glassfish.jersey.servlet.ServletContainer");
    holder.getInitParameters().put("jersey.config.servlet.filter.staticContentRegex", "(/favicon.ico|/(assets|images|fonts|css|js)/.*)");
    holder.getInitParameters().put("javax.ws.rs.Application", applicationClassName);
    return holder;
  }
}
