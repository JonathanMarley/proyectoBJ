/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocios;

import datos.ArticuloDao;
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
 * @author Usuario
 */
public class ArticuloControl {
    
    private final CategoriaDAO DATOSCAT;
    private final ArticuloDao DATOS;
    private Articulo obj;
    private DefaultTableModel modelTabla;
    private int registrosMostrados;
    private DefaultTableModel modeloTabla;
    private int registroMostrados;
    
     
     public ArticuloControl(){
         this.DATOSCAT = new CategoriaDAO();
         this.DATOS = new ArticuloDao();
     }
     
      public DefaultTableModel listar(String texto, int totalPorPagina, int numPagina){
        try {
            
            List<Articulo> lista = new ArrayList<>();
             System.out.println("mundo");
            lista.addAll(DATOS.listar(texto, totalPorPagina, numPagina));
            System.out.println("paso");
        
        String[] titulos = {"Id", "Categoria ID", "Categoria", "Codigo", "Nombre", "Precio Venta", "Sotck", "Descripcion", "Imagen", "Estado"}; //Titulos de la tabla
        this.modeloTabla = new DefaultTableModel(null, titulos);
        
        String estado;
        this.registroMostrados = 0;
        String[] registro = new String[10];
        
        for (Articulo item: lista) {
            if (item.isActivo()) {
                estado = "Activo";
            }else{
                estado = "Inactivo";
            }
            registro[0] = Integer.toString(item.getId());
            registro[1] = Integer.toString(item.getCategoriaId());
            registro[2] = item.getCategoriaNombre();
            registro[3] = item.getCodigo();
            registro[4] = item.getNombre();
            registro[5] = Double.toString(item.getPrecioVenta());
            registro[6] = Integer.toString(item.getStock());
            registro[7] = item.getDescripcion();
            registro[8] = item.getImagen();
            registro[9] = estado;
            
            
            this.modeloTabla.addRow(registro);
            this.registroMostrados = this.registroMostrados +1;
            
    
        }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ArticuloControl::listar-> " + e.getMessage());
        }
          System.out.println("HOLA");
        return this.modeloTabla;
    }

     
     public DefaultComboBoxModel llenarCombobox(){
         DefaultComboBoxModel items = new DefaultComboBoxModel();
         List<Categoria>lista = new ArrayList<>();
         lista = DATOSCAT.llenarCombobox();
         for (Categoria categoria : lista) {
             items.addElement(new Categoria(categoria.getId(), categoria.getNombre()));
         }
         return items;
     }
      public int total(){
         return DATOS.total();
     }
     
     public int totalMostrados(){
         return this.registroMostrados;
     }
    
}
