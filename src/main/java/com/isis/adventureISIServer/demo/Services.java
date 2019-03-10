/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isis.adventureISIServer.demo;

import generated.World;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author MrsFrozen
 */
public class Services {
    
    public World readWorldFromXml() throws JAXBException {
        JAXBContext cont = JAXBContext.newInstance(World.class);
        Unmarshaller u = cont.createUnmarshaller();
        World world;
        try {
            world = (World) u.unmarshal(new File("world.xml"));
        }
        catch (JAXBException e) {
            InputStream input = getClass().getClassLoader().getResourceAsStream("world.xml");
            world = (World) u.unmarshal(input);
            System.out.println(e.getMessage());
        }
        return world;
    }
    
    public void saveWorldToXml(World world) {
        try {
            OutputStream output = new FileOutputStream("world.xml");
            JAXBContext cont = JAXBContext.newInstance(World.class);
            Marshaller m = cont.createMarshaller();
            m.marshal(world, output);
        } 
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
}
