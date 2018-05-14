package org.avaje.glue.core;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Produces("text/plain")
@Path("/_health")
public class HealthResource {

  @GET
  public Response health() {

    ServerState.Status status = ServerState.status();
    switch (status) {
      case ALIVE:
        return Response.ok("ok").build();
//      case STOPPING:
//        return Response.ok("stopping").build();
      default:
        return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("status " + status).build();
    }

  }
}
