package controlador;

import modelo.Clima;
import modelo.Ecosistema;
import modelo.ResultadoIntervencion;

/**
 * Controlador de la pantalla de configuración inicial: valida los datos que
 * escribió la persona, crea la simulación y se la entrega a los demás controladores.
 *
 * La vista llama a iniciarSimulacion(...) con los textos de sus campos y muestra
 * el mensaje del resultado (error de validación o confirmación).
 */
public class ControladorConfiguracion {
    private final ControladorIntervencion intervencion;
    private final ControladorReporte reporte;
    private Ecosistema ecosistema;

    public ControladorConfiguracion(ControladorIntervencion intervencion, ControladorReporte reporte) {
        this.intervencion = intervencion;
        this.reporte = reporte;
    }

    /** La simulación creada por la última configuración válida (null si todavía no hay). */
    public Ecosistema getEcosistema() { return ecosistema; }

    /** Descarta la simulación actual y deja a los controladores sin datos. */
    public void reiniciar() {
        ecosistema = null;
        intervencion.setEcosistema(null);
        reporte.setEcosistema(null);
    }

    /** Los datos de la configuración ya validados. */
    private record Configuracion(int plantas, int conejos, int lobos, Clima clima, int turnos) {
        String resumen() {
            return plantas + " plantas, " + conejos + " conejos, " + lobos + " lobos, clima "
                    + Ecosistema.nombreClima(clima) + ", " + turnos + " turnos";
        }
    }

    /** Valida sin crear nada; si es válida, el mensaje del resultado es el resumen de la configuración. */
    public ResultadoIntervencion validar(String plantas, String conejos, String lobos,
                                         String clima, String turnos) {
        try {
            return new ResultadoIntervencion(true, leer(plantas, conejos, lobos, clima, turnos).resumen());
        } catch (IllegalArgumentException e) {
            return new ResultadoIntervencion(false, e.getMessage());
        }
    }

    /** Valida; si todo está bien crea la simulación y la reparte a los controladores. */
    public ResultadoIntervencion iniciarSimulacion(String plantas, String conejos, String lobos,
                                                   String clima, String turnos) {
        Configuracion cfg;
        try {
            cfg = leer(plantas, conejos, lobos, clima, turnos);
        } catch (IllegalArgumentException e) {
            return new ResultadoIntervencion(false, e.getMessage());
        }

        ecosistema = new Ecosistema(cfg.clima(), cfg.turnos());
        ecosistema.inicializarPoblacion(cfg.plantas(), cfg.conejos(), cfg.lobos());
        intervencion.setEcosistema(ecosistema);
        reporte.setEcosistema(ecosistema);
        return new ResultadoIntervencion(true, "Simulación creada: " + cfg.resumen() + ".");
    }

    // El mensaje de la excepción es el que ve la persona
    private static Configuracion leer(String plantas, String conejos, String lobos, String clima, String turnos) {
        int p = leerEntero(plantas, "plantas", Ecosistema.MIN_PLANTAS, Ecosistema.MAX_PLANTAS);
        int c = leerEntero(conejos, "conejos", Ecosistema.MIN_CONEJOS, Ecosistema.MAX_CONEJOS);
        int l = leerEntero(lobos, "lobos", Ecosistema.MIN_LOBOS, Ecosistema.MAX_LOBOS);
        int t = leerEntero(turnos, "turnos", Ecosistema.MIN_TURNOS, Ecosistema.MAX_TURNOS);
        Clima cl = ControladorIntervencion.parsearClima(clima);
        if (cl == null) {
            throw new IllegalArgumentException("Elegí un clima válido (soleado, lluvioso, sequía o invierno).");
        }
        return new Configuracion(p, c, l, cl, t);
    }

    private static int leerEntero(String texto, String campo, int min, int max) {
        int valor;
        try {
            valor = Integer.parseInt(texto == null ? "" : texto.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La cantidad de " + campo + " debe ser un número entero.");
        }
        if (valor < min || valor > max) {
            throw new IllegalArgumentException("La cantidad de " + campo + " debe estar entre " + min + " y " + max + ".");
        }
        return valor;
    }
}
