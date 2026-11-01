package controlador;

import modelo.ResultadoIntervencion;
import vista.PanelIntervencion;
import vista.PanelReporte;

/** Pruebas sin JUnit: ejecutar main; lanza AssertionError si algo falla. */
public class ControladorConfiguracionTest {
    static ControladorConfiguracion cc;

    static void check(boolean ok, String que) {
        if (!ok) throw new AssertionError(que);
        System.out.println("OK  " + que);
    }

    static boolean falla(String p, String c, String l, String clima, String t) {
        ResultadoIntervencion r = cc.iniciarSimulacion(p, c, l, clima, t);
        return !r.isExito() && cc.getEcosistema() == null;
    }

    public static void main(String[] args) {
        cc = new ControladorConfiguracion(new ControladorIntervencion(new PanelIntervencion()),
                new ControladorReporte(new PanelReporte()));

        check(falla("4", "2", "1", "Soleado", "10"), "plantas bajo el minimo");
        check(falla("31", "2", "1", "Soleado", "10"), "plantas sobre el maximo");
        check(falla("5", "1", "1", "Soleado", "10"), "conejos bajo el minimo");
        check(falla("5", "16", "1", "Soleado", "10"), "conejos sobre el maximo");
        check(falla("5", "2", "0", "Soleado", "10"), "lobos bajo el minimo");
        check(falla("5", "2", "6", "Soleado", "10"), "lobos sobre el maximo");
        check(falla("5", "2", "1", "Soleado", "9"), "turnos bajo el minimo");
        check(falla("5", "2", "1", "Soleado", "51"), "turnos sobre el maximo");
        check(falla("abc", "2", "1", "Soleado", "10"), "texto no numerico");
        check(falla("", "2", "1", "Soleado", "10"), "campo vacio");
        check(falla(null, "2", "1", "Soleado", "10"), "campo null");
        check(falla("5", "2", "1", "Nublado", "10"), "clima invalido");
        check(falla("5", "2", "1", null, "10"), "clima null");

        ResultadoIntervencion ok = cc.iniciarSimulacion(" 5 ", "2", "1", "Sequía", "10");
        check(ok.isExito() && cc.getEcosistema() != null, "datos validos crean la simulacion");
        check(cc.getEcosistema().contarPlantasVivas() == 5 && cc.getEcosistema().contarConejosVivos() == 2
                && cc.getEcosistema().contarLobosVivos() == 1 && cc.getEcosistema().getTurnosTotales() == 10,
                "poblacion y turnos segun la configuracion");

        cc.iniciarSimulacion("30", "15", "5", "invierno", "50");
        check(cc.getEcosistema().contarPlantasVivas() == 30 && cc.getEcosistema().getTurnosTotales() == 50,
                "maximos validos");
    }
}
