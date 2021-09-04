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
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Usuario
 */
public class ArticuloControl {
    
    private final CategoriaDAO DATOSCAT;
     
     public ArticuloControl(){
         this.DATOSCAT = new CategoriaDAO();
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
    
}
