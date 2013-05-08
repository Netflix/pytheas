package com.netflix.explorers.resources;

import javax.ws.rs.GET;

import com.netflix.explorers.annotations.Controller;
import com.netflix.explorers.jersey.ViewableResource;
import com.sun.jersey.api.view.Viewable;

@Controller("admin")
public class ExplorerAdminResource extends ViewableResource {
    
    @GET
    public Viewable info() {
        return view("info");
    }

}
