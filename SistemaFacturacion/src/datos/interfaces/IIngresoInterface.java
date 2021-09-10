/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos.interfaces;

import java.util.List;

/**
 *
 * @author Usuario
 */
public interface IIngresoInterface<T, D> {
    public List<T> listar(String texto, int totalPorPagina, int numPagina);
    public List<D> listaDetalle(int id);
    public boolean insertar(T obj);
    public boolean anular(int id);
    public int total();
    public boolean existen(String texto1, String texto2);
}
