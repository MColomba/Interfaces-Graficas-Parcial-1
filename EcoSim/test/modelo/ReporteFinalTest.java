package modelo;

/** Pruebas sin JUnit: ejecutar main; lanza AssertionError si algo falla. */
public class ReporteFinalTest {
    static void check(boolean ok, String que) {
        if (!ok) throw new AssertionError(que);
    }

    public static void main(String[] args) {
        java.io.PrintStream consolaOriginal = System.out;
        java.io.ByteArrayOutputStream consola = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(consola));   // el modelo no debe escribir en la consola
        for (int corrida = 0; corrida < 30; corrida++) {
            Ecosistema eco = new Ecosistema(Clima.SOLEADO, 50);
            eco.inicializarPoblacion(10, 6, 2);
            while (!eco.simulacionTerminada()) {
                eco.procesarTurno();
            }
            ReporteFinal r = eco.generarReporteFinal();

            // Los conteos del reporte tienen que cerrar con la poblacion final
            check(10 + r.getNacimientosPlantas() + r.getAgregadasPlantas() - r.getMuertesPlantas() == r.getPlantasFinal(),
                    "plantas: inicial + nacimientos - muertes = final");
            check(6 + r.getNacimientosConejos() + r.getAgregadosConejos() - r.getMuertesConejos() == r.getConejosFinal(),
                    "conejos: inicial + nacimientos - muertes = final (corrida " + corrida + ")");
            check(2 + r.getAgregadosLobos() - r.getMuertesLobos() == r.getLobosFinal(),
                    "lobos: inicial - muertes = final");
            check(r.getTurnoMayorActividad() > 0 && r.getEventosMayorActividad() > 0, "hubo actividad registrada");
        }
        System.setOut(consolaOriginal);
        check(consola.size() == 0, "el modelo no imprime nada por consola (salio: " + consola + ")");
        System.out.println("OK  reporte final coherente en 30 simulaciones");

        // Una muerte por falta de energia queda registrada como evento
        Ecosistema eco = new Ecosistema(Clima.SEQUIA, 50);
        eco.inicializarPoblacion(5, 2, 1);
        eco.getPlantas().clear();   // sin plantas ni plantas que comer, y la sequia resta energia
        eco.getLobos().clear();     // sin lobos nadie los caza
        eco.getConejos().get(0).setEnergia(1);
        eco.procesarTurno();
        boolean registrada = eco.getEventosActuales().stream().anyMatch(e -> e.contains("murio (sin energia)"));
        check(registrada, "muerte de un conejo registrada como evento");
        check(eco.generarReporteFinal().getMuertesConejos() >= 1, "y contada en el reporte");
        System.out.println("OK  muertes registradas como eventos");
    }
}
