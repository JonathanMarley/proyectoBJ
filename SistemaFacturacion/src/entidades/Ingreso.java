/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.sql.Date;
import java.util.List;

/**
 *
 * @author 10bra
 */
public class Ingreso {
    private int id;
    private int usuarioId;
    private String usarioNombre;
    private int personaId;
    private String personaNombre;
    private String tipoComprobante;
    private String numComprobante;
    private Date fecha;
    private double impuesto;
    private double total;
    private String estado;
    private List<DetalleIngreso> detalles; //HACE REFERENICA A LA TABLA detalle_ingreso

    public Ingreso() {
    }

    public Ingreso(int id, int usuarioId, String usarioNombre, int personaId, String personaNombre, String tipoComprobante, String numComprobante, Date fecha, double impuesto, double total, String estado, List<DetalleIngreso> detalles) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.usarioNombre = usarioNombre;
        this.personaId = personaId;
        this.personaNombre = personaNombre;
        this.tipoComprobante = tipoComprobante;
        this.numComprobante = numComprobante;
        this.fecha = fecha;
        this.impuesto = impuesto;
        this.total = total;
        this.estado = estado;
        this.detalles = detalles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsarioNombre() {
        return usarioNombre;
    }

    public void setUsarioNombre(String usarioNombre) {
        this.usarioNombre = usarioNombre;
    }

    public int getPersonaId() {
        return personaId;
    }

    public void setPersonaId(int personaId) {
        this.personaId = personaId;
    }

    public String getPersonaNombre() {
        return personaNombre;
    }

    public void setPersonaNombre(String personaNombre) {
        this.personaNombre = personaNombre;
    }

    public String getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(String tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public String getNumComprobante() {
        return numComprobante;
    }

    public void setNumComprobante(String numComprobante) {
        this.numComprobante = numComprobante;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(double impuesto) {
        this.impuesto = impuesto;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<DetalleIngreso> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleIngreso> detalles) {
        this.detalles = detalles;
    } 

    @Override
    public String toString() {
        return "Ingreso{" + "id=" + id + ", usuarioId=" + usuarioId + ", usarioNombre=" + usarioNombre + ", personaId=" + personaId + ", personaNombre=" + personaNombre + ", tipoComprobante=" + tipoComprobante + ", numComprobante=" + numComprobante + ", fecha=" + fecha + ", impuesto=" + impuesto + ", total=" + total + ", estado=" + estado + ", detalles=" + detalles + '}';
    }
    
}
