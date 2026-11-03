package controlador;

import java.util.ArrayList;
import modelo.Ecosistema;
import modelo.Entidad;

/**
 * Lo que la pantalla de simulación consulta y dispara; no conoce el modelo.
 * Si todavía no hay una simulación devuelve valores vacíos y un mensaje en los eventos,
 * y simulacionTerminada() da true para que la vista no deje avanzar.
 */
public class ControladorSimulacionAPI {
    static final String SIN_SIMULACION = "No hay una simulación en curso. Configurá una nueva para empezar.";

    private final ControladorPrincipal principal;

    public ControladorSimulacionAPI(ControladorPrincipal principal) {
        this.principal = principal;
    }

    public boolean haySimulacion() { return eco() != null; }
    public int getTurnoActual() { return haySimulacion() ? eco().getTurnoActual() : 0; }
    public String getClimaActual() { return haySimulacion() ? eco().getClimaActual().name() : "-"; }
    public int getCantidadPlantas() { return haySimulacion() ? eco().contarPlantasVivas() : 0; }
    public int getCantidadConejos() { return haySimulacion() ? eco().contarConejosVivos() : 0; }
    public int getCantidadLobos() { return haySimulacion() ? eco().contarLobosVivos() : 0; }
    public boolean simulacionTerminada() { return !haySimulacion() || eco().simulacionTerminada(); }
    public void avanzarTurno() { principal.avanzarTurno(); }
    public void nuevaSimulacion() { principal.nuevaSimulacion(); }

    public ArrayList<String> getEventosActuales() {
        if (!haySimulacion()) {
            ArrayList<String> mensaje = new ArrayList<>();
            mensaje.add(SIN_SIMULACION);
            return mensaje;
        }
        return eco().getEventosActuales();
    }

    /** Una fila por entidad viva: nombre, tipo, edad, energía. */
    public Object[][] getDatosEntidades() {
        ArrayList<Object[]> filas = new ArrayList<>();
        if (haySimulacion()) {
            agregar(filas, eco().getPlantasVivas(), "Planta");
            agregar(filas, eco().getConejosVivos(), "Conejo");
            agregar(filas, eco().getLobosVivos(), "Lobo");
        }
        return filas.toArray(new Object[0][]);
    }

    private void agregar(ArrayList<Object[]> filas, ArrayList<? extends Entidad> entidades, String tipo) {
        for (Entidad e : entidades) {
            filas.add(new Object[]{e.getNombre(), tipo, e.getEdad(), e.getEnergia()});
        }
    }

    private Ecosistema eco() { return principal.getEcosistema(); }
}
