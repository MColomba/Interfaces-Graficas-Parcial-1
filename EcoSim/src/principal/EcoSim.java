package principal;

import controlador.ControladorConfiguracion;
import controlador.ControladorIntervencion;
import controlador.ControladorPrincipal;
import controlador.ControladorReporte;
import controlador.ControladorSimulacionAPI;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import vista.PanelConfigIni;
import vista.PanelIntervencion;
import vista.PanelReporte;
import vista.VentanaPrincipal;
import vista.VistaSimulacion;

/**
 * EcoSim: clase principal (Main) del Simulador de Ecosistema.
 * Crea las vistas y los controladores, los conecta y muestra la ventana
 * en la pantalla de configuración inicial.
 */
public class EcoSim {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Mismo estilo que el diseñador de NetBeans (si no, Java usa "Metal" y cambian fuentes y tamaños)
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ReflectiveOperationException | UnsupportedLookAndFeelException e) {
                // si falla se queda con el estilo por defecto
            }

            // Vistas
            PanelConfigIni configuracion = new PanelConfigIni();
            VistaSimulacion simulacion = new VistaSimulacion();
            PanelReporte reporte = new PanelReporte();
            VentanaPrincipal ventana = new VentanaPrincipal(configuracion, simulacion, reporte);

            // Controladores
            ControladorIntervencion ctrlIntervencion = new ControladorIntervencion(new PanelIntervencion());
            ControladorReporte ctrlReporte = new ControladorReporte(reporte);
            ControladorConfiguracion ctrlConfiguracion = new ControladorConfiguracion(ctrlIntervencion, ctrlReporte);
            ControladorPrincipal ctrlPrincipal =
                    new ControladorPrincipal(ventana, ctrlConfiguracion, ctrlIntervencion, ctrlReporte);

            configuracion.setControlador(ctrlPrincipal);
            ventana.setControladorSimulacion(new ControladorSimulacionAPI(ctrlPrincipal));
            ventana.setVisible(true);
        });
    }
}
