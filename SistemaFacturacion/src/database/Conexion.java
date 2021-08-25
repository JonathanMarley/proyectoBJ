/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */

public class Conexion {
    public static void main(String[] args) {
        System.out.println("Hola");
    }
}
 public void desconectar(){
     try {
         this.cadena.close();
     } catch (SQLException e) {
         JOptionPane.showMessageDialog(null, "CONEXION:: desconectar-> "+e.getMessage());
         
     }
 }
 public synchronized static Conexion getInstancia(){
     if (instancia == null) {
         instancia = new Conexion();
     }
     return instancia;
 }
        
        
        
}

