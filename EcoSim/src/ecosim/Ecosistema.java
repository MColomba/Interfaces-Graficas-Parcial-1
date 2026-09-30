package ecosim;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * Guarda las poblaciones (plantas, conejos, lobos), el clima y el turno actual,
 * ejecuta cada turno en el orden que pide la consigna y arma el reporte final.
 *
 * ---------------------------------------------------------------------------
 */
public class Ecosistema {

    // ===================== Constantes =====================
    public static final int MAX_LOBOS_TOTALES = 5;       // tope de lobos en toda la simulacion
    public static final int INTERVALO_INTERVENCION = 3;  // el jugador interviene cada 3 turnos

    private static final String[] NOMBRES_CONEJOS = {
        "Blas", "Luna", "Topo", "Rex", "Nube", "Coco", "Pipo", "Trebol", "Canela", "Bruma"
    };
    private static final String[] NOMBRES_LOBOS = {
        "Fang", "Sombra", "Colmillo", "Ceniza", "Nieve", "Bruto", "Aullido", "Gris"
    };

    // ===================== Atributos (todos privados) =====================
    private ArrayList<Planta> plantas;
    private ArrayList<Conejo> conejos;
    private ArrayList<Lobo> lobos;
    private Clima climaActual;
    private int turnoActual;
    private int turnosTotales;

    // Historial de TODO lo que existio alguna vez (sirve para el reporte: longevidad, cacerias)
    private ArrayList<Planta> historialPlantas;
    private ArrayList<Conejo> historialConejos;
    private ArrayList<Lobo> historialLobos;

    // Contadores del reporte
    private int nacimientosPlantas;
    private int nacimientosConejos;
    private int muertesPlantas;
    private int muertesConejos;
    private int muertesLobos;
    private int agregadasPlantasJugador;
    private int agregadosConejosJugador;
    private int agregadosLobosJugador;

    // Actividad por turno (para "turno de mayor actividad")
    private ArrayList<Integer> eventosPorTurno;
    private int eventosTurnoActual;

    // Estadisticas en tiempo real (bonus) 
    private Estadisticas estadisticas;

    // Para generar nombres
    private int contadorPlantas;
    private int contadorConejos;
    private int contadorLobos;
    private Random random;

    // ===================== Constructor =====================
    public Ecosistema(Clima climaInicial, int turnosTotales) {
        if (climaInicial == null) {
            throw new IllegalArgumentException("El clima inicial no puede ser null.");
        }
        if (turnosTotales < 1) {
            throw new IllegalArgumentException("La cantidad de turnos debe ser al menos 1.");
        }
        this.climaActual = climaInicial;
        this.turnosTotales = turnosTotales;
        this.turnoActual = 0;

        this.plantas = new ArrayList<>();
        this.conejos = new ArrayList<>();
        this.lobos = new ArrayList<>();
        this.historialPlantas = new ArrayList<>();
        this.historialConejos = new ArrayList<>();
        this.historialLobos = new ArrayList<>();
        this.eventosPorTurno = new ArrayList<>();
        this.estadisticas = new Estadisticas();
        this.random = new Random();
    }

    // ===================== Getters y setters (con validacion) =====================
    public ArrayList<Planta> getPlantas() { return plantas; }
    public ArrayList<Conejo> getConejos() { return conejos; }
    public ArrayList<Lobo> getLobos() { return lobos; }

    public Clima getClimaActual() { return climaActual; }

    public void setClimaActual(Clima climaActual) {
        if (climaActual == null) {
            throw new IllegalArgumentException("El clima no puede ser null.");
        }
        this.climaActual = climaActual;
    }

    public int getTurnoActual() { return turnoActual; }

    public void setTurnoActual(int turnoActual) {
        if (turnoActual < 0) {
            throw new IllegalArgumentException("El turno no puede ser negativo.");
        }
        this.turnoActual = turnoActual;
    }

    public int getTurnosTotales() { return turnosTotales; }

    public void setTurnosTotales(int turnosTotales) {
        if (turnosTotales < 1) {
            throw new IllegalArgumentException("La cantidad de turnos debe ser al menos 1.");
        }
        this.turnosTotales = turnosTotales;
    }

