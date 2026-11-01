package controlador;

import java.util.ArrayList;
import modelo.Ecosistema;
import modelo.Entidad;

/** Lo que la pantalla de simulación consulta y dispara; no conoce el modelo. */
public class ControladorSimulacionAPI {
    private final ControladorPrincipal principal;

    public ControladorSimulacionAPI(ControladorPrincipal principal) {
        this.principal = principal;
    }

    public int getTurnoActual() { return eco().getTurnoActual(); }
    public String getClimaActual() { return eco().getClimaActual().name(); }
    public int getCantidadPlantas() { return eco().contarPlantasVivas(); }
    public int getCantidadConejos() { return eco().contarConejosVivos(); }
    public int getCantidadLobos() { return eco().contarLobosVivos(); }
    public boolean simulacionTerminada() { return eco().simulacionTerminada(); }
    public ArrayList<String> getEventosActuales() { return eco().getEventosActuales(); }
    public void avanzarTurno() { principal.avanzarTurno(); }

    /** Una fila por entidad viva: nombre, tipo, edad, energía. */
    public Object[][] getDatosEntidades() {
        ArrayList<Object[]> filas = new ArrayList<>();
        agregar(filas, eco().getPlantasVivas(), "Planta");
        agregar(filas, eco().getConejosVivos(), "Conejo");
        agregar(filas, eco().getLobosVivos(), "Lobo");
        return filas.toArray(new Object[0][]);
    }

    private void agregar(ArrayList<Object[]> filas, ArrayList<? extends Entidad> entidades, String tipo) {
        for (Entidad e : entidades) {
            filas.add(new Object[]{e.getNombre(), tipo, e.getEdad(), e.getEnergia()});
        }
    }

    private Ecosistema eco() { return principal.getEcosistema(); }
}
