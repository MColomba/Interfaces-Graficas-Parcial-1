/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.Ecosistema;
import modelo.ReporteFinal;
import vista.PanelReporte;

/**
 *
 * @author bruno
 */
public class ControladorReporte {
    
    private Ecosistema ecosistema;
    private final PanelReporte panel;
    
    private Runnable accionNuevaSimulacion;

 
    public ControladorReporte(PanelReporte panel) {
        this.panel = panel;
        this.panel.setControlador(this);
        this.panel.mostrarSinReporte();   // estado inicial: todavía no hay reporte
    }
 
    /** Se llama al iniciar una simulación nueva o al cargar una guardada.
     * @param ecosistema */
    public void setEcosistema(Ecosistema ecosistema) {
        this.ecosistema = ecosistema;
        this.panel.mostrarSinReporte();
    }
 
     
    /** El controlador principal le indica qué hacer al apretar "Nueva simulación". */
    public void setAccionNuevaSimulacion(Runnable accion) {
        this.accionNuevaSimulacion = accion;
    }
 
    /**
     * Detecta si la simulación terminó (turnos completos o extinción de una población).
     * Si terminó, deja el reporte cargado en el panel y devuelve true.
     * Se llama después de cada turno.
     */
    public boolean verificarFin() {
        if (ecosistema != null && ecosistema.simulacionTerminada()) {
            mostrarReporte();
            return true;
        }
        return false;
    }
 
    /** Lo dispara el botón "Nueva simulación" del panel. */
    public void nuevaSimulacion() {
        if (accionNuevaSimulacion != null) {
            accionNuevaSimulacion.run();
        }
    }
 
    /** Muestra el reporte si la simulación terminó; si no, muestra el estado "sin datos". */
    public void mostrarReporte() {
        if (ecosistema == null || !ecosistema.simulacionTerminada()) {
            panel.mostrarSinReporte();
            return;
        }
        ReporteFinal r = ecosistema.generarReporteFinal();
        panel.cargarReporte(r.getCausaFin(), armarFilas(r));
    }
 
    // Convierte el ReporteFinal en filas {dato, valor} para la tabla
    private String[][] armarFilas(ReporteFinal r) {
        String actividad = (r.getTurnoMayorActividad() == 0)
                ? "No se registraron eventos"
                : "Turno " + r.getTurnoMayorActividad()
                        + " (" + r.getEventosMayorActividad() + " eventos)";
 
        String cazador = (r.getLoboCazador() == null)
                ? "Ningún lobo logró cazar"
                : r.getLoboCazador() + " (" + r.getCazasLoboCazador() + " cacerías)";
 
        return new String[][] {
            fila("Turnos jugados", r.getTurnosJugados() + " de " + r.getTurnosTotales()),
            fila("Clima final", r.getClimaFinal()),
            fila("Plantas al final", String.valueOf(r.getPlantasFinal())),
            fila("Conejos al final", String.valueOf(r.getConejosFinal())),
            fila("Lobos al final", String.valueOf(r.getLobosFinal())),
            fila("Turno de mayor actividad", actividad),
            fila("Planta más longeva", r.getPlantaLongeva()),
            fila("Conejo más longevo", r.getConejoLongevo()),
            fila("Lobo más longevo", r.getLoboLongevo()),
            fila("Lobo con más cacerías", cazador),
            fila("Nacimientos de plantas", String.valueOf(r.getNacimientosPlantas())),
            fila("Nacimientos de conejos", String.valueOf(r.getNacimientosConejos())),
            fila("Nacimientos de lobos", "0 (los lobos no se reproducen)"),
            fila("Muertes de plantas", String.valueOf(r.getMuertesPlantas())),
            fila("Muertes de conejos", String.valueOf(r.getMuertesConejos())),
            fila("Muertes de lobos", String.valueOf(r.getMuertesLobos())),
            fila("Agregadas por el jugador", r.getAgregadasPlantas() + " plantas, "
                    + r.getAgregadosConejos() + " conejos, " + r.getAgregadosLobos() + " lobos")
        };
    }
 
    private String[] fila(String dato, String valor) {
        return new String[] { dato, valor };
    }
}
