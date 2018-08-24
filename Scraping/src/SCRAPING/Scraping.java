/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SCRAPING;

import UTILES.Credenciales;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

//Importar Log
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;

//Importar Jsoup
import org.jsoup.Jsoup;

/**
 *
 * @author ManuReyesI
 */
public class Scraping {
    
    private static Logger log = Logger.getLogger(Scraping.class);
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        // Carga el archivo de configuracion de log4J
        PropertyConfigurator.configure("log4j.properties"); 
        
        /* Este Servicio solo es valido para Dinahosting */
        
        log.info("Iniciando...............................");
        
        try{
            
            log.info("Recuperando IP");
            
            URL whatismyip = new URL("http://checkip.amazonaws.com");

            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));     

            String ip = in.readLine(); 
            
            log.info("IP: "+ip);
            
        }
        catch(IOException ex){
            log.error("Error al leer archivo con IP");
        }
        catch(Exception ex){
            log.error("Error al recuperar IP");
        }
        
        Response response = null;
        
        try{
            
            Map<String, String> cokies = new HashMap<String, String>();
            
            log.info("Preparando Inicio de Sesion");
            
            cokies = Jsoup.connect(Credenciales.URL+"/login").method(Method.POST)
                    .data("id", Credenciales.USUARIO)
                    .data("password", Credenciales.CONTRASENA)
                    .data("recordar", "0")
                    .execute().cookies();
            
            log.info("Cokies: "+cokies.toString());
            
            response = Jsoup.connect(Credenciales.URL+"/dominio/procesar-formulario-zonas-modificar-ajax/_producto/"+Credenciales.DOMINIO).method(Method.POST)
                .data("tipo_zona", Credenciales.TIPO_ZONA)
                .data("host_ipv4", Credenciales.NOMBRE_HOST)
                .data("ipv4", Credenciales.IP_NUEVA)
                .data("destino_original", Credenciales.IP_ACTUAL)
                .cookies(cokies)
                .execute();
            
            log.info("IP " + Credenciales.IP_NUEVA + " actualiza con exito para el Host " + Credenciales.NOMBRE_HOST);
            
        }
        catch(Exception ex){
            log.error("Error al modificar la IP");
        }
        
    }
    
}
