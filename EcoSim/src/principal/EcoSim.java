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
import vista.PanelReporte;
import vista.fraVentanaPrincipal;
import vista.PanelSimulacio;

/**
 * EcoSim: clase principal (Main) del Simulador de Ecosistema.
 * Crea las vistas y los controladores, los conecta y muestra la ventana
 * en la pantalla de configuración inicial.
 */
public class EcoSim {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Estilo "Metal" con la fuente Segoe UI: el estilo de Windows ignora el color de fondo de los botones
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                UIManager.put("Button.margin", new java.awt.Insets(2, 6, 2, 6));   // Metal trae 14 px por lado y corta los textos
                for (Object clave : java.util.Collections.list(UIManager.getDefaults().keys())) {
                    if (clave instanceof String && ((String) clave).endsWith(".font")) {
                        UIManager.put(clave, new javax.swing.plaf.FontUIResource("Segoe UI", java.awt.Font.PLAIN, 12));
                    }
                }
            } catch (ReflectiveOperationException | UnsupportedLookAndFeelException e) {
                // si falla se queda con el estilo por defecto
            }

            // Vista: la ventana trae sus tres pantallas armadas desde el diseñador
            fraVentanaPrincipal ventana = new fraVentanaPrincipal();
            PanelConfigIni configuracion = ventana.getPanelConfig();
            PanelSimulacio simulacion = ventana.getPanelSimulacion();
            PanelReporte reporte = ventana.getPanelReporte();

            // Controladores
            ControladorIntervencion ctrlIntervencion = new ControladorIntervencion(simulacion.getPanelIntervencion());
            ControladorReporte ctrlReporte = new ControladorReporte(reporte);
            ControladorConfiguracion ctrlConfiguracion = new ControladorConfiguracion(ctrlIntervencion, ctrlReporte);
            ControladorPrincipal ctrlPrincipal =
                    new ControladorPrincipal(ventana, ctrlConfiguracion, ctrlIntervencion, ctrlReporte);

            ctrlIntervencion.setAlIntervenir(ventana::actualizarSimulacion);
            configuracion.setControlador(ctrlPrincipal);
            ventana.setControladorSimulacion(new ControladorSimulacionAPI(ctrlPrincipal));
            ventana.setVisible(true);
        });
    }
}
