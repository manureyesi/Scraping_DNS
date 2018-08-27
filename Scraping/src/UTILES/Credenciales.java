/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UTILES;

import java.io.Serializable;

/**
 *
 * @author ManureyesI
 */
public class Credenciales implements Serializable{
    
    public String USUARIO;
    public String CONTRASENA;
    
    public String URL;
    
    public String DOMINIO;
    
    public String IP_NUEVA;
    public String IP_ACTUAL;
    
    public String NOMBRE_HOST;
    public String TIPO_ZONA;
    
    public Credenciales(){
        
        IP_NUEVA = "";
        IP_ACTUAL = "1.1.1.4";
        USUARIO = "";
        CONTRASENA = "";
        URL = "https://panel.dinahosting.com";
        DOMINIO = "fiandeira.es";
        NOMBRE_HOST = "prueba";
        TIPO_ZONA = "A";
        
    }

    public String getUSUARIO() {
        return USUARIO;
    }

    public void setUSUARIO(String USUARIO) {
        this.USUARIO = USUARIO;
    }

    public String getCONTRASENA() {
        return CONTRASENA;
    }

    public void setCONTRASENA(String CONTRASENA) {
        this.CONTRASENA = CONTRASENA;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getDOMINIO() {
        return DOMINIO;
    }

    public void setDOMINIO(String DOMINIO) {
        this.DOMINIO = DOMINIO;
    }

    public String getIP_NUEVA() {
        return IP_NUEVA;
    }

    public void setIP_NUEVA(String IP_NUEVA) {
        this.IP_NUEVA = IP_NUEVA;
    }

    public String getIP_ACTUAL() {
        return IP_ACTUAL;
    }

    public void setIP_ACTUAL(String IP_ACTUAL) {
        this.IP_ACTUAL = IP_ACTUAL;
    }

    public String getNOMBRE_HOST() {
        return NOMBRE_HOST;
    }

    public void setNOMBRE_HOST(String NOMBRE_HOST) {
        this.NOMBRE_HOST = NOMBRE_HOST;
    }

    public String getTIPO_ZONA() {
        return TIPO_ZONA;
    }

    public void setTIPO_ZONA(String TIPO_ZONA) {
        this.TIPO_ZONA = TIPO_ZONA;
    }
    
    
}
