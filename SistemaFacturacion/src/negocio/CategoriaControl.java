/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import datos.CategoriaDAO;
import entidades.Categoria;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author brayan
 */
public class CategoriaControl {
    
    private final CategoriaDAO DATOS;
    private Categoria obj;
    private DefaultTableModel modeloTabla;
    public int registrosMostrados;
    
    public CategoriaControl() {
        this.DATOS = new CategoriaDAO();
        this.obj = new Categoria();
        this.registrosMostrados = 0;
    }
    
    //MVC = Modelo Vista Controlador  - Arquitectura 3 capas
    //Modelo(datos) = Se encarga de comunicarse con la BD
    //Controlador(negocio) = Se encarga de recibir lo de cada Modelo(datos) y comunicarse con la capa presentacion
    //vista(presentacion) = Es la que recibi y envia del Controlador(negocio), y lo que ve el usuario
    
    public String insertar(String nombre, String descripcion) {
        try {
            if (DATOS.existe(nombre)) {
                return "El registro ya existe";
            } else {
                obj.setNombre(nombre);
                obj.setDescripcion(descripcion);
                if (DATOS.insertar(obj)) {
                    return "OK";
                } else {
                    return "Error en el registro";
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "CategoriaControl::insertar-> " + e.getMessage());
        }
        return "";
    }
    


}
