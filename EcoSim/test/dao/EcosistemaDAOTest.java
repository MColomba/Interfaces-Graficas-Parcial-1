package dao;

import controlador.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import modelo.*;
import vista.PanelIntervencion;
import vista.PanelReporte;
import vista.VistaPrincipal;

/**
 * Pruebas sin JUnit: ejecutar main; lanza AssertionError si algo falla.
 * Usa ecosistema_guardado.txt de la carpeta actual: si ya había un guardado lo respalda y lo restaura al final.
 */
public class EcosistemaDAOTest {
    static class VistaFalsa implements VistaPrincipal {
        String mensaje = "";
        String pantalla = "";
        boolean respuesta = true;
        int preguntas;
        public void mostrarConfiguracion() { pantalla = "config"; }
        public void mostrarSimulacion() { pantalla = "simulacion"; }
        public void mostrarReporte() { pantalla = "reporte"; }
        public void mostrarMensaje(String m) { mensaje = m; }
        public boolean confirmar(String p) { preguntas++; return respuesta; }
    }

    static void check(boolean ok, String que) {
        if (!ok) throw new AssertionError(que);
        System.out.println("OK  " + que);
    }

    public static void main(String[] args) throws Exception {
        File guardado = new File("ecosistema_guardado.txt");
        byte[] respaldo = guardado.exists() ? Files.readAllBytes(guardado.toPath()) : null;
        try {
            probar(guardado);
        } finally {
            if (respaldo != null) Files.write(guardado.toPath(), respaldo); else guardado.delete();
        }
    }

    static void probar(File guardado) throws Exception {
        guardado.delete();
        VistaFalsa v = new VistaFalsa();
        ControladorIntervencion ci = new ControladorIntervencion(new PanelIntervencion());
        ControladorReporte cr = new ControladorReporte(new PanelReporte());
        ControladorConfiguracion cc = new ControladorConfiguracion(ci, cr);
        ControladorPrincipal cp = new ControladorPrincipal(v, cc, ci, cr);
        ControladorSimulacionAPI api = new ControladorSimulacionAPI(cp);

        cp.guardarSimulacion();
        check(v.mensaje.startsWith("Error al guardar") && !guardado.exists(), "guardar sin simulación: avisa y no crea archivo");
        cp.cargarSimulacion();
        check(v.mensaje.startsWith("Error al cargar") && v.pantalla.isEmpty(), "cargar sin archivo: avisa y no navega");

        // Una partida que siga viva a los 3 turnos (el azar puede extinguir una población antes)
        Ecosistema antes = null;
        for (int intento = 0; intento < 100 && antes == null; intento++) {
            cp.iniciarSimulacion("30", "10", "3", "Soleado", "50");
            for (int i = 0; i < 3; i++) api.avanzarTurno();
            if (!api.simulacionTerminada()) antes = cp.getEcosistema();
        }
        check(antes != null, "hay una partida en curso para guardar");
        int turno = antes.getTurnoActual(), plantas = antes.contarPlantasVivas(),
            conejos = antes.contarConejosVivos(), lobos = antes.contarLobosVivos();
        int lobosHistoricos = antes.getHistorialLobos().size();
        int agregables = antes.lobosQuePuedenAgregarse();

        cp.guardarSimulacion();
        check(v.mensaje.contains("guardada") && guardado.exists(), "guardar crea el archivo");

        // Se sigue jugando, así que el estado actual difiere del guardado
        v.respuesta = false;
        api.avanzarTurno();
        int turnoDespues = api.getTurnoActual();
        check(turnoDespues == turno + 1, "la partida avanzó después de guardar");

        int preguntasAntes = v.preguntas;
        // Si no confirma el reemplazo, la partida sigue como estaba
        cp.cargarSimulacion();
        check(v.preguntas == preguntasAntes + 1 && api.getTurnoActual() == turnoDespues, "cargar con partida en curso pide confirmar y, si cancela, no cambia nada");

        v.respuesta = true;
        cp.cargarSimulacion();
        Ecosistema cargado = cp.getEcosistema();
        check(cargado != antes && v.pantalla.equals("simulacion") && v.mensaje.contains("cargada"), "cargar reemplaza la simulación y va a la pantalla de simulación");
        check(api.getTurnoActual() == turno && api.getCantidadPlantas() == plantas
                && api.getCantidadConejos() == conejos && api.getCantidadLobos() == lobos
                && cargado.getClimaActual() == antes.getClimaActual() && cargado.getTurnosTotales() == 50,
                "la API muestra el estado guardado (turno, poblaciones, clima, turnos totales)");
        check(cargado.getHistorialLobos().size() == lobosHistoricos && cargado.lobosQuePuedenAgregarse() == agregables,
                "el tope de lobos se conserva al cargar");

        // Los nombres nuevos no pueden repetir los de entidades existentes (vivas o muertas)
        java.util.HashSet<String> nombres = new java.util.HashSet<>();
        for (Planta p : cargado.getHistorialPlantas()) nombres.add(p.getNombre());
        for (Conejo c : cargado.getHistorialConejos()) nombres.add(c.getNombre());
        for (Lobo l : cargado.getHistorialLobos()) nombres.add(l.getNombre());
        check(!nombres.contains(cargado.generarNombre("planta")) && !nombres.contains(cargado.generarNombre("conejo")),
                "los nombres nuevos no repiten los guardados");

        // La partida cargada se puede seguir jugando hasta el reporte
        for (int i = 0; i < 60 && !api.simulacionTerminada(); i++) api.avanzarTurno();
        check(api.simulacionTerminada() && v.pantalla.equals("reporte"), "la partida cargada sigue hasta el reporte final");

        // Archivos inválidos: el DAO avisa claro y la partida actual queda intacta
        cp.iniciarSimulacion("30", "10", "3", "Soleado", "50");
        int turnoActual = api.getTurnoActual();
        String[] invalidos = {"CLIMA=NIEVE", "TURNO=abc", "PLANTA=Helecho-1,10", "COSA=1", "sin igual"};
        for (String contenido : invalidos) {
            Files.writeString(Path.of(guardado.getPath()), contenido + "\n");
            v.mensaje = "";
            cp.cargarSimulacion();
            check(v.mensaje.startsWith("Error al cargar") && !v.mensaje.contains("No enum") && !v.mensaje.contains("For input")
                    && api.getTurnoActual() == turnoActual, "archivo inválido (" + contenido + "): mensaje claro y la partida sigue");
        }
    }
}
