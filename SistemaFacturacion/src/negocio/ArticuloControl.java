/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import datos.ArticuloDAO;
import datos.CategoriaDAO;
import entidades.Articulo;
import entidades.Categoria;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author brayan
 */
public class ArticuloControl {
    private final CategoriaDAO DATOSCAT;
    private final ArticuloDAO DATOS;
    private Articulo obj;
    private DefaultTableModel modeloTabla;
    private int registrosMostrados;

    public ArticuloControl() {
        this.DATOSCAT = new CategoriaDAO();
        this.DATOS = new ArticuloDAO();
        this.obj = new Articulo();
        this.registrosMostrados = 0;
    }
    
    public DefaultTableModel listar(String texto, int totalPorPagina, int numPagina) {
        try {
            List<Articulo> lista = new ArrayList<>();
            lista.addAll(DATOS.listar(texto, totalPorPagina, numPagina));
            
            String[] titulos = {"Id", "Categoria ID", "Categoria", "Codigo", "Nombre", "Precio Venta", "Sotck"
                                ,"Descripcion", "Imagen", "Estado"};
            
            this.modeloTabla = new DefaultTableModel(null, titulos);
            
            String estado;
            String[] registro = new String[10];
            
            this.registrosMostrados = 0;
            for (Articulo item : lista) {
                if (item.isActivo()) { 
                    estado  = "Activo";
                } else {
                    estado = "Inactivo";
                }
                
                registro[0] = Integer.toString(item.getId());
                registro[1] = Integer.toString(item.getCategoriaId());
                registro[2] = item.getCategoriaNombre();
                registro[3] = item.getCodigo();
                registro[4] = item.getNombre();
                registro[5] = Double.toString(item.getPrecioVenta());
                registro[6] = Integer.toString(item.getStock());
                registro[7] = item.getDespcripcion();
                registro[8] = item.getImagen();
                registro[9] = estado;
                
                this.modeloTabla.addRow(registro);
                this.registrosMostrados =+ 1; 
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ArticuloControl::listar-> " + e.getMessage());
        }
        return this.modeloTabla;
    }
    
    public DefaultComboBoxModel llenarCombobox() {
        DefaultComboBoxModel items = new DefaultComboBoxModel();
        List<Categoria> lista = new ArrayList<>();
        lista = DATOSCAT.llenarCombobox();
        for (Categoria categoria : lista) {
            items.addElement(new Categoria(categoria.getId(), categoria.getNombre()));
        }
        return items;
    }
    
    public String insertar(int categoriaId, String codigo, String nombre, double precioVenta, int stock, String descripcion, String imagen) {
        try {
            System.out.println("hola mundo principio " +categoriaId);
            System.out.println("ENTRANDO"); 
            if (DATOS.existe(nombre)) {
                return "El registro ya existe";
            } else {
                System.out.println("LLEGANDO AQUI 2");
                obj.setCategoriaId(categoriaId);
                System.out.println("hola mundo "+categoriaId);
                obj.setCodigo(codigo);
                System.out.println("PASANDO -> setCodigo");
                obj.setNombre(nombre);
                System.out.println("PASANDO -> setNombre");
                obj.setPrecioVenta(precioVenta);
                System.out.println("PASANDO -> setPrecioVenta");
                obj.setStock(stock);
                System.out.println("PASANDO -> setStock");
                obj.setDespcripcion(descripcion);
                System.out.println("PASANDO -> setDespcripcion");
                obj.setImagen(imagen);
                System.out.println("Despues de obj.setImagen(imagen)");
                if (DATOS.insertar(obj)) {
                    System.out.println("Entrando al OK insertar");
                    return "OK";
                } else {
                    return "Error en el registro";
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ArticuloControl::insertar-> " + e.getMessage());
        }
        return "NULL";
    }

    public String actualizar(int id, int categoriaId, String codigo, String nombre, String nombreAnt, double precioVenta, int stock, String descripcion, String imagen) {
        try {
            if (nombre.equals(nombreAnt)) {
                obj.setId(id);
                obj.setCategoriaId(categoriaId);
                obj.setCodigo(codigo);
                obj.setNombre(nombre);
                obj.setPrecioVenta(precioVenta);
                obj.setStock(stock);
                obj.setDespcripcion(descripcion);
                obj.setImagen(imagen);

                if (DATOS.actualizar(obj)) {
                    return "OK";
                } else {
                    return "Error en la actualizacion";
                }
            } else {
                if (DATOS.existe(nombre)) {
                    return "El registro ya existe";
                } else {
                    obj.setId(id);
                    obj.setCategoriaId(categoriaId);
                    obj.setCodigo(codigo);
                    obj.setNombre(nombre);
                    obj.setPrecioVenta(precioVenta);
                    obj.setStock(stock);
                    obj.setDespcripcion(descripcion);
                    obj.setImagen(imagen);

                    if (DATOS.actualizar(obj)) {
                        return "OK";
                    } else {
                        return "Error en la actualizacion";
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ArticuloControl::actualizar-> " + e.getMessage());
        }
        return "NULL";
    }
    
    public int total() {
        return DATOS.total();
    }
    
    public int totalMostrados() {
        return this.registrosMostrados;
    }
}