    // ===================== Consultas de poblaciones vivas =====================
    public ArrayList<Planta> getPlantasVivas() {
        ArrayList<Planta> vivas = new ArrayList<>();
        for (Planta p : plantas) {
            if (p.getViva()) vivas.add(p);
        }
        return vivas;
    }

    public ArrayList<Conejo> getConejosVivos() {
        ArrayList<Conejo> vivos = new ArrayList<>();
        for (Conejo c : conejos) {
            if (c.getViva()) vivos.add(c);
        }
        return vivos;
    }

    public ArrayList<Lobo> getLobosVivos() {
        ArrayList<Lobo> vivos = new ArrayList<>();
        for (Lobo l : lobos) {
            if (l.getViva()) vivos.add(l);
        }
        return vivos;
    }

    public int contarPlantasVivas() { return getPlantasVivas().size(); }
    public int contarConejosVivos() { return getConejosVivos().size(); }
    public int contarLobosVivos() { return getLobosVivos().size(); }

    // ===================== Efectos del clima =====================

    /** Multiplicador de reproduccion de las plantas segun el clima actual. */
    public double getMultiplicadorReproduccionPlantas() {
        if (climaActual == Clima.SOLEADO) return 1.5;
        if (climaActual == Clima.LLUVIOSO) return 2.0;
        if (climaActual == Clima.SEQUIA) return 0.5;
        if (climaActual == Clima.INVIERNO) return 0.0;   // en invierno no se reproducen
        return 1.0;
    }

    /** Bonus (sumar a la probabilidad de caza): +20% en invierno, 0 en el resto. */
    public double getBonusExitoCaza() {
        return (climaActual == Clima.INVIERNO) ? 0.20 : 0.0;
    }

    /** Energia que ganan (+) o pierden (-) los conejos por turno debido al clima. */
    private double efectoClimaConejos() {
        if (climaActual == Clima.SOLEADO) return 5;
        if (climaActual == Clima.LLUVIOSO) return 3;
        if (climaActual == Clima.SEQUIA) return -5;
        if (climaActual == Clima.INVIERNO) return -8;
        return 0;
    }

    /** Energia que ganan (+) o pierden (-) los lobos por turno debido al clima. */
    private double efectoClimaLobos() {
        return (climaActual == Clima.LLUVIOSO) ? -5 : 0;
    }

    /** Nombre  del clima para mostrar en pantalla. */
    public static String nombreClima(Clima clima) {
        if (clima == Clima.SOLEADO) return "Soleado";
        if (clima == Clima.LLUVIOSO) return "Lluvioso";
        if (clima == Clima.SEQUIA) return "Sequia";
        if (clima == Clima.INVIERNO) return "Invierno";
        return "Desconocido";
    }

    public void cambiarClima(Clima nuevo) {
        if (nuevo == null) {
            System.out.println("No se puede cambiar el clima: valor invalido.");
            return;
        }
        String anterior = nombreClima(climaActual);
        setClimaActual(nuevo);
        System.out.println("El clima cambio de " + anterior + " a " + nombreClima(nuevo) + ".");
    }

    // ===================== Nombres y creacion de entidades =====================

    /** Devuelve "planta", "conejo" o "lobo" (o null si el texto no es un tipo valido). */
    public static String normalizarTipo(String tipo) {
        if (tipo == null) return null;
        String t = tipo.trim().toLowerCase();
        if (t.equals("planta") || t.equals("conejo") || t.equals("lobo")) return t;
        return null;
    }

    /** Genera un nombre unico para una entidad nueva del tipo indicado (null si el tipo no existe). */
    public String generarNombre(String tipo) {
        String t = normalizarTipo(tipo);
        if (t == null) return null;
        if (t.equals("planta")) {
            contadorPlantas++;
            return "Helecho-" + contadorPlantas;
        }
        if (t.equals("conejo")) {
            contadorConejos++;
            return nombreDePool(NOMBRES_CONEJOS, contadorConejos);
        }
        contadorLobos++;
        return nombreDePool(NOMBRES_LOBOS, contadorLobos);
    }

    // Usa la lista de nombres y, cuando se acaba, repite con un numero (Blas-2, Blas-3...)
    private String nombreDePool(String[] pool, int numero) {
        String base = pool[(numero - 1) % pool.length];
        int vuelta = (numero - 1) / pool.length;
        return (vuelta == 0) ? base : base + "-" + (vuelta + 1);
    }

