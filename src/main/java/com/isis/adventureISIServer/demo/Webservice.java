package com.isis.adventureISIServer.demo;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author MrsFrozen
 */

import com.isis.adventureISIServer.demo.Services;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;

@Path("generic")
public class Webservice {
    
        Services services;
        
        public Webservice(){
            services = new Services();
        }
        /*
        @GET // sans user
        @Path("world")
        @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
        public Response getWorld() throws JAXBException{
            return Response.ok(services.readWorldFromXml()).build(); 
        }
    */
    
        @GET // partie avec user
        @Path("world")
        @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
        public Response getXML(@Context HttpServletRequest request) throws JAXBException{
            String username = request.getHeader("X-user");
            return Response.ok(services.readWorldFromXml(username)).build();//faut modifier cette ligne
        }
    
    
}
