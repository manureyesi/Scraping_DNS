/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SCRAPING;

import UTILES.Credenciales;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

//Importar Log
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

//Importar Jsoup
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;

/**
 *
 * @author ManuReyesI
 */
public class Scraping {
    
    public static String FICHERO = "CAMBIO_IP";
    
    private static Logger log = Logger.getLogger(Scraping.class);
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // Carga el archivo de configuracion de log4J
        PropertyConfigurator.configure("log4j.properties"); 
        
        /* Este Servicio solo es valido para Dinahosting */
        
        log.info("Iniciando...............................");
        UTILES.Credenciales creden = null;
        
        log.info("Preparandose para leer Archivo de Objetos");
        try{
            
            File file = new File(FICHERO);
            
            //Comprobar si existe el Archivo
            if(file.exists()){
                //Leendo archivo de Objetos
                ObjectInputStream leerObjeto = new ObjectInputStream(new FileInputStream(FICHERO));
                
                creden = new UTILES.Credenciales();
                
                creden = (UTILES.Credenciales)leerObjeto.readObject();
                
            }
            
        }
        catch(IOException | ClassNotFoundException ex){
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
        
        if(ip.compareTo(Credenciales.IP_ACTUAL) != 0){
        
            Response response = null;
            
            Credenciales.IP_NUEVA = ip;

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
                
                Credenciales.IP_ACTUAL = Credenciales.IP_NUEVA;
                
            }
            catch(Exception ex){
                log.error("Error al modificar la IP");
            }

            try{

                //Guardando en Fichero
                log.info("Preparando para guarda objeto en Archivo");
                ObjectOutputStream guardarFichero = new ObjectOutputStream(new FileOutputStream(FICHERO));
                guardarFichero.writeObject(creden);
                guardarFichero.close();
                log.info("Objeto guardado con exito en el archivo " +FICHERO);

            }
            catch(IOException ex){
                log.error("Error al guardar Objeto en el fichero");
            }
        
        }
        
    }
    
}