    private double energiaAleatoria(int min, int max) {
        return min + random.nextInt(max - min + 1);
    }

    private double energiaAleatoriaPara(String tipo) {
        if (tipo.equals("planta")) return energiaAleatoria(30, 60);
        if (tipo.equals("conejo")) return energiaAleatoria(40, 80);
        return energiaAleatoria(60, 100);   // lobo
    }

    private Planta crearPlanta(double energia) {
        int tamanio = 1 + random.nextInt(5);   // 1 a 5
        return new Planta(generarNombre("planta"), energia, 0, true, tamanio);
    }

    private Conejo crearConejo(double energia) {
        int velocidad = 20 + random.nextInt(21);                          // 20 a 40
        double peso = Math.round((1.5 + random.nextDouble() * 2.0) * 10) / 10.0;  // ~1.5 a 3.5 kg
        return new Conejo(generarNombre("conejo"), energia, 0, true, velocidad, peso);
    }

    private Lobo crearLobo(double energia) {
        int velocidad = 40 + random.nextInt(21);                          // 40 a 60
        double peso = Math.round((25 + random.nextDouble() * 20) * 10) / 10.0;    // ~25 a 45 kg
        return new Lobo(generarNombre("lobo"), energia, 0, true, velocidad, peso, 0);
    }

    // Registrar = meter en la lista viva + en el historial
    private void registrarPlanta(Planta p) { plantas.add(p); historialPlantas.add(p); }
    private void registrarConejo(Conejo c) { conejos.add(c); historialConejos.add(c); }
    private void registrarLobo(Lobo l) { lobos.add(l); historialLobos.add(l); }

    /** Crea la poblacion inicial (energia aleatoria en rangos razonables). No cuenta como "agregado por el jugador". */
    public void inicializarPoblacion(int cantPlantas, int cantConejos, int cantLobos) {
        if (cantPlantas < 0 || cantConejos < 0 || cantLobos < 0) {
            throw new IllegalArgumentException("Las cantidades no pueden ser negativas.");
        }
        if (cantLobos > MAX_LOBOS_TOTALES) {
            throw new IllegalArgumentException("No puede haber mas de " + MAX_LOBOS_TOTALES + " lobos.");
        }
        for (int i = 0; i < cantPlantas; i++) registrarPlanta(crearPlanta(energiaAleatoriaPara("planta")));
        for (int i = 0; i < cantConejos; i++) registrarConejo(crearConejo(energiaAleatoriaPara("conejo")));
        for (int i = 0; i < cantLobos; i++) registrarLobo(crearLobo(energiaAleatoriaPara("lobo")));
    }

    /** Las plantas y conejos nuevos que nacen por reproduccion entran por aca (cuenta como nacimiento). */
    public void agregarPlanta(Planta nueva) {
        if (nueva == null) return;
        registrarPlanta(nueva);
        nacimientosPlantas++;
    }

    public void agregarConejo(Conejo nuevo) {
        if (nuevo == null) return;
        registrarConejo(nuevo);
        nacimientosConejos++;
    }

    /** ¿Se puede agregar otro lobo? (maximo 5 en TODA la simulacion, contando los iniciales). */
    public boolean puedeAgregarLobo() {
        return historialLobos.size() < MAX_LOBOS_TOTALES;
    }

    public int lobosQuePuedenAgregarse() {
        return Math.max(0, MAX_LOBOS_TOTALES - historialLobos.size());
    }

    /**
     * SOBRECARGA 1: agrega una entidad con energia aleatoria.
     * @return la entidad creada, o null si no se pudo agregar.
     */
    public Entidad agregarEntidad(String tipo) {
        String t = normalizarTipo(tipo);
        if (t == null) {
            System.out.println("Tipo de entidad invalido: '" + tipo + "'. Use planta, conejo o lobo.");
            return null;
        }
        return agregarEntidad(t, energiaAleatoriaPara(t));
    }

