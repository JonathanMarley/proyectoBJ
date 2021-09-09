/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos;

import database.Conexion;
import datos.interfaces.IPaginadoInterface;
import entidades.Usuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author
 */
public class UsuarioDAO implements IPaginadoInterface<Usuario> {

    private final Conexion CON;
    private PreparedStatement ps;
    private boolean resp;
    private ResultSet rs;

    public UsuarioDAO() {
        CON = Conexion.getInstancia();
    }

    @Override
    public List<Usuario> listar(String texto, int totalPorPagina, int numPagina) {
        List<Usuario> registros = new ArrayList();
        try {
            ps = CON.conectar().prepareStatement("SELECT u.id, u.rol_id,  u.nombre, u.tipo_documento, u.num_documento, u.direccion, u.telefono, u.email, u.clave, u.activo FROM usuario  WHERE u.nombre LIKE ? ORDER BY u.id ASC LIMIT ?,?");
            ps.setString(1, "%" + texto + "%");
            ps.setInt(2, (numPagina - 1) * totalPorPagina);
            ps.setInt(3, totalPorPagina);
            rs = ps.executeQuery();
            while (rs.next()) {
                registros.add(new Usuario(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getBoolean(11)));

            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "UsuarioDAO::listar->" + e.getMessage());

        } finally {
            ps = null;
            rs = null;
            CON.desconectar();
        }
        return registros;

    }

    @Override
    public boolean insertar(Usuario obj) {
        resp = false;
        try {
            ps = CON.conectar().prepareStatement("INSERT INT usuario (rol_id, nombre,tipo_documento,num_documento,direccion,telefono,email,clave,activo) VALUES (?,?,?,?,?,?,?,?,1)");
            ps.setInt(1, obj.getRolId());
            ps.setString(2, obj.getNombreUsuario());
            ps.setString(3, obj.getTipodocumento());
            ps.setString(4, obj.getNumDocumento());
            ps.setString(5, obj.getDireccion());
            ps.setString(6, obj.getTelefono());
            ps.setString(7, obj.getEmail());
            ps.setString(8, obj.getClave());
            if (ps.executeUpdate() > 0) {
                resp = true;
            }
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "UsuarioDAO::insertar->" + e.getMessage());
        } finally {
            ps = null;
            CON.desconectar();
        }
        return resp;
    }

    @Override
    public boolean actualizar(Usuario obj) {
        resp = false;
        try {
            ps = CON.conectar().prepareStatement("UPDATE usuario SET rol_id=?, nombre=?, tipo_documento=?, num_documento=?, direccion=?, telefono=?, email=?, clave=?, WHERE id=? ");
            ps.setInt(1, obj.getRolId());
            ps.setInt(1, obj.getRolId());
            ps.setString(2, obj.getNombreUsuario());
            ps.setString(3, obj.getTipodocumento());
            ps.setString(4, obj.getNumDocumento());
            ps.setString(5, obj.getDireccion());
            ps.setString(6, obj.getTelefono());
            ps.setString(7, obj.getEmail());
            ps.setString(8, obj.getClave());
            if (ps.executeUpdate() > 0) {
                resp = true;
            }
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "UsuarioDAO::actualizar->" + e.getMessage());
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
            ps = CON.conectar().prepareStatement("UPDATE usuario SET activo=0, WHERE id=?");
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                resp = true;
            }
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "UsuarioDAO::desactivar->" + e.getMessage());
        }
        return resp;
    }

    @Override
    public boolean activar(int id) {
          resp = false;
        try {
            ps = CON.conectar().prepareStatement("UPDATE usuario SET activo=1, WHERE id=?");
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                resp = true;
            }
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "UsuarioDAO::desactivar->" + e.getMessage());
        }
        return resp;
    }
    

    @Override
    public int total() {
        int totalRegistros = 0;
        try {
            ps = CON.conectar().prepareStatement("SELECT COUNT(id)FROM usuario");
            rs = ps.executeQuery();
            
            while (rs.next()) {
                totalRegistros = rs.getInt("COUNT(id)");
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
           JOptionPane.showMessageDialog(null, "UsuarioDAO::total->" + e.getMessage());

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
            ps = CON.conectar().prepareStatement("SELECT email FROM usuario WHERE email=?");
            ps.setString(1, texto);
            rs.last();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "UsuarioDAO::existente->" + e.getMessage());

        }finally{
            ps = null;
            rs = null;
            CON.desconectar();
        }
        return resp;
       
    }

}
