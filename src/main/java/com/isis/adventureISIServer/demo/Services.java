/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isis.adventureISIServer.demo;

import generated.PallierType;
import generated.ProductType;
import generated.World;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import static java.lang.Math.pow;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author MrsFrozen
 */

//todo : implémenter le score
public class Services {
        
    public World readWorldFromXml(String username) throws JAXBException {
        JAXBContext cont = JAXBContext.newInstance(World.class);
        Unmarshaller u = cont.createUnmarshaller();
        World world;
        try {
            world = (World) u.unmarshal(new File(username + "_world.xml"));
        } catch (JAXBException e) {
            InputStream input = getClass().getClassLoader().getResourceAsStream("world.xml");
            world = (World) u.unmarshal(input);
            System.out.println(e.getMessage());
        }
        return world; //score à ajouter ?
    }
    
    public void saveWorldToXml(World world, String username) {
        try {
            OutputStream output = new FileOutputStream(username + "_world.xml");
            JAXBContext cont = JAXBContext.newInstance(World.class);
            Marshaller m = cont.createMarshaller();
            m.marshal(world, output);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    // prend en paramètre le pseudo du joueur et le produit
    // sur lequel une action a eu lieu (lancement manuel de production ou
    // achat d’une certaine quantité de produit)
    // renvoie false si l’action n’a pas pu être traitée
    public Boolean updateProduct(String username, ProductType newproduct) throws JAXBException {
    // aller chercher le monde qui correspond au joueur
        World world = getWorld(username);
        // trouver dans ce monde, le produit équivalent à celui passé
        // en paramètre
        ProductType product = findProductById(world, newproduct.getId());
        if (product == null) { return false;}
        // calculer la variation de quantité. Si elle est positive c'est
        // que le joueur a acheté une certaine quantité de ce produit
        // sinon c’est qu’il s’agit d’un lancement de production.
        int qtchangee = newproduct.getQuantite() - product.getQuantite();
        if (qtchangee > 0) {//suites geos:formule revue en TP
            world.setMoney(world.getMoney()-(product.getCout()*((1-pow(product.getCroissance(),qtchangee))/(1-product.getCroissance())))); 
            product.setQuantite(product.getQuantite() + qtchangee);
        // soustraire de l'argent du joueur le cout de la quantité
        // achetée et mettre à jour la quantité de product
        } else {
            product.setTimeleft(newproduct.getVitesse());
        // initialiser product.timeleft à product.vitesse
        // pour lancer la production
        }
        // sauvegarder les changements du monde
        saveWorldToXml(world, username);
        return true;
    }
    
        // prend en paramètre le pseudo du joueur et le manager acheté.
    // renvoie false si l’action n’a pas pu être traitée
    public Boolean updateManager(String username, PallierType newmanager) throws JAXBException {
    // aller chercher le monde qui correspond au joueur
        World world = getWorld(username);
        // trouver dans ce monde, le manager équivalent à celui passé
        // en paramètre
        PallierType manager = findManagerByName(world, newmanager.getName());
        if (manager == null) {
            return false;
        }
        manager.setUnlocked(true);
        // débloquer ce manager
        // trouver le produit correspondant au manager
        ProductType product = findProductById(world, manager.getIdcible());
        if (product == null) {
            return false;
        }
        // débloquer le manager de ce produit
        // soustraire de l'argent du joueur le cout du manager
        // sauvegarder les changements au monde
        product.setManagerUnlocked(true); 
        world.setMoney(world.getMoney()-manager.getSeuil());
        saveWorldToXml(world, username);
        return true;
    }
    
    public ProductType findProductById(World world, int productId){//finder pour update product
        List<ProductType> products = world.getProducts().getProduct();
        for (ProductType product : products) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null; // si produit non trouvé renvoie null
    }
    
    public PallierType findManagerByName(World world, String managerName){//finder pour update manager
        List<PallierType> managers = world.getManagers().getPallier();
        for (PallierType manager : managers) {
            if (manager.getName() == managerName ) {// manager.getName().equals(managerName) | essayer ça si la condition ne marche pas
                return manager;
            }
        }
        return null;// si manager non trouvé renvoie null
    }
    
    // le score est maj dans getWorld
    public World getWorld(String username) throws JAXBException {
        World world = readWorldFromXml(username);
        newScore(world);//le score dependant de last update on maj le score avant de reinint last update
        world.setLastupdate(System.currentTimeMillis());//reinit de last update 
        saveWorldToXml(world, username);
        return world;
    }
    
    public void newScore(World world) {//à tester
        long timelapse = System.currentTimeMillis() - world.getLastupdate();
        long time;
        for (ProductType produit : world.getProducts().getProduct()) {
            if ((produit.isManagerUnlocked()) && (produit.getQuantite() > 0)) {//avec manager
                time  = (timelapse - produit.getVitesse() + produit.getTimeleft()) / produit.getVitesse();
                world.setScore(world.getMoney() + (produit.getRevenu() * (1 + world.getActiveangels() * world.getAngelbonus())) * time);
                //timeleft0
                if (produit.getTimeleft() < 0 || produit.getTimeleft() >= produit.getVitesse()) {
                    produit.setTimeleft(0);
                }
            } else {//si pas de manager
                if ((produit.getTimeleft() >= 0) && (produit.getTimeleft() <= timelapse)) {
                    produit.setTimeleft(0);
                } else if (produit.getTimeleft() > 0) {
                    produit.setTimeleft(0);
                }
            }
        }
    }    
    
    
}
