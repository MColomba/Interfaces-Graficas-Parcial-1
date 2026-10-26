/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ecosim;

/**
 *
 * @author bruno
 */
public class ReporteFinal {
    
    // --- Cómo terminó ---
    private boolean colapso;
    private String causaFin;          // texto listo para mostrar
    private int turnosJugados;
    private int turnosTotales;
    private String climaFinal;
 
    // --- Población al terminar ---
    private int plantasFinal;
    private int conejosFinal;
    private int lobosFinal;
 
    // --- Actividad ---
    private int turnoMayorActividad;   // 0 si no hubo eventos
    private int eventosMayorActividad;
 
    // --- Más longevos (texto listo: "Nombre (edad: N)" o "No hubo") ---
    private String plantaLongeva;
    private String conejoLongevo;
    private String loboLongevo;
 
    // --- Lobo con más cacerías ---
    private String loboCazador;        // null si ninguno cazó
    private int cazasLoboCazador;
 
    // --- Nacimientos y muertes por tipo ---
    private int nacimientosPlantas;
    private int nacimientosConejos;
    private int muertesPlantas;
    private int muertesConejos;
    private int muertesLobos;
 
    // --- Agregados por el jugador ---
    private int agregadasPlantas;
    private int agregadosConejos;
    private int agregadosLobos;
 
    // Getters (la vista y el controlador leen con estos)
    public boolean isColapso() { return colapso; }
    public String getCausaFin() { return causaFin; }
    public int getTurnosJugados() { return turnosJugados; }
    public int getTurnosTotales() { return turnosTotales; }
    public String getClimaFinal() { return climaFinal; }
    public int getPlantasFinal() { return plantasFinal; }
    public int getConejosFinal() { return conejosFinal; }
    public int getLobosFinal() { return lobosFinal; }
    public int getTurnoMayorActividad() { return turnoMayorActividad; }
    public int getEventosMayorActividad() { return eventosMayorActividad; }
    public String getPlantaLongeva() { return plantaLongeva; }
    public String getConejoLongevo() { return conejoLongevo; }
    public String getLoboLongevo() { return loboLongevo; }
    public String getLoboCazador() { return loboCazador; }
    public int getCazasLoboCazador() { return cazasLoboCazador; }
    public int getNacimientosPlantas() { return nacimientosPlantas; }
    public int getNacimientosConejos() { return nacimientosConejos; }
    public int getMuertesPlantas() { return muertesPlantas; }
    public int getMuertesConejos() { return muertesConejos; }
    public int getMuertesLobos() { return muertesLobos; }
    public int getAgregadasPlantas() { return agregadasPlantas; }
    public int getAgregadosConejos() { return agregadosConejos; }
    public int getAgregadosLobos() { return agregadosLobos; }
 
    // Setters sin "public": solo los usa Ecosistema (mismo paquete), nadie más puede modificar el reporte
    void setColapso(boolean colapso) { this.colapso = colapso; }
    void setCausaFin(String causaFin) { this.causaFin = causaFin; }
    void setTurnosJugados(int turnosJugados) { this.turnosJugados = turnosJugados; }
    void setTurnosTotales(int turnosTotales) { this.turnosTotales = turnosTotales; }
    void setClimaFinal(String climaFinal) { this.climaFinal = climaFinal; }
    void setPlantasFinal(int plantasFinal) { this.plantasFinal = plantasFinal; }
    void setConejosFinal(int conejosFinal) { this.conejosFinal = conejosFinal; }
    void setLobosFinal(int lobosFinal) { this.lobosFinal = lobosFinal; }
    void setTurnoMayorActividad(int turnoMayorActividad) { this.turnoMayorActividad = turnoMayorActividad; }
    void setEventosMayorActividad(int eventosMayorActividad) { this.eventosMayorActividad = eventosMayorActividad; }
    void setPlantaLongeva(String plantaLongeva) { this.plantaLongeva = plantaLongeva; }
    void setConejoLongevo(String conejoLongevo) { this.conejoLongevo = conejoLongevo; }
    void setLoboLongevo(String loboLongevo) { this.loboLongevo = loboLongevo; }
    void setLoboCazador(String loboCazador) { this.loboCazador = loboCazador; }
    void setCazasLoboCazador(int cazasLoboCazador) { this.cazasLoboCazador = cazasLoboCazador; }
    void setNacimientosPlantas(int n) { this.nacimientosPlantas = n; }
    void setNacimientosConejos(int n) { this.nacimientosConejos = n; }
    void setMuertesPlantas(int n) { this.muertesPlantas = n; }
    void setMuertesConejos(int n) { this.muertesConejos = n; }
    void setMuertesLobos(int n) { this.muertesLobos = n; }
    void setAgregadasPlantas(int n) { this.agregadasPlantas = n; }
    void setAgregadosConejos(int n) { this.agregadosConejos = n; }
    void setAgregadosLobos(int n) { this.agregadosLobos = n; }
}
