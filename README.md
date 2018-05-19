# glue-core
Glue Jetty + Jersey + Jackson + Spring

Spring boot like but avoiding uber jar, classpath scanning and automatic configuration.

## Background

In a Docker, Kubernetes, CI/CD world we need to do some things better.
Refer to: https://github.com/avaje-glue/glue-core/wiki


## Goals
- main() method to run (and debug)
- Docker friendly via lib/* classpath (i.e. avoid uber jar / war)
- Fast and lean (avoid classpath scanning and automatic configuration)
- Use Spring for DI only (can optionally use other DI such as Guice or Dagger)
- Support Rest via Jersey
- Support Websockets
- Support static content (via default servlet)
- Support additional Servlets and Filters as desired

## Limitations
- Restricted to Jetty 
- No JSP support out of the box


## Why not Spring boot? Dropwizard? Spark? etc ...
- I have used Spring boot a lot ... and have started un-liking it for both small and large projects
- Desire to be Docker and Kubernetes friendly
- Desire to be not so much a Framework but more the "Glue" to bring together other great frameworks

