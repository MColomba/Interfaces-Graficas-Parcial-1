/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.Clima;
import modelo.Ecosistema;
import modelo.ResultadoIntervencion;
import vista.PanelIntervencion;

import java.text.Normalizer;
 

 /* Controlador de las intervenciones del jugador 
 *
 * recibe lo que eligió la persona en el PanelIntervencion, valida las
 * reglas (¿toca intervenir?, ¿ya intervino?), le pide al modelo que haga la acción y le
 * ordena a la vista qué mostrar. 
 *
 * El controlador principal  tiene que:
 *   - llamar a setEcosistema(...) al empezar o cargar una simulación
 *   - llamar a actualizarPanel() después de cada turno procesado
*/

public class ControladorIntervencion {
    private Ecosistema ecosistema;
    private final PanelIntervencion panel;
 
    // Turno en el que el jugador ya usó su intervención (-1 = todavía ninguna).
    // Sirve para permitir UNA sola acción por cada ventana de intervención.
    private int turnoIntervenido = -1;
 
    public ControladorIntervencion(PanelIntervencion panel) {
        this.panel = panel;
        this.panel.setControlador(this);
    }
 
    // ===================== Lo que usa el controlador principal =====================
 
    /** Se llama al iniciar una simulación nueva o al cargar una guardada. */
    public void setEcosistema(Ecosistema ecosistema) {
        this.ecosistema = ecosistema;
        this.turnoIntervenido = -1;
        actualizarPanel();
    }
 
    /** Se llama después de cada turno: refresca el clima y habilita o bloquea el panel. */
    public void actualizarPanel() {
        if (ecosistema == null) {
            panel.habilitarIntervencion(false, "No hay una simulación en curso.");
            panel.mostrarClimaActual("-");
            return;
        }
        panel.mostrarClimaActual(Ecosistema.nombreClima(ecosistema.getClimaActual()));
        panel.habilitarIntervencion(puedeIntervenir(), textoEstado());
    }
 
    // ===================== Lo que dispara la vista (botones) =====================
 
    public void cambiarClima(String nombreClima) {
        if (!validarVentana()) {
            return;
        }
        Clima nuevo = parsearClima(nombreClima);
        if (nuevo == null) {
            panel.mostrarMensaje("Elegí un clima válido.");
            return;
        }
        ResultadoIntervencion r = ecosistema.cambiarClima(nuevo);
        cerrarVentanaSiFueExitosa(r);
    }
 
    public void agregarEntidad(String tipo) {
        if (!validarVentana()) {
            return;
        }
        ResultadoIntervencion r = ecosistema.agregarEntidad(tipo);   // energía aleatoria
        cerrarVentanaSiFueExitosa(r);
    }
 
    public void continuarSinIntervenir() {
        if (!validarVentana()) {
            return;
        }
        turnoIntervenido = ecosistema.getTurnoActual();
        actualizarPanel();
        panel.mostrarMensaje("Seguís sin intervenir. Avanzá al próximo turno.");
    }
 
    // ===================== Reglas =====================
 
    /** Toca intervenir (cada 3 turnos, sin haber terminado) y todavía no se usó en este turno.
     * @return  */
    public boolean puedeIntervenir() {
        return ecosistema != null
                && ecosistema.esTurnoDeIntervencion()
                && turnoIntervenido != ecosistema.getTurnoActual();
    }
 
    // Si no se puede intervenir, avisa por qué y devuelve false
    private boolean validarVentana() {
        if (ecosistema == null) {
            panel.mostrarMensaje("No hay una simulación en curso.");
            return false;
        }
        if (!ecosistema.esTurnoDeIntervencion()) {
            panel.mostrarMensaje("Solo se puede intervenir cada "
                    + Ecosistema.INTERVALO_INTERVENCION + " turnos.");
            return false;
        }
        if (turnoIntervenido == ecosistema.getTurnoActual()) {
            panel.mostrarMensaje("Ya intervenido en este turno. Avanzá al próximo.");
            return false;
        }
        return true;
    }
 
    // Si la acción salió bien, se usa la intervención; si no, la persona puede reintentar
    private void cerrarVentanaSiFueExitosa(ResultadoIntervencion r) {
        if (r.isExito()) {
            turnoIntervenido = ecosistema.getTurnoActual();
            actualizarPanel();
        }
        panel.mostrarMensaje(r.getMensaje());
    }
 
    private String textoEstado() {
        if (ecosistema.simulacionTerminada()) {
            return "La simulación terminó.";
        }
        if (puedeIntervenir()) {
            return "Podés intervenir. Lobos que aún se pueden agregar: "
                    + ecosistema.lobosQuePuedenAgregarse();
        }
        if (ecosistema.esTurnoDeIntervencion()) {
            return "Ya usaste tu intervención de este turno.";
        }
        int proximo = (ecosistema.getTurnoActual() / Ecosistema.INTERVALO_INTERVENCION + 1)
                * Ecosistema.INTERVALO_INTERVENCION;
        return "Próxima intervención en el turno " + proximo + ".";
    }
 
    // Convierte el texto del combo ("Sequía", "Sequia"...) en un Clima, sin importar tildes
    private Clima parsearClima(String texto) {
        if (texto == null) {
            return null;
        }
        String limpio = Normalizer.normalize(texto.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").toUpperCase();
        for (Clima c : Clima.values()) {
            if (c.name().equals(limpio)) {
                return c;
            }
        }
        return null;
    }
}
