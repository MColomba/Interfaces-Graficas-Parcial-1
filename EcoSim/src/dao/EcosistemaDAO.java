/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import modelo.*;
import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author Maxi
 */
public class EcosistemaDAO {
    private static final String RUTA_ARCHIVO = "ecosistema_guardado.txt";

    /**
     * 3. Implementar el guardado completo de la simulación.
     * Recibe el ecosistema actual y escribe su estado turno, clima y entidades en el .txt.
     */
    public static void guardar(Ecosistema ecosistema) throws IOException {
        if (ecosistema == null) {
            throw new IllegalArgumentException("No hay un ecosistema activo para guardar.");
        }

        // PrintWriter y FileWriter abren el archivo para escritura (si no existe, lo crea)
        try (PrintWriter escritor = new PrintWriter(new FileWriter(RUTA_ARCHIVO))) {
            // 2. Definir formato del archivo: Guardamos metadatos generales
            escritor.println("CLIMA=" + ecosistema.getClimaActual().name());
            escritor.println("TURNO=" + ecosistema.getTurnoActual());
            escritor.println("TURNOS_TOTALES=" + ecosistema.getTurnosTotales());

            // Guardar Plantas Vivas (Formato: PLANTA=Nombre,Energia,Edad,Viva,Tamanio)
            for (Planta p : ecosistema.getPlantasVivas()) {
                escritor.println("PLANTA=" + p.getNombre() + "," + p.getEnergia() + "," + p.getEdad() + "," + p.getViva() + "," + p.getTamanio());
            }

            // Guardar Conejos Vivos (Formato: CONEJO=Nombre,Energia,Edad,Viva,Velocidad,Peso)
            for (Conejo c : ecosistema.getConejosVivos()) {
                escritor.println("CONEJO=" + c.getNombre() + "," + c.getEnergia() + "," + c.getEdad() + "," + c.getViva() + "," + c.getVelocidad() + "," + c.getPeso());
            }

            // Guardar Lobos Vivos (Formato: LOBO=Nombre,Energia,Edad,Viva,Velocidad,Peso,ExitosCaza)
            for (Lobo l : ecosistema.getLobosVivos()) {
                escritor.println("LOBO=" + l.getNombre() + "," + l.getEnergia() + "," + l.getEdad() + "," + l.getViva() + "," + l.getVelocidad() + "," + l.getPeso() + "," + l.getExitosCaza());
            }
        }
    }

    /**
     * 4. Implementar la lectura del archivo y la reconstrucción de la simulación.
     * 5. Manejo de errores si el archivo no existe, está incompleto o tiene datos inválidos.
     */
    public static Ecosistema cargar() throws Exception {
        File archivo = new File(RUTA_ARCHIVO);
        
        // Manejo de error si el archivo no existe
        if (!archivo.exists()) {
            throw new FileNotFoundException("No se encontró ningún archivo de guardado previo.");
        }

        Clima clima = Clima.SOLEADO; // Clima por defecto temporal
        int turno = 0;
        int turnosTotales = 20;

        ArrayList<Planta> plantasCargadas = new ArrayList<>();
        ArrayList<Conejo> conejosCargados = new ArrayList<>();
        ArrayList<Lobo> lobosCargados = new ArrayList<>();

        // BufferedReader lee el archivo línea por línea de forma eficiente
        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            int numeroLinea = 0;

            while ((linea = lector.readLine()) != null) {
                numeroLinea++;
                linea = linea.trim();
                if (linea.isEmpty()) continue; // Ignorar líneas vacías

                // Separar la clave y el valor usando el signo '='
                String[] partes = linea.split("=");
                if (partes.length != 2) {
                    throw new IOException("Formato inválido en la línea " + numeroLinea + ".");
                }

                String clave = partes[0].trim();
                String valor = partes[1].trim();

                // Analizar según el tipo de dato almacenado
                switch (clave) {
                    case "CLIMA":
                        clima = Clima.valueOf(valor); // Convierte texto a enum Clima
                        break;
                    case "TURNO":
                        turno = Integer.parseInt(valor);
                        break;
                    case "TURNOS_TOTALES":
                        turnosTotales = Integer.parseInt(valor);
                        break;
                    case "PLANTA":
                        // Separar los atributos de la planta separados por comas
                        String[] datosP = valor.split(",");
                        if (datosP.length < 5) throw new IOException("Datos incompletos para planta en línea " + numeroLinea);
                        plantasCargadas.add(new Planta(datosP[0], Double.parseDouble(datosP[1]), Integer.parseInt(datosP[2]), Boolean.parseBoolean(datosP[3]), Integer.parseInt(datosP[4])));
                        break;
                    case "CONEJO":
                        String[] datosC = valor.split(",");
                        if (datosC.length < 6) throw new IOException("Datos incompletos para conejo en línea " + numeroLinea);
                        conejosCargados.add(new Conejo(datosC[0], Double.parseDouble(datosC[1]), Integer.parseInt(datosC[2]), Boolean.parseBoolean(datosC[3]), Integer.parseInt(datosC[4]), Double.parseDouble(datosC[5])));
                        break;
                    case "LOBO":
                        String[] datosL = valor.split(",");
                        if (datosL.length < 7) throw new IOException("Datos incompletos para lobo en línea " + numeroLinea);
                        lobosCargados.add(new Lobo(datosL[0], Double.parseDouble(datosL[1]), Integer.parseInt(datosL[2]), Boolean.parseBoolean(datosL[3]), Integer.parseInt(datosL[4]), Double.parseDouble(datosL[5]), Integer.parseInt(datosL[6])));
                        break;
                    default:
                        throw new IOException("Clave desconocida '" + clave + "' en la línea " + numeroLinea);
                }
            }
        }

        // Reconstruir el objeto Ecosistema con los datos leídos
        Ecosistema ecosistema = new Ecosistema(clima, turnosTotales);
        ecosistema.setTurnoActual(turno);

        // Añadir las entidades recuperadas a las listas del ecosistema
        for (Planta p : plantasCargadas) ecosistema.getPlantas().add(p);
        for (Conejo c : conejosCargados) ecosistema.getConejos().add(c);
        for (Lobo l : lobosCargados) ecosistema.getLobos().add(l);

        return ecosistema;
    }
}
