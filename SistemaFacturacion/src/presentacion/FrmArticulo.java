/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentacion;

import entidades.Categoria;
import java.awt.Desktop;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.TableRowSorter;
import negocio.ArticuloControl;

/**
 *
 * @author brayan
 */
public class FrmArticulo extends javax.swing.JInternalFrame {
    private final ArticuloControl CONTROL;
    private String accion; 
    private String nombreAnt; 
    
//    VARIABLES DE PAGINACION
    private int totalPorPagina = 10;
    private int numPagina = 1;
    private boolean primeraCarga = true;
    private int totalRegistros;
    
    //VARIABLES PARA GUARDAR LA IMAGEN
    private String rutaOrigen;
    private String rutaDestino;
    private final String DIRECTORIO = "src/file/articulos/";
    private String imagen = "";
    private String imagenAnt;
    
    
    /**
     * Creates new form FrmArticulo
     */
    public FrmArticulo() {
        initComponents();
        this.CONTROL = new ArticuloControl();
        this.cargarCategorias();
        this.paginar();
        this.listar("", false);
        this.primeraCarga = false;
        tabGeneral.setEnabledAt(1, false);
        txtId.setVisible(false);
        this.accion = "guardar";
    }
    
    private void cargarCategorias() {
        DefaultComboBoxModel items = this.CONTROL.llenarCombobox();
        cboCategoria.setModel(items);
    }
    
    private void paginar() {
        int totalPaginas;
        
        this.totalRegistros = this.CONTROL.total(); //50
        this.totalPorPagina = Integer.parseInt((String)cboTotalPorPagina.getSelectedItem()); //Obtenemos el item del cboTotalPorPagina = 10
        //Ceil redondeado los decimales al entero proximo mayor
        totalPaginas = (int) (Math.ceil((double) this.totalRegistros / this.totalPorPagina)); // 50 / 10 = 5
        
        if (totalPaginas == 0) {
            totalPaginas = 1; //Mostrar una pagina
        }
        cboNumPagina.removeAllItems();
        
        for (int i = 1; i <= totalPaginas; i++) {
            cboNumPagina.addItem(Integer.toString(i));
        }
        cboNumPagina.setSelectedIndex(0);
    }
    
    private void listar(String texto, boolean paginar) {
        //Obteniendo el valor seleccionado por el usuario del combobox cboTotalPorPagina
        this.totalPorPagina = Integer.parseInt((String) cboTotalPorPagina.getSelectedItem()); 
        if ((String) cboNumPagina.getSelectedItem() != null) {
            //Obteniendo el valor seleccionado por el usuario del combobox cboNumPagina
            this.numPagina = Integer.parseInt((String) cboNumPagina.getSelectedItem()); 
        }
        
        if (paginar){
            tablaListado.setModel(this.CONTROL.listar(texto, this.totalPorPagina, this.numPagina)); // totalPorPagina = 10, this.numPagina = 2
        } else {
            tablaListado.setModel(this.CONTROL.listar(texto, this.totalPorPagina, 1));
        }
        TableRowSorter orden = new TableRowSorter(tablaListado.getModel());
        tablaListado.setRowSorter(orden);
        this.ocultarColumnas();
        lblTotalRegistros.setText("Mostrando "+ this.CONTROL.totalMostrados() + " de un total "+ this.CONTROL.total() + " registros");
    }
    
    private void ocultarColumnas() {
        //OCULTANDO LA COLUMNA CATEGORIA ID
        tablaListado.getColumnModel().getColumn(1).setMaxWidth(0);//Seteando un ancho maximo
        tablaListado.getColumnModel().getColumn(1).setMinWidth(0);//Seteando un ancho minimo
        tablaListado.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(0); //Seteando un ancho maximo al titulo Categoria ID
        tablaListado.getTableHeader().getColumnModel().getColumn(1).setMinWidth(0); //Seteando un ancho minimo al titulo Categoria ID
        
        //OCULTANDO LA COLUMNA IMAGEN
        tablaListado.getColumnModel().getColumn(8).setMaxWidth(0);//Seteando un ancho maximo
        tablaListado.getColumnModel().getColumn(8).setMinWidth(0);//Seteando un ancho minimo
        tablaListado.getTableHeader().getColumnModel().getColumn(8).setMaxWidth(0); //Seteando un ancho maximo al titulo Categoria ID
        tablaListado.getTableHeader().getColumnModel().getColumn(8).setMinWidth(0); //Seteando un ancho minimo al titulo Categoria ID
    }
    
