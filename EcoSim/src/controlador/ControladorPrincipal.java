package controlador;

import modelo.Ecosistema;
import modelo.ResultadoIntervencion;
import vista.VistaPrincipal;

/**
 * Coordina la navegación configuración -> simulación -> reporte -> configuración.
 * La pantalla de configuración llama a iniciarSimulacion(...); la de simulación,
 * a avanzarTurno() (a través de ControladorSimulacionAPI); el reporte, a nuevaSimulacion().
 */
public class ControladorPrincipal {
    private final VistaPrincipal vista;
    private final ControladorConfiguracion configuracion;
    private final ControladorIntervencion intervencion;
    private final ControladorReporte reporte;

    public ControladorPrincipal(VistaPrincipal vista, ControladorConfiguracion configuracion,
                                ControladorIntervencion intervencion, ControladorReporte reporte) {
        this.vista = vista;
        this.configuracion = configuracion;
        this.intervencion = intervencion;
        this.reporte = reporte;
        reporte.setAccionNuevaSimulacion(this::nuevaSimulacion);
    }

    public Ecosistema getEcosistema() { return configuracion.getEcosistema(); }

    /** Valida la configuración: si es válida va a la simulación; si no, muestra el error. */
    public void iniciarSimulacion(String plantas, String conejos, String lobos, String clima, String turnos) {
        ResultadoIntervencion r = configuracion.iniciarSimulacion(plantas, conejos, lobos, clima, turnos);
        if (r.isExito()) {
            vista.mostrarSimulacion();
        } else {
            vista.mostrarMensaje(r.getMensaje());
        }
    }

    /** Un turno; si la simulación terminó, deja el reporte cargado y navega a él. */
    public void avanzarTurno() {
        Ecosistema eco = getEcosistema();
        if (eco == null || eco.simulacionTerminada()) {
            return;
        }
        eco.procesarTurno();
        intervencion.actualizarPanel();
        if (reporte.verificarFin()) {
            vista.mostrarReporte();
        }
    }

    /** Hay una simulación empezada que todavía no terminó. */
    public boolean hayPartidaEnCurso() {
        Ecosistema eco = getEcosistema();
        return eco != null && !eco.simulacionTerminada();
    }

    /** Vuelve a la configuración; si hay una partida en curso pide confirmar antes de descartarla. */
    public void nuevaSimulacion() {
        if (hayPartidaEnCurso()
                && !vista.confirmar("Hay una simulación en curso. Si empezás una nueva se pierde. ¿Continuar?")) {
            return;
        }
        configuracion.reiniciar();
        vista.mostrarConfiguracion();
    }
}
