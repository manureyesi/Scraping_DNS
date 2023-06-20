/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scraping.dns;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.crypto.Data;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import com.google.gson.Gson;
import com.scraping.dns.utiles.Credenciales;

/**
 *
 * @author ManuReyesI
 */
public class Scraping {
    
    public static String FICHERO = "CAMBIO_IP.json";
    
    private static Logger log = Logger.getLogger(Scraping.class);
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // Carga el archivo de configuracion de log4J
        PropertyConfigurator.configure("log4j.properties");
        
        /* Este Servicio solo es valido para Dinahosting */
        
        log.info("Iniciando...............................");
        Credenciales creden = null;
        
        log.info("Preparandose para leer Archivo de Objetos");
        try{
            
            File file = new File(FICHERO);
            
            //Comprobar si existe el Archivo
            if(file.exists()){
                //Leendo archivo de Objetos
                FileReader reader = new FileReader(FICHERO);

                creden = new Gson().fromJson(reader, Credenciales.class);

            }else{
                creden = new Credenciales();
            }
            
        }
        catch(IOException ex){
            log.error("Error al leer archivo en el fichero");
        }
        
        String ip = "";
        
        try{
            
            log.info("Recuperando IP");
            
            URL whatismyip = new URL("http://checkip.amazonaws.com");

            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));     

            ip = in.readLine(); 
            
            log.info("IP: "+ip);
            
        }
        catch(IOException ex){
            log.error("Error al leer archivo con IP");
        }
        catch(Exception ex){
            log.error("Error al recuperar IP");
        }
        
        if(ip.compareTo(creden.IP_ACTUAL) != 0){
        
            Response response = null;
            
            creden.IP_NUEVA = ip;

            try{

                Map<String, String> cokies = new HashMap<String, String>();

                log.info("Preparando Inicio de Sesion");

                cokies = Jsoup.connect(creden.URL+"/login").method(Method.POST)
                        .data("id", creden.USUARIO)
                        .data("password", creden.CONTRASENA)
                        .data("recordar", "0")
                        .execute().cookies();

                log.info("Cokies: "+cokies.toString());
                
                try{
                
                    response = Jsoup.connect(creden.URL+"/dominio/procesar-formulario-zonas-modificar-ajax/_producto/"+creden.DOMINIO).method(Method.POST)
                        .data("tipo_zona", creden.TIPO_ZONA)
                        .data("host_ipv4", creden.NOMBRE_HOST)
                        .data("ipv4", creden.IP_NUEVA)
                        .data("destino_original", creden.IP_ACTUAL)
                        .cookies(cokies)
                        .execute();

                }
                catch(Exception ex){}
                
                log.info("IP actual: "+ creden.IP_ACTUAL);

                log.info("IP " + creden.IP_NUEVA + " actualiza con exito para el Host " + creden.NOMBRE_HOST);
                
                creden.IP_ACTUAL = creden.IP_NUEVA;
                
                log.info("IP actual: "+ creden.IP_ACTUAL);
                
            }
            catch(Exception ex){
                log.error("Error al modificar la IP");
            }

            try{

                //Guardando en Fichero
                log.info("Preparando para guarda objeto en Archivo");
                FileWriter fileW = new FileWriter(FICHERO);
                String jsonInString = new Gson().toJson(creden);
                fileW.write(jsonInString);
                fileW.flush();
                fileW.close();

                log.info("Objeto guardado con exito en el archivo " +FICHERO);

            }
            catch(IOException ex){
                log.error("Error al guardar Objeto en el fichero");
            }
            
            
        }
        
    }
    
}
