package org.avaje.glue.core;

public class ServerState {

  private static ServerState INSTANCE = new ServerState();

  private Status status;

  public static Status status() {
    return INSTANCE.status;
  }

  public enum Status {
    STARTING,
    ALIVE,
    STOPPING,
    STOPPED
  }


  public static ServerState get() {
    return INSTANCE;
  }

  public synchronized void starting() {
    status = Status.STARTING;
  }

  public synchronized void alive() {
    status = Status.ALIVE;
  }

  public synchronized void stopping() {
    status = Status.STOPPING;
  }

  public synchronized void stopped() {
    status = Status.STOPPED;
  }

}
