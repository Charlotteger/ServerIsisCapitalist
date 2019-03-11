package com.isis.adventureISIServer.demo;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cgerber
 */
public class CORSResponseFilter {
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException{
        requestContext.getHeaders().add("Access-Control-Allow-Origin", "*");
        requestContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE,PUT, OPTIONS");
        requestContext.getHeaders().add("Access-Control-Allow-Headers", "X-Requested-With,Content-Type, X-Codingpedia, authorization,X-User");
    }
            
}
