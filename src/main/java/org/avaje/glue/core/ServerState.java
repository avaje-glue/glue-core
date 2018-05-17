package org.avaje.glue.core;

/**
 * Holds the current state of the Server for reporting the health/availability of the service.
 */
public class ServerState {

  private static ServerState INSTANCE = new ServerState();

  private Status status;

  /**
   * Return the current status.
   */
  public static Status status() {
    return INSTANCE.status;
  }

  /**
   * The Status of the Server.
   */
  public enum Status {
    STARTING,
    ALIVE,
    STOPPING,
    STOPPED
  }

  /**
   * Return the current server state.
   */
  public static ServerState get() {
    return INSTANCE;
  }

  /**
   * Set the state to Starting.
   */
  public synchronized void starting() {
    status = Status.STARTING;
  }

  /**
   * Set the state to Alive.
   */
  public synchronized void alive() {
    status = Status.ALIVE;
  }

  /**
   * Set the state to Stopping.
   */
  public synchronized void stopping() {
    status = Status.STOPPING;
  }

  /**
   * Set the state to Stopped.
   */
  public synchronized void stopped() {
    status = Status.STOPPED;
  }

}
