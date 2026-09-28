/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ecosim;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * EcoSim: clase principal (Main) del Simulador de Ecosistema 
 *
 * Se encarga de:
 *   1. La configuración inicial (con validación de rangos y confirmación)
 *   2. El loop principal: un turno por cada Enter
 *   3. El menú de intervención cada 3 turnos
 *   4. Mostrar el reporte final
 */
public class EcoSim {

    // Rangos que pide la consigna
    private static final int MIN_PLANTAS = 5,  MAX_PLANTAS = 30;
    private static final int MIN_CONEJOS = 2,  MAX_CONEJOS = 15;
    private static final int MIN_LOBOS   = 1,  MAX_LOBOS   = 5;
    private static final int MIN_TURNOS  = 10, MAX_TURNOS  = 50;

    private static final Clima[] CLIMAS = {
        Clima.SOLEADO, Clima.LLUVIOSO, Clima.SEQUIA, Clima.INVIERNO
    };

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            System.out.println("==================================================");
            System.out.println("            SIMULADOR DE ECOSISTEMA");
            System.out.println("        Plantas  -  Conejos  -  Lobos");
            System.out.println("==================================================");

            Ecosistema eco = configurarSimulacion();
            ejecutarSimulacion(eco);

            System.out.println();
            System.out.println("=== FIN DE LA SIMULACIÓN ===");
            eco.generarReporteFinal();
        } catch (NoSuchElementException e) {
            // Pasa si se cierra la entrada (Ctrl+D / Ctrl+Z) en medio de la simulación
            System.out.println();
            System.out.println("Se cerró la entrada de datos. Simulación interrumpida.");
        } finally {
            scanner.close();
        }
    }

    // =====================================================================
    // 1. CONFIGURACIÓN INICIAL
    // =====================================================================

    private static Ecosistema configurarSimulacion() {
        while (true) {
            System.out.println();
            System.out.println("=== CONFIGURACIÓN INICIAL ===");

            int plantas = leerEnteroEnRango("Cantidad inicial de plantas", MIN_PLANTAS, MAX_PLANTAS);
            int conejos = leerEnteroEnRango("Cantidad inicial de conejos", MIN_CONEJOS, MAX_CONEJOS);
            int lobos   = leerEnteroEnRango("Cantidad inicial de lobos", MIN_LOBOS, MAX_LOBOS);
            Clima clima = leerClima("Clima inicial");
            int turnos  = leerEnteroEnRango("Cantidad de turnos totales", MIN_TURNOS, MAX_TURNOS);

            System.out.println();
            System.out.println("--- Resumen de la configuración ---");
            System.out.println("Plantas: " + plantas);
            System.out.println("Conejos: " + conejos);
            System.out.println("Lobos:   " + lobos);
            System.out.println("Clima:   " + Ecosistema.nombreClima(clima));
            System.out.println("Turnos:  " + turnos);

            if (confirmar("¿Confirmar configuración e iniciar?")) {
                Ecosistema eco = new Ecosistema(clima, turnos);
                eco.inicializarPoblacion(plantas, conejos, lobos);
                return eco;
            }
            System.out.println("Configuración descartada. Ingresá los datos de nuevo.");
        }
    }

    // =====================================================================
    // 2. LOOP PRINCIPAL
    // =====================================================================

    private static void ejecutarSimulacion(Ecosistema eco) {
        System.out.println();
        System.out.println("Ecosistema creado.");
        eco.mostrarEstado();
        esperarEnter(">>> Presione Enter para comenzar...");

        while (true) {
            eco.procesarTurno();

            if (eco.simulacionTerminada()) {
                break;   // se cumplieron los turnos o el ecosistema colapsó
            }

            esperarEnter(">>> Presione Enter para continuar...");

            if (eco.esTurnoDeIntervencion()) {
                menuIntervencion(eco);
            }
        }
    }

    // =====================================================================
    // 3. INTERVENCIÓN (cada 3 turnos)
    // =====================================================================

    private static void menuIntervencion(Ecosistema eco) {
        boolean listo = false;
        while (!listo) {
            System.out.println();
            System.out.println("=== INTERVENCIÓN (cada " + Ecosistema.INTERVALO_INTERVENCION + " turnos) ===");
            System.out.println("1. Cambiar clima (actual: " + Ecosistema.nombreClima(eco.getClimaActual()) + ")");
            System.out.println("2. Agregar entidad");
            System.out.println("3. Solo avanzar");

            int opcion = leerEnteroEnRango("Opción", 1, 3);

            if (opcion == 1) {
                listo = intervenirClima(eco);
            } else if (opcion == 2) {
                listo = intervenirAgregar(eco);
            } else {
                listo = true;   // solo avanzar
            }
        }
    }

    // Devuelve true si se ejecutó el cambio; false si se canceló (vuelve al menú)
    private static boolean intervenirClima(Ecosistema eco) {
        Clima nuevo = leerClima("Nuevo clima");

        if (nuevo == eco.getClimaActual()) {
            System.out.println("Ese ya es el clima actual. No hay nada que cambiar.");
            return false;
        }
        if (!confirmar("¿Cambiar el clima a " + Ecosistema.nombreClima(nuevo) + "?")) {
            System.out.println("Acción cancelada.");
            return false;
        }
        eco.cambiarClima(nuevo);
        return true;
    }

    // Devuelve true si se agregó la entidad; false si se canceló o no se pudo (vuelve al menú)
    private static boolean intervenirAgregar(Ecosistema eco) {
        String tipo = null;
        while (tipo == null) {
            System.out.print("¿Qué entidad agregar? (planta/conejo/lobo, o 'volver'): ");
            String texto = scanner.nextLine().trim();

            if (texto.equalsIgnoreCase("volver")) {
                return false;
            }
            tipo = Ecosistema.normalizarTipo(texto);
            if (tipo == null) {
                System.out.println("Opción inválida. Escriba planta, conejo o lobo.");
            }
        }

        if (tipo.equals("lobo") && !eco.puedeAgregarLobo()) {
            System.out.println("No se pueden agregar más lobos: el máximo es "
                    + Ecosistema.MAX_LOBOS_TOTALES + " en toda la simulación.");
            return false;
        }

        if (!confirmar("¿Agregar un/a " + tipo + " al ecosistema?")) {
            System.out.println("Acción cancelada.");
            return false;
        }

        Entidad nueva = eco.agregarEntidad(tipo);   // energía aleatoria (sobrecarga sin energía)
        if (nueva == null) {
            return false;
        }
        System.out.println("Se agregó '" + nueva.getNombre() + "' al ecosistema.");
        return true;
    }

    // =====================================================================
    // Utilidades de entrada (Scanner con validación)
    // =====================================================================

    /** Pide un entero y repite hasta que sea válido y esté entre min y max. */
    private static int leerEnteroEnRango(String mensaje, int min, int max) {
        while (true) {
            System.out.print(mensaje + " (" + min + "-" + max + "): ");
            String texto = scanner.nextLine().trim();
            try {
                int valor = Integer.parseInt(texto);
                if (valor >= min && valor <= max) {
                    return valor;
                }
                System.out.println("Valor fuera de rango. Debe estar entre " + min + " y " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Ingrese un número entero.");
            }
        }
    }

    /** Muestra los 4 climas y devuelve el elegido. */
    private static Clima leerClima(String mensaje) {
        System.out.println(mensaje + ":");
        for (int i = 0; i < CLIMAS.length; i++) {
            System.out.println("  " + (i + 1) + ". " + Ecosistema.nombreClima(CLIMAS[i]));
        }
        int opcion = leerEnteroEnRango("Elija un clima", 1, CLIMAS.length);
        return CLIMAS[opcion - 1];
    }

    /** Pregunta s/n y repite hasta recibir una respuesta válida. */
    private static boolean confirmar(String pregunta) {
        while (true) {
            System.out.print(pregunta + " (s/n): ");
            String r = scanner.nextLine().trim().toLowerCase();
            if (r.equals("s") || r.equals("si") || r.equals("sí")) return true;
            if (r.equals("n") || r.equals("no")) return false;
            System.out.println("Respuesta inválida. Escriba 's' o 'n'.");
        }
    }

    private static void esperarEnter(String mensaje) {
        System.out.println(mensaje);
        scanner.nextLine();
    }
}
