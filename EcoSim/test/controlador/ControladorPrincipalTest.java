package controlador;

import vista.PanelIntervencion;
import vista.PanelReporte;
import vista.VistaPrincipal;

/** Pruebas sin JUnit: ejecutar main; lanza AssertionError si algo falla. */
public class ControladorPrincipalTest {
    static class VistaFalsa implements VistaPrincipal {
        String pantalla = "";
        String mensaje;
        public void mostrarConfiguracion() { pantalla = "config"; }
        public void mostrarSimulacion() { pantalla = "simulacion"; }
        public void mostrarReporte() { pantalla = "reporte"; }
        public void mostrarMensaje(String m) { mensaje = m; }
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
        check(v.mensaje != null && v.pantalla.isEmpty(), "config invalida: muestra mensaje y no navega");

        cp.iniciarSimulacion("5", "2", "1", "Soleado", "10");
        check(v.pantalla.equals("simulacion"), "config valida: va a simulacion");
        check(api.getTurnoActual() == 0 && api.getCantidadPlantas() == 5 && api.getCantidadConejos() == 2
                && api.getCantidadLobos() == 1 && api.getClimaActual().equals("SOLEADO")
                && !api.simulacionTerminada(), "API refleja la simulacion creada");
        check(api.getDatosEntidades().length == 8 && api.getDatosEntidades()[0].length == 4, "API: una fila por entidad viva");

        api.avanzarTurno();
        check(api.getTurnoActual() == 1 && (v.pantalla.equals("simulacion") || v.pantalla.equals("reporte")), "un turno avanza");

        for (int i = 0; i < 10 && !v.pantalla.equals("reporte"); i++) api.avanzarTurno();
        check(v.pantalla.equals("reporte") && api.simulacionTerminada(), "al terminar navega al reporte");

        cr.nuevaSimulacion();   // lo que dispara el boton "Nueva simulacion" del reporte
        check(v.pantalla.equals("config"), "nueva simulacion desde el reporte vuelve a config");

        cp.iniciarSimulacion("6", "3", "2", "Lluvioso", "12");
        check(v.pantalla.equals("simulacion") && api.getCantidadPlantas() == 6 && api.getTurnoActual() == 0,
                "segunda simulacion arranca de cero");
    }
}