    /**
     * SOBRECARGA 2: agrega una entidad con la energia inicial indicada.
     * @return la entidad creada, o null si no se pudo agregar.
     */
    public Entidad agregarEntidad(String tipo, double energiaInicial) {
        String t = normalizarTipo(tipo);
        if (t == null) {
            System.out.println("Tipo de entidad invalido: '" + tipo + "'. Use planta, conejo o lobo.");
            return null;
        }
        if (energiaInicial <= 0) {
            System.out.println("La energia inicial debe ser mayor que 0.");
            return null;
        }

        if (t.equals("planta")) {
            Planta p = crearPlanta(energiaInicial);
            registrarPlanta(p);
            agregadasPlantasJugador++;
            return p;
        }
        if (t.equals("conejo")) {
            Conejo c = crearConejo(energiaInicial);
            registrarConejo(c);
            agregadosConejosJugador++;
            return c;
        }
        // lobo
        if (!puedeAgregarLobo()) {
            System.out.println("No se pueden agregar mas lobos: el maximo es " + MAX_LOBOS_TOTALES
                    + " en toda la simulacion.");
            return null;
        }
        Lobo l = crearLobo(energiaInicial);
        registrarLobo(l);
        agregadosLobosJugador++;
        return l;
    }

    // ===================== Eventos =====================

