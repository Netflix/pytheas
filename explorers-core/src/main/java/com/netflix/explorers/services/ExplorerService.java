package com.netflix.explorers.services;

/**
 * Interface for a singleton service within the explorer framework.  
 * Services are a mechanism to add singletons that may be shared by
 * multiple explorers.  Services are registered with the ExplorerManager
 * and should be started by any explorer that needs it.  The service
 * should support multiple starts and stops.
 * 
 * @author elandau
 *
 */
public interface ExplorerService {
    void start();
    
    void stop();
}
