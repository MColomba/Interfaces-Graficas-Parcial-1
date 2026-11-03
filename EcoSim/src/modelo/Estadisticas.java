package modelo;

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
    
    /** Texto con el maximo y minimo de cada poblacion; quien lo pide decide donde mostrarlo. */
    public String reporteEstadistico() {
        if (historial.isEmpty()) {
            return "No hay datos estadisticos registrados.";
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

        return "PLANTAS:\n"
                + "  * Maximo: " + maxP.cantPlantas + " plantas en el Turno " + maxP.turno + "\n"
                + "  * Minimo: " + minP.cantPlantas + " plantas en el Turno " + minP.turno + "\n\n"
                + "CONEJOS:\n"
                + "  * Maximo: " + maxC.cantConejos + " conejos en el Turno " + maxC.turno + "\n"
                + "  * Minimo: " + minC.cantConejos + " conejos en el Turno " + minC.turno + "\n\n"
                + "LOBOS:\n"
                + "  * Maximo: " + maxL.cantLobos + " lobos en el Turno " + maxL.turno + "\n"
                + "  * Minimo: " + minL.cantLobos + " lobos en el Turno " + minL.turno;
    }
}