    /** Muestra un evento del turno y lo suma a la actividad del turno. */
    public void registrarEvento(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) return;
        System.out.println(descripcion);
        eventosTurnoActual++;
    }

    // ===================== Estado y fin de simulacion =====================

    private String lineaConteo() {
        return "Plantas: " + contarPlantasVivas()
                + "  Conejos: " + contarConejosVivos()
                + "  Lobos: " + contarLobosVivos();
    }

    public void mostrarEstado() {
        System.out.println("Estado: " + lineaConteo() + "  | Clima: " + nombreClima(climaActual));
    }

    /** true si alguna de las tres poblaciones llego a 0. */
    public boolean ecosistemaColapsado() {
        return contarPlantasVivas() == 0 || contarConejosVivos() == 0 || contarLobosVivos() == 0;
    }

    /** Lista con el nombre de las poblaciones extinguidas ("plantas", "conejos", "lobos"). */
    public ArrayList<String> getPoblacionesExtintas() {
        ArrayList<String> extintas = new ArrayList<>();
        if (contarPlantasVivas() == 0) extintas.add("plantas");
        if (contarConejosVivos() == 0) extintas.add("conejos");
        if (contarLobosVivos() == 0) extintas.add("lobos");
        return extintas;
    }

    /** La simulacion termina al completar los turnos o si el ecosistema colapsa. */
    public boolean simulacionTerminada() {
        return turnoActual >= turnosTotales || ecosistemaColapsado();
    }

    /** true si despues del turno que se acaba de jugar toca el menu de intervencion. */
    public boolean esTurnoDeIntervencion() {
        return turnoActual > 0
                && turnoActual % INTERVALO_INTERVENCION == 0
                && !simulacionTerminada();
    }

    // ===================== Turno =====================

    /**
     * Ejecuta un turno completo, en este orden:
     *  1. Plantas y conejos se reproducen (un solo recorrido con ArrayList<Reproducible>)
     *  2. Conejos comen
     *  3. Lobos cazan
     *  4. Todos envejecen / gastan energia base (+ efecto del clima)
     *  5. Se verifican las muertes
     *  6. Se muestran los eventos (a medida que ocurren, bajo "-- Eventos --")
     *  7. Se muestra el estado
     */
    public void procesarTurno() {
        turnoActual++;
        eventosTurnoActual = 0;

        System.out.println();
        System.out.println("=== TURNO " + turnoActual + " | Clima: " + nombreClima(climaActual) + " ===");
        System.out.println(lineaConteo());
        System.out.println("-- Eventos --");

        faseReproduccion();      // 1
        faseConejosComen();      // 2
        faseLobosCazan();        // 3
        faseEnvejecimiento();    // 4
        faseMuertes();           // 5

        if (eventosTurnoActual == 0) {
            System.out.println("(sin eventos)");
        }

        // Guardar datos del turno para el reporte
        eventosPorTurno.add(eventosTurnoActual);
        estadisticas.registrarTurno(turnoActual, contarPlantasVivas(), contarConejosVivos(), contarLobosVivos());

        mostrarEstado();         // 7
    }

    // 1. POLIMORFISMO: plantas y conejos se procesan en el mismo recorrido
    private void faseReproduccion() {
        ArrayList<Reproducible> reproducibles = new ArrayList<>();
        reproducibles.addAll(getPlantasVivas());
        reproducibles.addAll(getConejosVivos());
        // Recorremos una copia: las crias nuevas se agregan a las listas reales sin romper el for
        for (Reproducible r : reproducibles) {
            r.intentarReproduccion(this);
        }
    }

    // 2. Conejos buscan plantas y comen (la reproduccion ya se resolvio en el paso 1)
    private void faseConejosComen() {
        for (Conejo c : getConejosVivos()) {
            if (c.getViva()) {
                c.comer(this);
            }
        }
    }

    // 3. Lobos intentan cazar
    private void faseLobosCazan() {
        for (Lobo l : getLobosVivos()) {
            if (l.getViva()) {
                l.actuar(this);
            }
        }
    }

    // 4. Envejecer, gastar energia base y aplicar el efecto del clima
    private void faseEnvejecimiento() {
        for (Planta p : getPlantasVivas()) {
            p.envejecer();
        }
        for (Conejo c : getConejosVivos()) {
            c.envejecer();
            c.setEnergia(c.getEnergia() + efectoClimaConejos());
        }
        for (Lobo l : getLobosVivos()) {
            l.envejecer();
            l.setEnergia(l.getEnergia() + efectoClimaLobos());
        }
    }

    // 5. Verificar muertes y sacar a los muertos de las listas
    private void faseMuertes() {
        // Animales: Mortal.verificarMuerte() (metodo default de la interface).
        // Importante: hay que llamarlo mientras la entidad TODAVIA figura viva (getViva()==true).
        // Adentro, verificarMuerte() se fija si estaVivo() dio false (energia <= 0) y ahi
        // recien llama a morir(). Si filtraramos por estaVivo() en vez de getViva(), nunca se
        // dispararia morir(), porque estaVivo() ya daria false antes de entrar al metodo.
        for (Conejo c : conejos) {
            if (c.getViva()) {
                c.verificarMuerte();
            }
        }
        for (Lobo l : lobos) {
            if (l.getViva()) {
                l.verificarMuerte();
            }
        }

        // Plantas: no son Mortal, asi que se controla aca
        for (Planta p : plantas) {
            if (p.getViva() && p.getEnergia() <= 0) {
                p.setViva(false);
                registrarEvento("Planta '" + p.getNombre() + "' murio (sin energia)");
            }
        }

        muertesPlantas += retirarMuertas(plantas);

        int conejosMuertos = retirarMuertas(conejos);
        int lobosMuertos = retirarMuertas(lobos);
        muertesConejos += conejosMuertos;
        muertesLobos += lobosMuertos;
        // Las muertes de animales las imprime verificarMuerte(); aca solo suman actividad
        eventosTurnoActual += conejosMuertos + lobosMuertos;
    }

    // Saca de la lista las entidades que ya no estan vivas y devuelve cuantas saco
    private int retirarMuertas(ArrayList<? extends Entidad> lista) {
        int antes = lista.size();
        lista.removeIf(e -> !e.getViva());
        return antes - lista.size();
    }

    // ===================== Reporte final =====================

    public void generarReporteFinal() {
        System.out.println();
        System.out.println("==================================================");
        System.out.println("           REPORTE FINAL DE LA SIMULACION");
        System.out.println("==================================================");

        // --- Causa de fin ---
        if (ecosistemaColapsado()) {
            System.out.println("Causa de fin: COLAPSO del ecosistema en el turno " + turnoActual + ".");
            System.out.println("Poblacion extinguida: " + String.join(", ", getPoblacionesExtintas()) + ".");
        } else {
            System.out.println("Causa de fin: se completaron los " + turnosTotales + " turnos configurados.");
        }
        System.out.println("Turnos jugados: " + turnoActual + " de " + turnosTotales);
        System.out.println("Clima final: " + nombreClima(climaActual));
        System.out.println("Poblacion final -> " + lineaConteo());

        // --- Turno de mayor actividad ---
        System.out.println();
        System.out.println("-- Actividad --");
        int mejorTurno = -1;
        int mejorCantidad = 0;
        for (int i = 0; i < eventosPorTurno.size(); i++) {
            if (eventosPorTurno.get(i) > mejorCantidad) {
                mejorCantidad = eventosPorTurno.get(i);
                mejorTurno = i + 1;
            }
        }
        if (mejorTurno == -1) {
            System.out.println("Turno de mayor actividad: no se registraron eventos.");
        } else {
            System.out.println("Turno de mayor actividad: turno " + mejorTurno + " (" + mejorCantidad + " eventos)");
        }

        // --- Mas longevas ---
        System.out.println();
        System.out.println("-- Entidades mas longevas --");
        imprimirLongeva("Planta", masLongeva(historialPlantas));
        imprimirLongeva("Conejo", masLongeva(historialConejos));
        imprimirLongeva("Lobo", masLongeva(historialLobos));

        // --- Lobo con mas cacerias ---
        Lobo cazador = null;
        for (Lobo l : historialLobos) {
            if (cazador == null || l.getExitosCaza() > cazador.getExitosCaza()) {
                cazador = l;
            }
        }
        if (cazador != null && cazador.getExitosCaza() > 0) {
            System.out.println("Lobo con mas cacerias exitosas: '" + cazador.getNombre()
                    + "' (" + cazador.getExitosCaza() + ")");
        } else {
            System.out.println("Lobo con mas cacerias exitosas: ningun lobo logro cazar.");
        }

        // --- Nacimientos y muertes ---
        System.out.println();
        System.out.println("-- Nacimientos (por reproduccion) --");
        System.out.println("Plantas: " + nacimientosPlantas + " | Conejos: " + nacimientosConejos
                + " | Lobos: 0 (los lobos no se reproducen)");
        System.out.println("Agregados por el jugador: Plantas: " + agregadasPlantasJugador
                + " | Conejos: " + agregadosConejosJugador + " | Lobos: " + agregadosLobosJugador);
        System.out.println("-- Muertes --");
        System.out.println("Plantas: " + muertesPlantas + " | Conejos: " + muertesConejos
                + " | Lobos: " + muertesLobos);

        // --- Bonus: estadisticas de poblacion (clase Estadisticas de Mati) ---
        System.out.println();
        estadisticas.mostrarReporteEstadistico();

        // --- Bonus: elementos peligrosos ordenados por nivel ---
        System.out.println();
        System.out.println("-- Elementos peligrosos (mayor a menor nivel) --");
        imprimirPeligrosos();

        System.out.println("==================================================");
    }

    // Devuelve la entidad con mas edad de la lista (o null si esta vacia)
    private Entidad masLongeva(ArrayList<? extends Entidad> lista) {
        Entidad mejor = null;
        for (Entidad e : lista) {
            if (mejor == null || e.getEdad() > mejor.getEdad()) {
                mejor = e;
            }
        }
        return mejor;
    }

    private void imprimirLongeva(String tipo, Entidad e) {
        if (e == null) {
            System.out.println(tipo + " mas longevo/a: no hubo.");
        } else {
            System.out.println(tipo + " mas longevo/a: '" + e.getNombre() + "' (edad: " + e.getEdad() + ")");
        }
    }

    // Junta todo lo que implemente Peligroso (Lobo, PlantaVenenosa...) y lo ordena por nivel
    private void imprimirPeligrosos() {
        ArrayList<Entidad> vivas = new ArrayList<>();
        vivas.addAll(plantas);
        vivas.addAll(conejos);
        vivas.addAll(lobos);

        ArrayList<Entidad> peligrosos = new ArrayList<>();
        for (Entidad e : vivas) {
            if (e instanceof Peligroso && e.getViva()) {
                peligrosos.add(e);
            }
        }

        if (peligrosos.isEmpty()) {
            System.out.println("No quedan elementos peligrosos en el ecosistema.");
            return;
        }

        peligrosos.sort((a, b) -> Integer.compare(
                ((Peligroso) b).getNivelPeligro(), ((Peligroso) a).getNivelPeligro()));

        for (Entidad e : peligrosos) {
            System.out.println("Nivel " + ((Peligroso) e).getNivelPeligro() + " - " + e.getNombre());
        }
    }
}
