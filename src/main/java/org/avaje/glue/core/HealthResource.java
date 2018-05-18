package org.avaje.glue.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.lang.management.ManagementFactory;
import java.util.concurrent.atomic.AtomicBoolean;

@Singleton
@Produces("text/plain")
@Path("/_health")
public class HealthResource {

  private static final Logger log = LoggerFactory.getLogger(HealthResource.class);

  private final AtomicBoolean alive = new AtomicBoolean(false);

  @GET
  public Response health() {

    ServerState.Status status = ServerState.status();
    switch (status) {
      case ALIVE:
        return alive();

      default:
        return notAvailable(status);
    }
  }

  private Response notAvailable(ServerState.Status status) {
    if (alive.get()) {
      if (alive.compareAndSet(true, false)) {
        // log on change from Alive to NotAvailable being reported
        log.info("NotAvailable server state reported");
      }
    }
    return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("status " + status).build();
  }

  private Response alive() {
    if (!alive.get()) {
      if (alive.compareAndSet(false, true)) {
        // log the first time we report Alive status
        long atMillis = ManagementFactory.getRuntimeMXBean().getUptime();
        log.info("Alive server state reported at {}ms", atMillis);
      }
    }
    return Response.ok("ok").build();
  }
}
