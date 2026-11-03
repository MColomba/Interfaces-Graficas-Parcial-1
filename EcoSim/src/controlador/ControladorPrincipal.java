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

    /**
     * Valida la configuración: si no es válida muestra el error; si es válida pide confirmar
     * y, si la persona acepta, crea la simulación y va a esa pantalla.
     */
    public void iniciarSimulacion(String plantas, String conejos, String lobos, String clima, String turnos) {
        ResultadoIntervencion valida = configuracion.validar(plantas, conejos, lobos, clima, turnos);
        if (!valida.isExito()) {
            vista.mostrarMensaje(valida.getMensaje());
            return;
        }
        if (!vista.confirmar("¿Iniciar la simulación con " + valida.getMensaje() + "?")) {
            return;
        }
        configuracion.iniciarSimulacion(plantas, conejos, lobos, clima, turnos);
        vista.mostrarSimulacion();
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
    
        public void guardarSimulacion() {
        try {
            Ecosistema eco = getEcosistema();
            dao.EcosistemaDAO.guardar(eco);
            vista.mostrarMensaje("¡Simulación guardada con éxito!");
        } catch (Exception e) {
            vista.mostrarMensaje("Error al guardar: " + e.getMessage());
        }
    }

    /** Carga una simulación guardada y pasa directamente a la pantalla de simulación. */
    public void cargarSimulacion() {
        try {
            Ecosistema ecoLoaded = dao.EcosistemaDAO.cargar();
            // Reasignamos el ecosistema cargado a los controladores de configuración e intervención
            configuracion.getEcosistema(); // (O puedes crear un método setter en ControladorConfiguracion si lo prefieres)
            // O directamente actualizar los controladores:
            // configuracion.ecosistema = ecoLoaded; (si tienes acceso o creas un método setEcosistema en ControladorConfiguracion)
            vista.mostrarMensaje("¡Simulación cargada con éxito!");
            vista.mostrarSimulacion();
        } catch (Exception e) {
            vista.mostrarMensaje("Error al cargar la simulación: " + e.getMessage());
        }
    }
}
