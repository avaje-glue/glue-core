package org.avaje.glue.config;

import javax.websocket.server.ServerEndpointConfig;
import java.util.List;

/**
 * Interface for plugins that provide WebSocket endpoints.
 */
public interface WebSocketProvider {

  /**
   * Return the WebSocket endpoints that should be registered with the container.
   */
  List<ServerEndpointConfig> endpoints();
}
