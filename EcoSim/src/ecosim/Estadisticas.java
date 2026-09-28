package ecosim;

import java.util.ArrayList;

public class Estadisticas {
    //CLASE INTERNA QUE GUARDA EL ESTADO DE CADA TURNO
    private static class RegistroTurno {
        private int turno;
        private int cantPlantas;
        private int cantConejos;
        private int cantLobos;

        public RegistroTurno(int turno, int cantPlantas, int cantConejos, int cantLobos) {
            this.turno = turno;
            this.cantPlantas = cantPlantas;
            this.cantConejos = cantConejos;
            this.cantLobos = cantLobos;
        }
    }

    //ATRIBUTOS
    private ArrayList<RegistroTurno> historial;

    //CONSTRUCTOS
    public Estadisticas() {
        this.historial = new ArrayList<>();
    }

    //METODOS
    public void registrarTurno(int turno, int plantas, int conejos, int lobos) {
        historial.add(new RegistroTurno(turno, plantas, conejos, lobos));
    }
    
    public void mostrarReporteEstadistico() {
        if (historial.isEmpty()) {
            System.out.println("No hay datos estadisticos registrados.");
            return;
        }
        
        RegistroTurno maxP = historial.get(0), minP = historial.get(0);
        RegistroTurno maxC = historial.get(0), minC = historial.get(0);
        RegistroTurno maxL = historial.get(0), minL = historial.get(0);
        
        for (RegistroTurno r : historial) {
            // Plantas
            if (r.cantPlantas > maxP.cantPlantas) maxP = r;
            if (r.cantPlantas < minP.cantPlantas) minP = r;

            // Conejos
            if (r.cantConejos > maxC.cantConejos) maxC = r;
            if (r.cantConejos < minC.cantConejos) minC = r;

            // Lobos
            if (r.cantLobos > maxL.cantLobos) maxL = r;
            if (r.cantLobos < minL.cantLobos) minL = r;
        }

        System.out.println("\n=======================================================");
        System.out.println("   BONUS: ESTADÍSTICAS EN TIEMPO REAL (HISTORIAL)      ");
        System.out.println("=======================================================");
        System.out.println("PLANTAS:");
        System.out.println("  * Maximo: " + maxP.cantPlantas + " plantas en el Turno " + maxP.turno);
        System.out.println("  * Minimo: " + minP.cantPlantas + " plantas en el Turno " + minP.turno);

        System.out.println("\nCONEJOS:");
        System.out.println("  * Maximo: " + maxC.cantConejos + " conejos en el Turno " + maxC.turno);
        System.out.println("  * Minimo: " + minC.cantConejos + " conejos en el Turno " + minC.turno);

        System.out.println("\nLOBOS:");
        System.out.println("  * Maximo: " + maxL.cantLobos + " lobos en el Turno " + maxL.turno);
        System.out.println("  * Minimo: " + minL.cantLobos + " lobos en el Turno " + minL.turno);
        System.out.println("=======================================================\n");
    }
}