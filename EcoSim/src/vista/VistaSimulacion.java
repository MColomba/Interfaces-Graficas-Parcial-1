/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;
import modelo.Ecosistema;
import modelo.Clima;
import javax.swing.table.DefaultTableModel;
import modelo.Planta;
import modelo.Conejo;
import modelo.Lobo;
import controlador.ControladorSimulacionAPI;

/**
 *
 * @author 20390802472
 */
public class VistaSimulacion extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VistaSimulacion.class.getName());

    /**
     * Creates new form VistaSimulacion
     */
    
    // TEMPORAL: permite probar la pantalla sin el controlador definitivo.
    // Al integrar MVC, eliminar esta dependencia y utilizar solo el controlador.
    
    private Ecosistema ecosistema;
    private ControladorSimulacionAPI controlador;
    public void setControlador(ControladorSimulacionAPI controlador) {
        if (controlador == null) {
            throw new IllegalArgumentException(
                "El controlador no puede ser null"
            );
        }

        this.controlador = controlador;
        actualizarPantalla();
        btnAvanzarTurno.setEnabled(!controlador.simulacionTerminada());
    }
    // PENDIENTE MVC: reemplazar el acceso directo a Ecosistema
    // por el controlador acordado con Mati.
    // La vista consultara turnos, clima, cantidades, entidades y eventos
    // exclusivamente a traves del controlador.
    
    public VistaSimulacion() {
        initComponents();
        
        ecosistema = new Ecosistema(Clima.SOLEADO, 20);
        
        ecosistema.inicializarPoblacion(10, 5, 2);
        
        actualizarPantalla();
    }
    
    private void actualizarPantalla() {

        if (controlador != null) {

            cargarIndicadores(
                controlador.getTurnoActual(),
                controlador.getClimaActual(),
                controlador.getCantidadPlantas(),
                controlador.getCantidadConejos(),
                controlador.getCantidadLobos()
            );

            cargarDatosEnTabla(
                controlador.getDatosEntidades()
            );

            cargarEventos(
                controlador.getEventosActuales()
            );

        } else {

            // Funcionamiento provisional.
            cargarIndicadores(
                ecosistema.getTurnoActual(),
                ecosistema.getClimaActual().name(),
                ecosistema.contarPlantasVivas(),
                ecosistema.contarConejosVivos(),
                ecosistema.contarLobosVivos()
            );

            actualizarTablaEntidades();
            actualizarEventos();
        }
    }
    
    private void actualizarTablaEntidades() {

        java.util.ArrayList<Object[]> filas = new java.util.ArrayList<>();

        for (Planta planta : ecosistema.getPlantasVivas()) {
            filas.add(new Object[]{
                planta.getNombre(),
                "Planta",
                planta.getEdad(),
                planta.getEnergia()
            });
        }

        for (Conejo conejo : ecosistema.getConejosVivos()) {
            filas.add(new Object[]{
                conejo.getNombre(),
                "Conejo",
                conejo.getEdad(),
                conejo.getEnergia()
            });
        }

        for (Lobo lobo : ecosistema.getLobosVivos()) {
            filas.add(new Object[]{
                lobo.getNombre(),
                "Lobo",
                lobo.getEdad(),
                lobo.getEnergia()
            });
        }

        cargarDatosEnTabla(filas.toArray(new Object[0][]));
    }
    
    private void cargarDatosEnTabla(Object[][] datos) {
        DefaultTableModel modelo =
                (DefaultTableModel) tblEntidades.getModel();

        modelo.setRowCount(0);

        if (datos == null) {
            return;
        }

        for (Object[] fila : datos) {
            modelo.addRow(fila);
        }
    }
    
    private void actualizarEventos() {
        cargarEventos(ecosistema.getEventosActuales());
    }
    
    private void cargarEventos(java.util.ArrayList<String> eventos) {
        txtEventos.setText("");

        if (eventos == null) {
            return;
        }

        for (String evento : eventos) {
            txtEventos.append(evento + "\n");
        }
    }
    
    private void cargarIndicadores(
            int turno,
            String clima,
            int plantas,
            int conejos,
            int lobos) {

        lblTurno.setText(String.valueOf(turno));
        lblClima.setText(clima);

        lblPlantas.setText(String.valueOf(plantas));
        lblConejos.setText(String.valueOf(conejos));
        lblLobos.setText(String.valueOf(lobos));
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lblTurno = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblClima = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblPlantas = new javax.swing.JLabel();
        lblConejos = new javax.swing.JLabel();
        lblLobos = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblEntidades = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtEventos = new javax.swing.JTextArea();
        btnAvanzarTurno = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("EcoSim - Simulacion del Ecosistema");

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitulo.setText("SIMULADOR DE ECOSISTEMA");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Turno:");

        lblTurno.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTurno.setText("0");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Clima:");

        lblClima.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblClima.setText("Sin definir");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Plantas");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Conejos");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Lobos");

        lblPlantas.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblPlantas.setText("0");

        lblConejos.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblConejos.setText("0");

        lblLobos.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblLobos.setText("0");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("ENTIDADES VIVAS");

        tblEntidades.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Tipo", "Edad", "Energia"
            }
        ));
        jScrollPane1.setViewportView(tblEntidades);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("EVENTOS DEL TURNO");

        txtEventos.setEditable(false);
        txtEventos.setColumns(20);
        txtEventos.setLineWrap(true);
        txtEventos.setRows(5);
        txtEventos.setWrapStyleWord(true);
        jScrollPane2.setViewportView(txtEventos);

        btnAvanzarTurno.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAvanzarTurno.setText("Avanzar turno");
        btnAvanzarTurno.addActionListener(this::btnAvanzarTurnoActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(353, Short.MAX_VALUE)
                .addComponent(lblTitulo)
                .addGap(296, 296, 296))
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTurno, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblClima))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnAvanzarTurno)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane2)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 809, Short.MAX_VALUE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(20, 20, 20)
                                        .addComponent(lblPlantas)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(100, 100, 100)
                                        .addComponent(lblConejos)
                                        .addGap(97, 97, 97)
                                        .addComponent(lblLobos))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(78, 78, 78)
                                            .addComponent(jLabel5)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel6))
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(231, 231, 231)
                                            .addComponent(jLabel3))))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitulo)
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblTurno)
                    .addComponent(jLabel2)
                    .addComponent(lblClima))
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPlantas)
                    .addComponent(lblConejos)
                    .addComponent(lblLobos))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(26, 26, 26)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAvanzarTurno)
                .addContainerGap(188, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAvanzarTurnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAvanzarTurnoActionPerformed
            if (controlador != null) {

            if (controlador.simulacionTerminada()) {
                btnAvanzarTurno.setEnabled(false);
                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "La simulación ha finalizado.",
                    "Fin de simulación",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            controlador.avanzarTurno();
            actualizarPantalla();
            

            if (controlador.simulacionTerminada()) {
                btnAvanzarTurno.setEnabled(false);
                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "La simulación ha finalizado.",
                    "Fin de simulación",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
                );
            }

        } else {

            // Funcionamiento temporal hasta integrar el controlador de Mati.
            if (ecosistema.simulacionTerminada()) {
                btnAvanzarTurno.setEnabled(false);
                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "La simulación ha finalizado.",
                    "Fin de simulación",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            ecosistema.procesarTurno();
            actualizarPantalla();

            if (ecosistema.simulacionTerminada()) {
                btnAvanzarTurno.setEnabled(false);
                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "La simulación ha finalizado.",
                    "Fin de simulación",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
                );
            }
        }
    }//GEN-LAST:event_btnAvanzarTurnoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new VistaSimulacion().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAvanzarTurno;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblClima;
    private javax.swing.JLabel lblConejos;
    private javax.swing.JLabel lblLobos;
    private javax.swing.JLabel lblPlantas;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTurno;
    private javax.swing.JTable tblEntidades;
    private javax.swing.JTextArea txtEventos;
    // End of variables declaration//GEN-END:variables
}
