/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Unai Pérez Sánchez
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    //TODO
    //Add all the REST
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(service.CoordinateFacadeREST.class);
        resources.add(service.RouteFacadeREST.class);
        resources.add(service.SessionFacadeREST.class);
        resources.add(service.UserFacadeREST.class);
    }
    
}
