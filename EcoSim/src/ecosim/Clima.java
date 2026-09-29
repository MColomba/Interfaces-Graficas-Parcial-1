/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package ecosim;

/**
 *
 * @author Maxi
 */
public enum Clima {
    SOLEADO(1.5),  // Plantas: reproducción x1.5
    LLUVIOSO(2.0), // Plantas: reproducción x2.0
    SEQUIA(0.5),   // Plantas: reproducción x0.5
    INVIERNO(0.0); // Plantas: no se reproducen (x0.0)

    private final double multiplicadorPlanta;

    Clima(double multiplicadorPlanta) {
        this.multiplicadorPlanta = multiplicadorPlanta;
    }

    public double getMultiplicadorPlanta() {
        return multiplicadorPlanta;
    }
}
