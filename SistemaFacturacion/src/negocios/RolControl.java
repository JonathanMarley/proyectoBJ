/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocios;

import datos.RolDAO;
import entidades.Rol;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class RolControl {
    
    private final RolDAO DATOS;
    private Rol obj;
    private DefaultTableModel modeloTabla;
    public int registrosMostrados;

    public RolControl(RolDAO DATOS) {
        this.DATOS = DATOS;
        this.obj = new Rol();
        this.registrosMostrados = 0;
    }
    
    public DefaultTableModel listar(){
        try {
            List<Rol> lista = new ArrayList();
        lista.addAll(DATOS.listar());
        
        String[] titulos = {"Id", "Nombre","Descripcion"};
        this.modeloTabla = new DefaultTableModel(null, titulos);
        
        String[] registro = new String[3];
        this.registrosMostrados = 0;
        
        for (Rol item : lista) {
            registro[0] = Integer.toString(item.getId());
            registro[1] = item.getNombre();
            registro[2] = item.getDescripcion();
            this.modeloTabla.addRow(registro);
            this.registrosMostrados = this.registrosMostrados + 1;
        }
        return this.modeloTabla;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "RolUsuarioControl::listar-> " + e.getMessage());
        }finally{
            return this.modeloTabla;
        }
        
        
    }
    
    public int total(){
        return DATOS.total();
    }
    
    public int totalMostrando(){
        return this.registrosMostrados;
    }
    
}
