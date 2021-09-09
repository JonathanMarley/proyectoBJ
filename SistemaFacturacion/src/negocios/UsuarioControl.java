/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocios;

import datos.RolDAO;
import datos.UsuarioDAO;
import entidades.Usuario;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class UsuarioControl {

    private final UsuarioDAO DATOS;
    // private final RolDAO DATOSROL;
    private Usuario obj;
    private DefaultTableModel modeloTabla;
    public int registrosMostrados;

    public UsuarioControl() {
        this.DATOS = new UsuarioDAO();
        //this.DATOSROL = new RolDAO();
        this.obj = new Usuario();
        this.registrosMostrados = 0;

    }

    public DefaultTableModel listar(String texto, int totalPorPagina, int numPagina) {
        try {
            List<Usuario> lista = new ArrayList();
            lista.addAll(DATOS.listar(texto, totalPorPagina, numPagina));

            String[] titulos = {"Id", "Rol ID", "Usuario", "DOcumento", "Num Documento", "Direccion", "Telefono", "Email", "Clave", "Estado"};
            this.modeloTabla = new DefaultTableModel(null, titulos);

            String estado;
            String[] registros = new String[11];
            this.registrosMostrados = 0;
            for (Usuario item : lista) {
                if (item.isActivo()) {
                    estado = "Activo";
                } else {
                    estado = "inactivo";
                }

                registros[0] = Integer.toString(item.getId());
                registros[1] = Integer.toString(item.getRolId());
                registros[2] = item.getNombre();
                registros[3] = item.getNombreUsuario();
                registros[4] = item.getTipodocumento();
                registros[5] = item.getNumDocumento();
                registros[6] = item.getDireccion();
                registros[7] = item.getTelefono();
                registros[8] = item.getEmail();
                registros[9] = item.getClave();
                registros[10] = estado;
                this.modeloTabla.addRow(registros);
                this.registrosMostrados = this.registrosMostrados + 1;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "UsuarioControl::listar-> " + e.getMessage());
        }
        return this.modeloTabla;
    }

//    private static String encriptar(String Pass){
//        MessageDigest pas;
//        
////        try {
////            pas = MessageDigest.getInstance("SHA-256");
////        } catch (NoSuchAlgorithmException e) {
////            return null;
////        }
//    }
    public String insertar(int rolId, String nombre, String tipoDocumento, String numDocumento, String direccion, String telefono, String email, String clave) {
        try {

            if (DATOS.existe(email)) {
                return "El registro ya existe.";
            } else {
                obj.setRolId(rolId);
                obj.setNombreUsuario(nombre);
                obj.setTipodocumento(tipoDocumento);
                obj.setNumDocumento(numDocumento);
                obj.setDireccion(direccion);
                obj.setTelefono(telefono);
                obj.setEmail(email);
                obj.setClave(clave);
                if (DATOS.insertar(obj)) {
                    return "OK";
                } else {
                    return "Error en el registro.";
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "UsuarioControl::insertar-> " + e.getMessage());
        }
        return "";
         
         
           

    public String actualizar(int id, int rolId, String nombre, String tipoDocumento, String numDocumento, String direccion, String telefono, String email, String emailAnt, String clave) {
        try {
            if (email.equals(emailAnt)) {
                obj.setId(id);
                obj.setRolId(rolId);
                obj.setNombreUsuario(nombre);
                obj.setTipodocumento(tipoDocumento);
                obj.setNumDocumento(numDocumento);
                obj.setDireccion(direccion);
                obj.setTelefono(telefono);
                obj.setEmail(email);
                 obj.setClave(clave);
                if (DATOS.actualizar(obj)) {
                    return "OK";
                } else {
                    return "Error en la actualizacion";
                }
            } else {
               if (DATOS.existe(email)) {
                return "El registro ya existe.";
            } else {
                obj.setId(id);
                obj.setRolId(rolId);
                obj.setNombreUsuario(nombre);
                obj.setTipodocumento(tipoDocumento);
                obj.setNumDocumento(numDocumento);
                obj.setDireccion(direccion);
                obj.setTelefono(telefono);
                obj.setEmail(email);
                 obj.setClave(clave);
                    if (DATOS.actualizar(obj)) {
                        return "OK";
                    } else {
                        return "Error en la actualizacion";
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "UsuarioControl::actualizar " + e.getMessage());
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
            JOptionPane.showMessageDialog(null, "UsuarioControl::desactivar-> " + e.getMessage());
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
            JOptionPane.showMessageDialog(null, "UsuarioControl::activar-> " + e.getMessage());
        }
        return "NULL";
    }
     
     public int total(){
         return DATOS.total();
     }
     
     public int totalMostrados(){
         return this.registrosMostrados;
     }


}
