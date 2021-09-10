/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos;

import database.Conexion;
import datos.interfaces.IPaginadoInterface;
import entidades.Articulo;
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
public class ArticuloDao implements IPaginadoInterface<Articulo> {

    private final Conexion CON;
    private PreparedStatement ps;
    private ResultSet rs;
    private boolean resp;

    public ArticuloDao() {
        CON = Conexion.getInstancia();
    }

    @Override
    public List<Articulo> listar(String texto, int totalPorPagina, int numPagina, String tipoPersona) {
        List<Articulo> registros = new ArrayList<>();//Metiendo toda la data que viene desde la BD en la lista
        try {
            ps = CON.conectar().prepareStatement("SELECT A.id, A.categoria_id, C.nombre as categoria_nombre, A.codigo, A.nombre, A.precio_venta, A.stock, A.descripcion, A.imagen, "
                    + "A.activo FROM articulo A INNER JOIN categoria C ON A.categoria_id = C.id WHERE A.nombre LIKE ? ORDER BY A.id ASC LIMIT ?,?");
            ps.setString(1, "%" + texto + "%");
            ps.setInt(2, (numPagina - 1) * totalPorPagina);
            ps.setInt(3, totalPorPagina);
            rs = ps.executeQuery();

            while (rs.next()) { //Ejecutate mientras encuentres un registros
                registros.add(new Articulo(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getDouble(6), rs.getInt(7), rs.getString(8), rs.getString(9), rs.getBoolean(10)));

            }
            //System.out.println("LISTA categoriaDAO: "+registros.toString());
            ps.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ArticuloDao::listar->" + e.getMessage());

        } finally {

            ps = null;
            rs = null;
            CON.desconectar();
        }
        return registros;
    }

    @Override
    public boolean insertar(Articulo obj) {
        resp = false;
        try {
            ps = CON.conectar().prepareStatement("INSERT INTO articulo (categoria_id, codigo,nombre, precio_venta, stock, descripcion, imagen, activo) "
                    + "VALUES (?,?,?,?,?,?,?,1)");
            ps.setInt(1, obj.getCategoriaId());
            ps.setString(2, obj.getCodigo());
            ps.setString(3, obj.getNombre());
            ps.setDouble(4, obj.getPrecioVenta());
            ps.setInt(5, obj.getStock());
            ps.setString(6, obj.getDespcricion());
            ps.setString(7, obj.getImagen());

            if (ps.executeUpdate() > 0) {
                resp = true;
            }
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ArticuloDao::insertar->" + e.getMessage());

        } finally {//Todo lo que esta dentro del finally se ejecutar haya o no un error
            ps = null;
            CON.desconectar();
        }
        return resp;

    }

    @Override
    public boolean actualizar(Articulo obj) {
        resp = false;
        try {
            ps = CON.conectar().prepareStatement("UPDATE articulo SET categoria_id=?, codigo = ?, nombre = ?, precio_venta = ?, stock = ?, descripcion= ?, imagen = ? WHERE id = ?");
            ps.setInt(1, obj.getCategoriaId());
            ps.setString(2, obj.getCodigo());
            ps.setString(3, obj.getNombre());
            ps.setDouble(4, obj.getPrecioVenta());
            ps.setInt(5, obj.getStock());
            ps.setString(6, obj.getDespcricion());
            ps.setString(7, obj.getImagen());
            ps.setInt(8, obj.getId());

            if (ps.executeUpdate() > 0) { // Es ´porque se actualizo correctamente
                resp = true;
            }
            ps.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ArticuloDao::actualizar->" + e.getMessage());
        } finally {
            ps = null;
            CON.desconectar();
        }
        return resp;
    }

    @Override
    public boolean desactivar(int id) {
        resp = false;
        try {
            ps = CON.conectar().prepareStatement("UPDATE articulo SET activo=0 WHERE id =?");
            ps.setInt(1, id);

            if (ps.executeUpdate() > 0) { // Es ´porque se actualizo correctamente
                resp = true;
            }
            ps.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ArtticuloDao::desactivar->" + e.getMessage());
        } finally {
            ps = null;
            CON.desconectar();
        }
        return resp;
    }

    @Override
    public boolean activar(int id) {
        resp = false;
        try {
            ps = CON.conectar().prepareStatement("UPDATE articulo SET activo=1 WHERE id =?");
            ps.setInt(1, id);
            
            if (ps.executeUpdate() > 0) { // Es ´porque se actualizo correctamente
                resp = true;
            }
            ps.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ArticuloDao::activar->" + e.getMessage());
        } finally {
            ps = null;
            CON.desconectar();
        }
        return resp;
    }

    @Override
    public int total() {
        int totalRegistros = 0;
        try {
            ps = CON.conectar().prepareStatement("SELECT COUNT(id) as TOTAL_REGISTROS FROM articulo");
            rs = ps.executeQuery();

            while (rs.next()) {
                totalRegistros = rs.getInt("TOTAL_REGISTROS");
            }

            ps.close();
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ArticuloDAO::total-> " + e.getMessage());
        } finally {
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
            ps = CON.conectar().prepareStatement("SELECT nombre FROM articulo WHERE nombre = ?");
            ps.setString(1, texto);
            rs = ps.executeQuery();
            rs.last();
            if (rs.getRow() > 0) {
                resp = true;
            }
            ps.close();
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ArticuloDao::existe-> " + e.getMessage());
        } finally {
            ps = null;
            rs = null;
            CON.desconectar();
        }
        return resp;
    }
}
