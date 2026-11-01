package vista;

import controlador.ControladorSimulacionAPI;
import java.awt.CardLayout;
import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Ventana única de la aplicación: muestra una pantalla a la vez
 * (configuración, simulación o reporte) con un CardLayout.
 */
public class VentanaPrincipal extends JFrame implements VistaPrincipal {
    private static final String CONFIGURACION = "configuracion";
    private static final String SIMULACION = "simulacion";
    private static final String REPORTE = "reporte";

    private final CardLayout cartas = new CardLayout();
    private final JPanel contenido = new JPanel(cartas);
    private final PanelConfigIni configuracion;
    private final VistaSimulacion simulacion;
    private final PanelReporte reporte;
    private ControladorSimulacionAPI controladorSimulacion;

    public VentanaPrincipal(PanelConfigIni configuracion, VistaSimulacion simulacion, PanelReporte reporte) {
        super("EcoSim");
        this.configuracion = configuracion;
        this.simulacion = simulacion;
        this.reporte = reporte;
        contenido.add(configuracion, CONFIGURACION);
        // VistaSimulacion es un JFrame: se reutiliza su contenido como pantalla (nunca se muestra su ventana)
        contenido.add(simulacion.getContentPane(), SIMULACION);
        contenido.add(reporte, REPORTE);
        setContentPane(contenido);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        mostrarConfiguracion();
        setLocationRelativeTo(null);
    }

    /** Se necesita antes de mostrar la simulación: es lo que carga sus datos en pantalla. */
    public void setControladorSimulacion(ControladorSimulacionAPI controlador) {
        this.controladorSimulacion = controlador;
    }

    @Override
    public void mostrarConfiguracion() {
        mostrar(CONFIGURACION, configuracion);
    }

    @Override
    public void mostrarSimulacion() {
        simulacion.setControlador(controladorSimulacion);   // refresca los datos de la simulación nueva
        mostrar(SIMULACION, simulacion.getContentPane());
    }

    @Override
    public void mostrarReporte() {
        mostrar(REPORTE, reporte);
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "EcoSim", JOptionPane.WARNING_MESSAGE);
    }

    // CardLayout toma el tamaño de la pantalla más grande; así la ventana se ajusta a la que se ve
    private void mostrar(String nombre, Component pantalla) {
        cartas.show(contenido, nombre);
        contenido.setPreferredSize(pantalla.getPreferredSize());
        pack();
    }
}