    private void pestaniaFrame(String tipo) {
        if (tipo.equalsIgnoreCase("listado")) {
            tabGeneral.setSelectedIndex(0);
            tabGeneral.setEnabledAt(0, true); //Pestaña listado activada
            tabGeneral.setEnabledAt(1, false);//Pestaña Mantenimiento desactivada
        } else {
            tabGeneral.setEnabledAt(1, true); //Pestaña Mantenimiento activada
            tabGeneral.setEnabledAt(0, false); //Pestaña listado desactivada
            tabGeneral.setSelectedIndex(1);
        }
    }
    
    private void limpiar() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtId.setText("");
        txtPrecioVenta.setText("");
        txtStock.setText("");
        lblImagen.setIcon(null);
        this.imagen = "";
        this.imagenAnt = "";
        this.rutaDestino = "";
        this.rutaOrigen = "";
        this.accion = "guardar";
    }
    
    private void mensaje(String mensaje, String tipo) {
        if (tipo.equals("correcto")) {
            JOptionPane.showMessageDialog(this, mensaje, "ProyectoBJ", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, mensaje, "ProyectoBJ", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void subirImagenes() {
        File origen = new File(this.rutaOrigen); 
        File destino = new File(this.rutaDestino);
        try {
            InputStream in = new FileInputStream(origen); //in = Entrada
            OutputStream out = new FileOutputStream(destino);// out = fuera
            byte[] buf = new byte[1024];
            int lectura;
            while ((lectura = in.read(buf)) > 0 ) { //read = leer
                //lectura contiene los byte de la imagen 
                out.write(buf, 0, lectura); //write = escribir, haciendo copia de la imagen
            }
              in.close();
              out.close();
        } catch (Exception e) {
            this.mensaje("FrmArticulo::subirImagenes()-> "+e.getMessage(), "error");
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToggleButton1 = new javax.swing.JToggleButton();
        tabGeneral = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        txtBuscar = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaListado = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        cboTotalPorPagina = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        lblTotalRegistros = new javax.swing.JLabel();
        btnActivar = new javax.swing.JButton();
        btnDesactivar = new javax.swing.JButton();
        btnImagen = new javax.swing.JButton();
        btnVer = new javax.swing.JButton();
        cboNumPagina = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        cboCategoria = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        txtPrecioVenta = new javax.swing.JTextField();
        txtStock = new javax.swing.JTextField();
        txtId = new javax.swing.JTextField();
        lblImagen = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDescripcion = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        btnAgregarImagen = new javax.swing.JButton();

        jToggleButton1.setText("jToggleButton1");

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        jLabel1.setText("Nombre");

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/presentacion/imagenes/Buscar (2).png"))); // NOI18N
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/presentacion/imagenes/nuevo.png"))); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/presentacion/imagenes/actualizar.png"))); // NOI18N
        btnEditar.setText("Editar");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        tablaListado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tablaListado);

        cboNumPagina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboNumPaginaActionPerformed(evt);
            }
        });

        jLabel2.setText("N Pagina");

        cboTotalPorPagina.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "10", "20", "50", "100", "200", "500" }));
        cboTotalPorPagina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTotalPorPaginaActionPerformed(evt);
            }
        });

        jLabel3.setText("Total de registro por pagina");

        lblTotalRegistros.setText("Registros");

        btnActivar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/presentacion/imagenes/cambiar.png"))); // NOI18N
        btnActivar.setText("Activar");
        btnActivar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActivarActionPerformed(evt);
            }
        });

        btnDesactivar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/presentacion/imagenes/apagar (1).png"))); // NOI18N
        btnDesactivar.setText("Desactivar");
        btnDesactivar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDesactivarActionPerformed(evt);
            }
        });

        btnImagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/presentacion/imagenes/imagen.png"))); // NOI18N
        btnImagen.setText("Imagen");
        btnImagen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImagenActionPerformed(evt);
            }
        });

        btnVer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/presentacion/imagenes/boton.png"))); // NOI18N
        btnVer.setText("Ver");
        btnVer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerActionPerformed(evt);
            }
        });

        cboNumPagina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboNumPaginaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel1)
                        .addGap(26, 26, 26)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(btnBuscar)
                        .addGap(18, 18, 18)
                        .addComponent(btnNuevo)
                        .addGap(18, 18, 18)
                        .addComponent(btnEditar)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboNumPagina, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addComponent(cboTotalPorPagina, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(lblTotalRegistros, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jScrollPane1)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addComponent(btnActivar)
                .addGap(18, 18, 18)
                .addComponent(btnDesactivar)
                .addGap(18, 18, 18)
                .addComponent(btnImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnVer, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(btnBuscar)
                    .addComponent(btnNuevo)
                    .addComponent(btnEditar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cboTotalPorPagina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(lblTotalRegistros)
                    .addComponent(cboNumPagina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDesactivar)
                    .addComponent(btnImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnVer)
                    .addComponent(btnActivar))
                .addGap(77, 77, 77))
        );

        tabGeneral.addTab("Listado", jPanel1);

        cboCategoria.setPreferredSize(new java.awt.Dimension(65, 30));
        cboCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCategoriaActionPerformed(evt);
            }
        });

        jLabel4.setText("Categoria(*)");

        txtCodigo.setPreferredSize(new java.awt.Dimension(303, 30));

        txtNombre.setPreferredSize(new java.awt.Dimension(303, 30));

        txtPrecioVenta.setPreferredSize(new java.awt.Dimension(303, 30));

        txtStock.setPreferredSize(new java.awt.Dimension(303, 30));

        txtId.setPreferredSize(new java.awt.Dimension(23, 30));

        txtDescripcion.setColumns(20);
        txtDescripcion.setRows(5);
        jScrollPane2.setViewportView(txtDescripcion);

        jLabel5.setText("Codigo");

        jLabel6.setText("Nombre (*)");

        jLabel7.setText("Precio Venta (*)");

        jLabel8.setText("Stock (*)");

        jLabel9.setText("Imagen");

        jLabel10.setText("Descripcion");

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/presentacion/imagenes/disquete.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/presentacion/imagenes/cancelar.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        jLabel11.setText("(*) Indica que es un campo obligatorio");

        btnAgregarImagen.setText("Imagen");
        btnAgregarImagen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarImagenActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10))
                        .addGap(54, 54, 54)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(45, 45, 45)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(54, 54, 54)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtPrecioVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(cboCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(lblImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(33, 33, 33)
                                        .addComponent(btnAgregarImagen))))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(247, 247, 247)
                        .addComponent(btnGuardar)
                        .addGap(27, 27, 27)
                        .addComponent(btnCancelar)))
                .addContainerGap(220, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addComponent(txtPrecioVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jLabel9))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAgregarImagen)
                            .addComponent(lblImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel10)
                        .addGap(174, 174, 174))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnimagen)
                            .addComponent(lblImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnCancelar))
                .addGap(57, 57, 57))
        );

        tabGeneral.addTab("Mantenimiento", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabGeneral)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabGeneral)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cboCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCategoriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboCategoriaActionPerformed

    private void cboTotalPorPaginaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTotalPorPaginaActionPerformed
        // TODO add your handling code here:
        this.paginar();
    }//GEN-LAST:event_cboTotalPorPaginaActionPerformed

    private void cboNumPaginaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboNumPaginaActionPerformed
        // TODO add your handling code here:
        if (!this.primeraCarga) {
            this.listar("", true);
        }
    }//GEN-LAST:event_cboNumPaginaActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
        this.listar(txtBuscar.getText(), false);
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnAgregarImagenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarImagenActionPerformed
        // TODO add your handling code here:
        JFileChooser file = new JFileChooser();
        int estado = file.showOpenDialog(this); //Abriendo ventana del FileChoser
        if (estado == JFileChooser.APPROVE_OPTION) { //Si el usuario da click en abrir(Open)
            this.imagen = file.getSelectedFile().getName(); //Obtenemos el nombre del archivo
            this.rutaOrigen = file.getSelectedFile().getAbsolutePath(); //Obteniendo la ruta absoluta de la imagen o archivo
            this.rutaDestino = this.DIRECTORIO + this.imagen; //src/file/articulos/nombreImagen.extension
            
            ImageIcon im = new ImageIcon(this.rutaOrigen);
            
            //lblImagen.getWidth() = Obteniendo el ancho del label
            //lblImagen.getHeight() = Obteniendo el altodel label
            //Image.SCALE_DEFAULT = Ajustandose al tamanio del label
            Icon icono = new ImageIcon(im.getImage().getScaledInstance(lblImagen.getWidth(), lblImagen.getHeight(), Image.SCALE_DEFAULT));
            lblImagen.setIcon(icono);
            lblImagen.repaint();
        }
    }//GEN-LAST:event_btnAgregarImagenActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        // TODO add your handling code here:
        this.pestaniaFrame("Mantenimiento");
        this.accion = "guardar";
        btnGuardar.setText("Guardar");
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
        if (txtNombre.getText().length() == 0 || txtNombre.getText().length() > 100) {
            this.mensaje("Debes ingresar un nombre y no debe ser mayor a 100 caracteres, es obligatorio", "error");
            txtNombre.requestFocus();
            return;
        }
        if (txtPrecioVenta.getText().isEmpty()) {
            this.mensaje("Debes ingresar un precio de venta, es obligatorio", "error");
            txtPrecioVenta.requestFocus();
            return;
        }
        if (txtStock.getText().length() == 0 ) {
            this.mensaje("Debes ingresar un stock del articulo, es obligatorio", "error");
            txtStock.requestFocus();
            return;
        }
        if (txtDescripcion.getText().length() > 255 ) {
            this.mensaje("Debes ingresar una descripcion menor o igual a 255 caracteres, es obligatorio", "error");
            txtDescripcion.requestFocus();
            return;
        }
        
        String resp;
        if (this.accion.equals("editar")) {
            //EDITAR
            String imagenActual = "";
            if (this.imagen.equals("")) {
                imagenActual = this.imagenAnt;
            } else {
                imagenActual = this.imagen;
            }
            Categoria seleccionado = (Categoria) cboCategoria.getSelectedItem(); //Obtener el item seleccionado del combobox 
            resp = this.CONTROL.actualizar(Integer.parseInt(txtId.getText()),seleccionado.getId(), txtCodigo.getText(), txtNombre.getText(), nombreAnt, 
                    Double.parseDouble(txtPrecioVenta.getText()), Integer.parseInt(txtStock.getText()), txtDescripcion.getText(),imagenActual);
            if (resp.equals("OK")) {
                if (!this.imagen.equals("")) { //Si el usuario selecciono una imagen 
                    this.subirImagenes();
                }
                this.mensaje("Actualizado correctamente", "correcto");
                this.limpiar();
                this.listar("", false);
                this.pestaniaFrame("Listado");
            } else {
                this.mensaje(resp, "error");
            }
        } else {
            //GUARDAR
            Categoria seleccionado = (Categoria) cboCategoria.getSelectedItem(); //Obtener el item seleccionado del combobox 
            resp = this.CONTROL.insertar(seleccionado.getId(), txtCodigo.getText(), txtNombre.getText(), Double.parseDouble(txtPrecioVenta.getText()), Integer.parseInt(txtStock.getText()),txtDescripcion.getText(), this.imagen);
            System.out.println("ID: "+seleccionado.getId());
            if (resp.equals("OK")) {
                if (!this.imagen.equals("")) { //Si el usuario selecciono una imagen
                    this.subirImagenes();
                }
                this.mensaje("Registrado correctamente", "correcto");
                this.limpiar();
                this.listar("", false);
            } else {
                this.mensaje(resp, "error");
            }
        }
        
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        this.pestaniaFrame("Listado");
        this.limpiar();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnImagenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImagenActionPerformed
        // TODO add your handling code here:
        if (tablaListado.getSelectedRowCount() == 1) {
            String ruta = tablaListado.getValueAt(tablaListado.getSelectedRow(), 8).toString(); //Obteniento el nombre de la imagen
            String path = "\\home\\brayan\\Documentos\\Repositorios\\proyectoBJ\\SistemaFacturacion\\src\\file\\articulos\\";
            if (Desktop.isDesktopSupported()) {
                try {
                    File miArchivo = new File(path+ruta);
                    Desktop.getDesktop().open(miArchivo); //Abrime el archivo
                } catch (IOException e) {
                    this.mensaje("Error al abrir el articulo "+e.getMessage(), "error");
                }
            }
        } else {
            this.mensaje("Debe seleccionar un articulo", "error");
        }
    }//GEN-LAST:event_btnImagenActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        if (tablaListado.getSelectedRowCount() == 1) {
            String id = tablaListado.getValueAt(tablaListado.getSelectedRow(), 0).toString();
            int categoriaId = Integer.parseInt(tablaListado.getValueAt(tablaListado.getSelectedRow(), 1).toString());
            String categoriaNombre = tablaListado.getValueAt(tablaListado.getSelectedRow(), 2).toString();
            String codigo = tablaListado.getValueAt(tablaListado.getSelectedRow(), 3).toString();
            String nombre = tablaListado.getValueAt(tablaListado.getSelectedRow(), 4).toString();
            nombreAnt = tablaListado.getValueAt(tablaListado.getSelectedRow(), 4).toString();
            String precioVenta = tablaListado.getValueAt(tablaListado.getSelectedRow(), 5).toString();
            String stock = tablaListado.getValueAt(tablaListado.getSelectedRow(), 6).toString();
            String descripcion = tablaListado.getValueAt(tablaListado.getSelectedRow(), 7).toString();
            this.imagenAnt = tablaListado.getValueAt(tablaListado.getSelectedRow(), 8).toString();
            
            txtId.setText(id);
            //Para poder obtener el nombre de la categoria seleccionada por el usuario y setearla al combobox
            Categoria seleccionado = new Categoria(categoriaId, categoriaNombre); 
            cboCategoria.setSelectedItem(seleccionado);//seteando al combobox el nombre de la categoria
            txtCodigo.setText(codigo);
            txtNombre.setText(nombre);
            txtPrecioVenta.setText(precioVenta);
            txtStock.setText(stock);
            txtDescripcion.setText(descripcion);
           
            
            //IMPRIMIR LA IMAGEN EN EL LABEL
            ImageIcon im = new ImageIcon(this.DIRECTORIO + this.imagenAnt);
            Icon icono = new ImageIcon(im.getImage().getScaledInstance(lblImagen.getWidth(), lblImagen.getHeight(), Image.SCALE_DEFAULT));
            lblImagen.setIcon(icono);
            lblImagen.repaint(); //Imprimiendo la imagen
            
            this.pestaniaFrame("Mantenimiento");
            this.accion = "editar";
            btnGuardar.setText("Editar");
        } else {
            this.mensaje("Debes seleccionar un arituclo", "error");
        }
        
    }//GEN-LAST:event_btnEditarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActivar;
    private javax.swing.JButton btnAgregarImagen;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnDesactivar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnImagen;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnVer;
    private javax.swing.JButton btnimagen;
    private javax.swing.JComboBox<String> cboCategoria;
    private javax.swing.JComboBox<String> cboNumPagina;
    private javax.swing.JComboBox<String> cboTotalPorPagina;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JLabel lblImagen;
    private javax.swing.JLabel lblTotalRegistros;
    private javax.swing.JTabbedPane tabGeneral;
    private javax.swing.JTable tablaListado;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextArea txtDescripcion;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPrecioVenta;
    private javax.swing.JTextField txtStock;
    // End of variables declaration//GEN-END:variables

}
