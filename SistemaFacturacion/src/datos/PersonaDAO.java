/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos;

import database.Conexion;
import datos.interfaces.IPaginadoInterface;
import entidades.Persona;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class PersonaDAO implements IPaginadoInterface<Persona> {
    
    private final Conexion CON;
    private PreparedStatement ps;
    private ResultSet rs;
    private boolean resp;
    
    public PersonaDAO(){
        CON = Conexion.getInstancia();
    }

    @Override
    public List<Persona> listar(String texto, int totalPorPagina, int numPaginas) {
        List<Persona> registros = new ArrayList();
        try {
            ps = CON.conectar().prepareStatement("SELECT P.id, P.tipo_persona, P.nombre, P.tipo_documento, P.num_documento, "
             + "P.direccion, P.telefono, P.email, P.activo FROM persona P  WHERE P.nombre LIKE ?  ORDER BY P.id ASC LIMIT ?,?"); 
            ps.setString(1, "%" + texto + "%");
            
            ps.setInt(2,(numPaginas - 1) * totalPorPagina);
            ps.setInt(3, totalPorPagina);
           
            rs = ps.executeQuery();
            
            while (rs.next()) {
                registros.add(new Persona(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getBoolean(9)));
            }
            ps.close();
            rs.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "PersonaDao::insertar->" + e.getMessage());
        }finally{
            ps = null;
            rs = null;
            CON.desconectar();
        }
        return registros;
    }
    

    
    
    

    @Override
    public boolean insertar(Persona obj) {
        resp = false;
        try {
            ps = CON.conectar().prepareStatement("INSERT INTO persona (tipo_persona, nombre, tipo_documento, num_documento, direccion, "
            + "telefono, email, activo) VALUES (?,?,?,?,?,?,?,1)");
            ps.setString(1, obj.getTipoPersona());
            ps.setString(2, obj.getNombre());
            ps.setString(3, obj.getTipoDocumento());
            ps.setString(4, obj.getNumDocumento());
            ps.setString(5, obj.getDireccion());
            ps.setString(6, obj.getTelefono());
            ps.setString(7, obj.getEmail());
            if (ps.executeUpdate() > 0) {
                resp = true;
            }
            ps.close();
 
        } catch (SQLException e) {
             JOptionPane.showMessageDialog(null, "PersonaDAO::insertar->" + e.getMessage());
        }finally{
            ps = null;
            CON.desconectar();
        }
        return resp;
        
    }

    @Override
    public boolean actualizar(Persona obj) {
        resp = false;
        try {
            ps = CON.conectar().prepareStatement("UPDATE persona  SET tipo_persona=?, nombre=?, tipo_documento=?, num_documento=?, direccion=?, telefono=?, email=? WHERE id=? ");
            ps.setString(1, obj.getTipoPersona());
            ps.setString(2, obj.getNombre());
            ps.setString(3, obj.getTipoDocumento());
            ps.setString(4, obj.getNumDocumento());
            ps.setString(5, obj.getDireccion());
            ps.setString(6, obj.getTelefono());
            ps.setString(7, obj.getEmail());
            ps.setInt(8, obj.getId());
            
            if (ps.executeUpdate() >0) {
                resp = true;
            }
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }finally{
            ps = null;
            CON.desconectar();
        }
         return resp;
    }

    @Override
    public boolean desactivar(int id) {
        resp = false;
        
        try {
            ps = CON.conectar().prepareStatement("UPDATE persona SET activo=0 WHERE id=?");
            ps.setInt(1,id);
            if (ps.executeUpdate() > 0 ) {
                resp = true;
            }
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }finally{
           ps = null;
           CON.desconectar();
       }
            return resp;
    }

    @Override
    public boolean activar(int id) {
        resp = false;
        
       try {
            ps = CON.conectar().prepareStatement("UPDATE persona SET activo=1 WHERE id=?");
            ps.setInt(1,id);
            if (ps.executeUpdate() > 0 ) {
                resp = true;
            }
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }finally{
           ps = null;
           CON.desconectar();
       }
            return resp;
    }

    @Override
    public int total() {
        int totalRegistros = 0;
        try {
        ps = CON.conectar().prepareStatement("SELECT COUNT(id) FROM persona");
        rs = ps.executeQuery();
        
            while (rs.next()) {
                totalRegistros = rs.getInt("COUNT(id)");
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "PersonaDAO::total->" + e.getMessage());
        }finally{
        ps = null;
        rs = null;
        CON.desconectar();
    }
        return totalRegistros;
    }
    
    
        
        
    @Override
    public boolean existe(String texto) {
        resp = false;
        try {
            ps = CON.conectar().prepareStatement("SELECT nombre FROM persona WHERE nombre=?");
            ps.setString(1, texto);
            rs = ps.executeQuery();
            rs.last();
            if (rs.getRow() > 0) {
                resp = true;
            }
            ps.close();
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "PersonaDAO::existe-> " + e.getMessage());
        } finally {
            ps = null;
            rs = null;
            CON.desconectar();
        }
        return resp;
    }
    
}
