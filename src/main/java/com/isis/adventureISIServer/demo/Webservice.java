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

import com.google.gson.Gson;
import com.isis.adventureISIServer.demo.Services;
import generated.PallierType;
import generated.ProductType;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
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
            return Response.ok(services.readWorldFromXml(username)).build();
        }
        
        @PUT
        @Path("product")
        @Consumes(MediaType.APPLICATION_JSON)
        public void product (@Context HttpServletRequest request, ProductType product) throws JAXBException { 
            String username = request.getHeader("X-user");
            services.updateProduct(username, product);
        }

        @PUT
        @Path("manager")
        @Consumes(MediaType.APPLICATION_JSON)
        public void manager (@Context HttpServletRequest request, PallierType pallier) throws JAXBException { 
            String username = request.getHeader("X-user");
            services.updateManager(username, pallier);
            //PallierType pallier = new Gson().fromJson(content, PallierType.class);
        }
        
        @PUT
        @Path("upgrade")//pas implémenté
        @Consumes(MediaType.APPLICATION_JSON)
        public void upgrade (@Context HttpServletRequest request, String content) throws JAXBException { 
            String username = request.getHeader("X-user");
            PallierType upgrade = new Gson().fromJson(content, PallierType.class);
            //update upgrade pas implémenter
        }
    
    
}
