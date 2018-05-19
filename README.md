# glue-core
Glue Jetty + Jersey + Jackson + Spring

Spring boot like but avoiding uber jar, classpath scanning and automatic configuration.

## Background

In a [Docker, Kubernetes, CI/CD world](https://github.com/avaje-glue/glue-core/wiki) we need to do some things better.


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
- Spring used for DI only 

## Why not Spring boot? 
- I have used Spring boot a lot ... and have started un-liking it for both small and large projects
- Desire to be more Docker and Kubernetes friendly 
- Desire to be not so much a Framework but more the "Glue" to bring together other great frameworks
- Want to avoid Uber Jar for Docker reasons
- Want to avoid classpath scanning and any auto configuration that is wasteful (fast starting services)

## Why not Dropwizard? Spark? etc ...

I like Jersey/JAX-RS and I like Spring for DI ... but otherwise I think these a good choices to investigate and there are many other options. 

