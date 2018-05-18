package org.avaje.glue.core;

import org.avaje.glue.jetty.JettyRun;
import org.avaje.glue.properties.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Shutdown logic that is executed when shutdown is requested.
 * <ul>
 *   <li>Change the Health response (Server state) to Not available</li>
 *   <li>Wait for any active requests to complete (with a timeout)</li>
 *   <li>Finally execute any registered shutdown hooks</li>
 * </ul>
 */
public class ShutdownLogic implements Runnable {

  private static final Logger log = LoggerFactory.getLogger(ShutdownLogic.class);

  private final int minDelaySecs = Property.getInt("shutdown.minDelaySecs", 3);

  private final JettyRun jettyRun;

  public ShutdownLogic(JettyRun jettyRun) {
    this.jettyRun = jettyRun;
  }

  @Override
  public void run() {

    int activeRequests = jettyRun.activeRequests();
    log.info("Shutdown requested - activeRequests {}  minDelaySecs:{}", activeRequests, minDelaySecs);
    ServerState.get().stopping();

    performMinWait();

    jettyRun.waitForActiveRequests();
  }

  private void performMinWait() {

    try {
      Thread.sleep(minDelaySecs * 1000);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      log.warn("InterruptedException while waiting for shutdown.minDelaySecs");
    }
  }

}
