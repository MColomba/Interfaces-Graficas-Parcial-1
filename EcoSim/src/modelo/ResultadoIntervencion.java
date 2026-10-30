/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 * @author bruno
 * Resultado de una intervención del jugador (cambiar clima / agregar entidad).
 * modelo le devuelve al controlador en vez de imprimir en consola:
 *   - exito:   true si la acción se pudo hacer, false si no
 *   - mensaje: texto para que la vista le muestre a la persona
 */

public class ResultadoIntervencion {
    private final boolean exito;
    private final String mensaje;
 
    public ResultadoIntervencion(boolean exito, String mensaje) {
        this.exito = exito;
        this.mensaje = mensaje;
    }
 
    public boolean isExito() { return exito; }
    public String getMensaje() { return mensaje; }
}
