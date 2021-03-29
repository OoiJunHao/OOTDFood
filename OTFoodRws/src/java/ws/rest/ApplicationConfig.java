package ws.rest;

import java.util.Set;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.media.multipart.MultiPartFeature;



@javax.ws.rs.ApplicationPath("Resources")
public class ApplicationConfig extends Application
{
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        
        resources.add(MultiPartFeature.class);
        
        return resources;
    }

    
    
    private void addRestResourceClasses(Set<Class<?>> resources){
        resources.add(ws.rest.BentoResource.class);
        resources.add(ws.rest.CorsFilter.class);
        resources.add(ws.rest.OTUserResource.class);
        resources.add(ws.rest.StaffResource.class);
    }    
}
