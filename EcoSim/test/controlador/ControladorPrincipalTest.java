package controlador;

import vista.PanelIntervencion;
import vista.PanelReporte;
import vista.VistaPrincipal;

/** Pruebas sin JUnit: ejecutar main; lanza AssertionError si algo falla. */
public class ControladorPrincipalTest {
    static class VistaFalsa implements VistaPrincipal {
        String pantalla = "";
        String mensaje;
        boolean respuesta = true;
        int preguntas;    // preguntas de "nueva simulacion" (partida en curso)
        int inicios;      // preguntas de confirmacion al iniciar
        String ultimaPregunta;
        public void mostrarConfiguracion() { pantalla = "config"; }
        public void mostrarSimulacion() { pantalla = "simulacion"; }
        public void mostrarReporte() { pantalla = "reporte"; }
        public void mostrarMensaje(String m) { mensaje = m; }
        public boolean confirmar(String p) {
            ultimaPregunta = p;
            if (p.startsWith("¿Iniciar")) inicios++; else preguntas++;
            return respuesta;
        }
    }

    static void check(boolean ok, String que) {
        if (!ok) throw new AssertionError(que);
        System.out.println("OK  " + que);
    }

    public static void main(String[] args) {
        VistaFalsa v = new VistaFalsa();
        ControladorIntervencion ci = new ControladorIntervencion(new PanelIntervencion());
        ControladorReporte cr = new ControladorReporte(new PanelReporte());
        ControladorConfiguracion cc = new ControladorConfiguracion(ci, cr);
        ControladorPrincipal cp = new ControladorPrincipal(v, cc, ci, cr);
        ControladorSimulacionAPI api = new ControladorSimulacionAPI(cp);

        cp.avanzarTurno();
        check(v.pantalla.isEmpty(), "avanzar sin simulacion no navega ni falla");

        cp.iniciarSimulacion("x", "2", "1", "Soleado", "10");
        check(v.mensaje != null && v.pantalla.isEmpty() && v.inicios == 0, "config invalida: muestra el error y no pide confirmar");

        cp.iniciarSimulacion("5", "2", "1", "Soleado", "10");
        check(v.pantalla.equals("simulacion") && v.inicios == 1 && v.ultimaPregunta.contains("5 plantas, 2 conejos, 1 lobos"),
                "config valida: pide confirmar con el resumen y va a simulacion");
        check(api.getTurnoActual() == 0 && api.getCantidadPlantas() == 5 && api.getCantidadConejos() == 2
                && api.getCantidadLobos() == 1 && api.getClimaActual().equals("SOLEADO")
                && !api.simulacionTerminada(), "API refleja la simulacion creada");
        check(api.getDatosEntidades().length == 8 && api.getDatosEntidades()[0].length == 4, "API: una fila por entidad viva");

        api.avanzarTurno();
        check(api.getTurnoActual() == 1 && (v.pantalla.equals("simulacion") || v.pantalla.equals("reporte")), "un turno avanza");

        for (int i = 0; i < 10 && !v.pantalla.equals("reporte"); i++) api.avanzarTurno();
        check(v.pantalla.equals("reporte") && api.simulacionTerminada(), "al terminar navega al reporte");

        cr.nuevaSimulacion();   // lo que dispara el boton "Nueva simulacion" del reporte
        check(v.pantalla.equals("config") && v.preguntas == 0, "nueva simulacion desde el reporte vuelve a config sin preguntar");

        cp.iniciarSimulacion("6", "3", "2", "Lluvioso", "12");
        check(v.pantalla.equals("simulacion") && api.getCantidadPlantas() == 6 && api.getTurnoActual() == 0,
                "segunda simulacion arranca de cero");

        // Nueva simulacion con una partida en curso: pide confirmacion
        v.respuesta = false;
        cp.nuevaSimulacion();
        check(v.preguntas == 1 && v.pantalla.equals("simulacion") && cp.hayPartidaEnCurso(),
                "nueva con partida en curso: pregunta y, si cancela, sigue la partida");
        v.respuesta = true;
        cp.nuevaSimulacion();
        check(v.preguntas == 2 && v.pantalla.equals("config") && !cp.hayPartidaEnCurso(),
                "nueva con partida en curso: si confirma, vuelve a config y descarta la partida");
        check(!api.haySimulacion() && api.simulacionTerminada() && api.getCantidadPlantas() == 0
                && api.getTurnoActual() == 0 && api.getClimaActual().equals("-") && api.getDatosEntidades().length == 0,
                "sin simulacion: la API devuelve valores vacios y no deja avanzar");
        check(api.getEventosActuales().size() == 1 && api.getEventosActuales().get(0).contains("No hay una simulaci"),
                "sin simulacion: los eventos muestran el mensaje de estado");
        api.avanzarTurno();
        cp.nuevaSimulacion();
        check(v.preguntas == 2 && v.pantalla.equals("config"), "nueva sin partida: no pregunta y avanzar no falla");

        // Si la persona no confirma el inicio, no se crea nada y sigue en la configuracion
        v.respuesta = false;
        int iniciosAntes = v.inicios;
        cp.iniciarSimulacion("6", "3", "2", "Lluvioso", "12");
        check(v.inicios == iniciosAntes + 1 && v.pantalla.equals("config") && !api.haySimulacion(),
                "inicio cancelado: sigue en la configuracion y no crea la simulacion");
        v.respuesta = true;
        cp.iniciarSimulacion("6", "3", "2", "Lluvioso", "12");
        check(api.haySimulacion() && api.getCantidadPlantas() == 6, "despues de descartar se puede iniciar otra");

        // Intervencion: solo cada 3 turnos, y avisa a la ventana para que refresque
        int[] avisos = {0};
        ci.setAlIntervenir(() -> avisos[0]++);
        cp.iniciarSimulacion("30", "5", "1", "Soleado", "50");   // 5 conejos: un lobo no los extingue en 3 turnos
        ci.agregarEntidad("planta");
        check(avisos[0] == 0 && api.getCantidadPlantas() == 30, "turno 0: no se puede intervenir");
        for (int i = 0; i < 3; i++) api.avanzarTurno();
        if (api.simulacionTerminada()) {   // el azar extinguio una poblacion antes del turno 3: no hay ventana de intervencion
            System.out.println("SKIP intervencion: la simulacion colapso antes del turno 3");
            return;
        }
        int plantas = api.getCantidadPlantas();
        ci.agregarEntidad("planta");
        check(avisos[0] == 1 && api.getCantidadPlantas() == plantas + 1, "turno 3: interviene y avisa a la ventana");
        ci.agregarEntidad("planta");
        check(avisos[0] == 1, "una sola intervencion por turno");
    }
}
