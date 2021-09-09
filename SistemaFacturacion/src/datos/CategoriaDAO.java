/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos;

import database.Conexion;
import datos.interfaces.ISimpleInterface;
import entidades.Categoria;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author brayan
 */
public class CategoriaDAO implements ISimpleInterface<Categoria> {

    private final Conexion CON;
    private PreparedStatement ps;
    private ResultSet rs;
    private boolean resp;

    public CategoriaDAO() {
        CON = Conexion.getInstancia();
    }

    @Override
    public List<Categoria> listar(String texto) { //Listar y Buscar
        List<Categoria> registros = new ArrayList<>(); //Metiendo toda la data que viene desde la BD en la lista
        try {
            ps = CON.conectar().prepareStatement("SELECT * FROM categoria WHERE nombre LIKE ?");
            ps.setString(1, "%" + texto + "%"); //u
            rs = ps.executeQuery();
            while (rs.next()) { //Ejecutate mientras encontres un registros
                registros.add(new Categoria(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getBoolean(4)));
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "CategoriaDAO::listar-> " + e.getMessage());
        } finally {
            ps = null;
            rs = null;
            CON.desconectar();
        }
        return registros; //Retornarmos todos nuestros registros de la BD
    }
    
    public List<Categoria> llenarCombobox() {
        List<Categoria> registros = new ArrayList<>(); 
        try {
            ps = CON.conectar().prepareStatement("SELECT id, nombre FROM categoria ORDER BY nombre ASC");
            rs = ps.executeQuery();
            while (rs.next()) { 
                registros.add(new Categoria(rs.getInt(1), rs.getString(2)));
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "CategoriaDAO::llenarCombobox-> " + e.getMessage());
        } finally {
            ps = null;
            rs = null;
            CON.desconectar();
        }
        return registros; 
    } 

    @Override
    public boolean insertar(Categoria obj) {
        resp = false;
        try {
            ps = CON.conectar().prepareStatement("INSERT INTO categoria (nombre, descripcion, activo) VALUES (?,?,1)");
            ps.setString(1, obj.getNombre());
            ps.setString(2, obj.getDescripcion());
            if (ps.executeUpdate() > 0) {
                resp = true;
            }
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "CategoriaDAO::insertar-> " + e.getMessage());
        } finally { //Todo lo que esta dentro del finally se ejecuta haya o no un error
            ps = null;
            CON.desconectar();
        }

        return resp;
    }

    @Override
    public boolean actualizar(Categoria obj) {
        resp = false;
        try {
            ps = CON.conectar().prepareStatement("UPDATE categoria SET nombre=?, descripcion=? WHERE id = ?");
            ps.setString(1, obj.getNombre());
            ps.setString(2, obj.getDescripcion());
            ps.setInt(3, obj.getId());
            if (ps.executeUpdate() > 0) { //Es porque se actualizo correctamente
                resp = true;
            }
            ps.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "CategoriaDAO::actualizar-> " + e.getMessage());
        } finally {
            ps = null;
            CON.desconectar();
        }
        return resp;
    }
        return resp;
    }

    @Override
    public boolean desactivar(int id) {
        resp = false;
        try {
            ps = CON.conectar().prepareStatement("UPDATE categoria SET activo=0 WHERE id = ?");
            ps.setInt(1, id);
            
            if (ps.executeUpdate() > 0) { //Es porque se actualizo correctamente
                resp = true;
            }
            ps.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "CategoriaDAO::desactivar-> " + e.getMessage());
        } finally {
            ps = null;
            CON.desconectar();
        }
        return resp;
    }
        return resp;
    }

    @Override
    public boolean activar(int id) {
        resp = false;
        try {
            ps = CON.conectar().prepareStatement("UPDATE categoria SET activo=1 WHERE id = ?");
            ps.setInt(1, id);
            
            if (ps.executeUpdate() > 0) { //Es porque se actualizo correctamente
                resp = true;
            }
            ps.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "CategoriaDAO::desactivar-> " + e.getMessage());
        } finally {
            ps = null;
            CON.desconectar();
        }
        return resp;
    }
        return resp;
    }
    

    @Override
    public int total() {
        int totalRegistros = 0;
        try {
            ps = CON.conectar().prepareStatement("SELECT COUNT(id) as TOTAL_REGISTROS FROM categoria");
            rs = ps.executeQuery();
            
            while (rs.next()) {
                totalRegistros = rs.getInt("TOTAL_REGISTROS");
            }
            
            ps.close();
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "CategoriaDAO::total-> " + e.getMessage());
        } finally {
            ps = null;
            rs = null;
            CON.desconectar();
        }
        return totalRegistros;
    }
        return totalRegistros;
    }

    @Override
    public boolean existe(String texto) {
        resp = false;
        try {
            ps = CON.conectar().prepareStatement("SELECT nombre FROM categoria WHERE nombre = ?");
            ps.setString(1, texto);
            rs = ps.executeQuery();
            rs.last();
            if (rs.getRow() > 0) {
                resp = true;
            }
            ps.close();
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "CategoriaDAO::existe-> " + e.getMessage());
        } finally {
            ps = null;
            rs = null;
            CON.desconectar();
        }
        return resp;
    }

}
