package org.avaje.glue.core;

import org.avaje.glue.jetty.ContainerLifecycleListener;

public class ServerLifecycleState implements ContainerLifecycleListener {

  @Override
  public void starting() {
    ServerState.get().starting();
  }

  @Override
  public void started() {
    ServerState.get().alive();
  }

  @Override
  public void failure() {
    ServerState.get().stopping();
  }

  @Override
  public void stopping() {
    ServerState.get().stopping();
  }

  @Override
  public void stopped() {
    ServerState.get().stopped();
  }
}
