/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocios;

import datos.CategoriaDAO;
import entidades.Categoria;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class CategoriaControl {
    private final CategoriaDAO DATOS;
    private Categoria obj;
    private DefaultTableModel modeloTabla;
    public int registroMostrados;

    public CategoriaControl(){
        this.DATOS = new CategoriaDAO();
        this.obj = new Categoria();
        this.registroMostrados = 0;
    }
    //MVC = Modelo Vista Controlador  - Arquitectura 3 capas
    //Modelo(datos) = Se encarga de comunicarse con la BD
    //Controlador(negocio) = Se encarga de recibir lo de cada Modelo(datos) y comunicarse con la capa presentacion
    //vista(presentacion) = Es la que recibi y envia del Controlador(negocio), y lo que ve el usuario

    public DefaultTableModel listar(String texto){
        try {
            List<Categoria> lista = new ArrayList<>();
        lista.addAll(DATOS.listar(texto));
        System.out.println("LISTA categoriaDAO: "+lista.toString());
        String[] titulos = {"Id", "Nombre", "Describcion", "Estado"}; //Titulos de la tabla
        this.modeloTabla = new DefaultTableModel(null, titulos);
        
        String estado;
        this.registroMostrados = 0;
        String[] registro = new String[4];
        
        for (Categoria item: lista) {
            if (item.isActivo()) {
                estado = "Activo";
            }else{
                estado = "Inactivo";
            }
            registro[0] = Integer.toString(item.getId());
            registro[1] = item.getNombre();
            registro[2] = item.getDescripcion();
            registro[3] = estado;
            this.modeloTabla.addRow(registro);
            this.registroMostrados = this.registroMostrados +1;
    
        }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "CategoriaDAO::insertar-> " + e.getMessage());
        }
        return this.modeloTabla;
    }

    public String insertar(String nombre, String descripcion ){
        try {
            if (DATOS.existe(nombre)) {
                return "El registro ya existe";
            }else{
                obj.setNombre(nombre);
                obj.setDescripcion(descripcion);
                if (DATOS.insertar(obj)) {
                    return "OK";
                }else{
                    return "Error en el registro";
                }
            }
        } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "CategoriaDAO::insertar-> " + e.getMessage());

        }
        return"";
    }
    
    public String actualizar(int id, String nombre, String nombreAnt, String descripcion){
        try {
            if (nombre.equals(nombreAnt)) {
                obj.setId(id);
                obj.setNombre(nombre);
                obj.setDescripcion(descripcion);
                if (DATOS.actualizar(obj)) {
                    return "OK";
                }else{
                    return "Error en la actualizacion";
                }
            }else{
                if (DATOS.existe(nombre)) {
                    return "El registro ya existe";
                }else{
                 obj.setId(id);
                obj.setNombre(nombre);
                obj.setDescripcion(descripcion);
                if (DATOS.actualizar(obj)) {
                    return "OK";
                }else{
                    return "Error en la actualizacion";
                }   
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "CategoriaDAO::actualizar " + e.getMessage());
        }
        return "NULL";
    }
    
    public String desactivar(int id){
        try {
           
            if (DATOS.desactivar(id)) {
                return "OK";
        
            }else{
                return "No se puede desactivar el registro";
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "CategoriaDAO::desactivar-> " + e.getMessage());
        }
        return "NULL";
    }
  
     public String activar(int id){
        try {
           
            if (DATOS.activar(id)) {
                return "OK";
        
            }else{
                return "No se puede activar el registro";
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "CategoriaDAO::activar-> " + e.getMessage());
        }
        return "NULL";
    }
     
     public int total(){
         return DATOS.total();
     }
     
     public int totalMostrados(){
         return this.registroMostrados;
     }

}
