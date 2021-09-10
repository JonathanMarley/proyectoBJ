/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocios;

import datos.PersonaDAO;
import entidades.Persona;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class PersonaControl {

    private final PersonaDAO DATOS;
    private Persona obj;
    private DefaultTableModel modelTabla;
    private int registroMostrados;
    private DefaultTableModel modeloTabla;
    //private DefaultTableModel modelTabla;

    public PersonaControl() {
        this.DATOS = new PersonaDAO();
        //this.registrosMostrados = 0;
        this.obj = new Persona();
        this.registroMostrados = 0;
    }

    public DefaultTableModel listar(String texto, int totalPorPagina, int numPagina) {
        try {
            List<Persona> lista = new ArrayList();
            lista.addAll(DATOS.listar(texto, totalPorPagina, numPagina));

            String[] titulos = {"Id", "Tipo_Persona", "Nombre", "Documento", "N°Dcumento", "Direccion", "Telefono", "Email", "Estado"};
            this.modelTabla = new DefaultTableModel(null, titulos);

            String estado;
            this.registroMostrados = 0;
            String[] registro = new String[9];

            
            for (Persona item : lista) {
                if (item.isActivo()) {
                    estado = "Activo";
                } else {
                    estado = "Inactivo";
                }
                registro[0] = Integer.toString(item.getId());
                registro[1] = item.getTipoPersona();
                registro[2] = item.getNombre();
                registro[3] = item.getTipoDocumento();
                registro[4] = item.getNumDocumento();
                registro[5] = item.getDireccion();
                registro[6] = item.getTelefono();
                registro[7] = item.getEmail();
                registro[8] = estado;
                
                this.modelTabla.addRow(registro);
                this.registroMostrados = this.registroMostrados + 1;//CONTADOR

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "PersonaControl::listar-> " + e.getMessage());
        }
        return this.modelTabla;
    }

    public String insertar(String tipoPersona, String nombre, String tipoDocumentos, String numDocumentos, String direccion, String telefono, String email) {
        try {
            if (DATOS.existe(nombre)) {
                System.out.println("Proveedor");
                return "El registro ya existe.";
            } else {
                obj.setTipoPersona(tipoPersona);
                obj.setNombre(nombre);
                obj.setTipoDocumento(tipoDocumentos);
                obj.setNumDocumento(numDocumentos);
                obj.setDireccion(direccion);
                obj.setTelefono(telefono);
                obj.setEmail(email);
                if (DATOS.insertar(obj)) {
                    return "OK";
                } else {
                    return "ERROR en el registro.";
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "personaDAO::insertar-> " + e.getMessage());
        }
        return "";
    }

    public String actualizar(int id, String tipoPersona, String nombre, String nombreAnt, String tipoDocumentos, String numDocumento, String direccion, String telefono, String email) {
        try {
            if (nombre.equals(nombreAnt)) {
                obj.setId(id);
                obj.setTipoPersona(tipoPersona);
                obj.setNombre(nombre);
                obj.setTipoDocumento(tipoDocumentos);
                obj.setNumDocumento(numDocumento);
                obj.setDireccion(direccion);
                obj.setTelefono(telefono);
                obj.setEmail(email);

                if (DATOS.actualizar(obj)) {
                    return "OK";
                } else {
                    return "ERROR en la actualizacion.";
                }
                } else {
                if (DATOS.existe(nombre)) {
                    return "El registro ya existe";
                } else{
                 obj.setId(id);
                obj.setTipoPersona(tipoPersona);
                obj.setNombre(nombre);
                obj.setTipoDocumento(tipoDocumentos);
                obj.setNumDocumento(numDocumento);
                obj.setDireccion(direccion);
                obj.setTelefono(telefono);
                obj.setEmail(email);
                
                if (DATOS.actualizar(obj)) {
                    return "OK";
                }else{
                    return "Error en la actualizacion";
                }   
                }
            }
        }catch(Exception e) {
         JOptionPane.showMessageDialog(null, "personaControl::actualizar " + e.getMessage());
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
            JOptionPane.showMessageDialog(null, "persona::desactivar-> " + e.getMessage());
        }
        return "NULL";
    }
    
    public String activar(int id){
        if (DATOS.activar(id)) {
            return "OK";
        }else{
            return "No se puede activar el registro";
        }
    }
    
    public int total(){
        return DATOS.total();
    }
    
    public int totalMostrados(){
        return this.registroMostrados;
    }
    

}

