package modelo;

import java.util.ArrayList;

public class Conejo extends Animal implements Reproducible{
    //ATRIBUTOS
    //Heredados: String nombre, double energia, int edad, boolean viva, int velocidad, double peso
    
    //CONSTRUCTOR
    public Conejo(String nombre, double energia, int edad, boolean viva, int velocidad, double peso){
        super(nombre, energia, edad, viva, velocidad, peso);
    }    
    
    //METODOS
    @Override
    public void comer(Ecosistema eco) {

        Planta plantaEncontrada = null;

        for (Planta planta : eco.getPlantas()) {
            if (planta.getViva()) {
                plantaEncontrada = planta;
                break;
            }
        }

        if (plantaEncontrada != null) {
            int energiaGanada = plantaEncontrada.serComida();

            this.setEnergia(this.getEnergia() + energiaGanada);

            eco.registrarEvento(this.getNombre()
                    + " comio una planta y gano "
                    + energiaGanada + " de energia.");
        } else {
            this.setEnergia(this.getEnergia() - 15);

            eco.registrarEvento(this.getNombre()
                    + " no encontro plantas y perdio 15 de energia.");
        }
    }
    
    @Override
    public void actuar(Ecosistema eco){
        comer(eco);
        reproducirse(eco);
    }
    
    @Override
    public String describirEstado(){
        String estado = this.getNombre() + " - energia: " + this.getEnergia();
        if (this.getEnergia() < 20){
            estado += " (peligro)";
        }
        return estado;
    }
    @Override
    public void reproducirse(Ecosistema eco) {

        if (!puedeReproducirse()) {
            return;
        }

        boolean hayOtroConejoVivo = false;

        for (Conejo conejo : eco.getConejos()) {
            if (conejo != this && conejo.getViva()) {
                hayOtroConejoVivo = true;
                break;
            }
        }

        if (!hayOtroConejoVivo) {
            return;
        }
        Conejo nuevoConejo = new Conejo(
            eco.generarNombre("conejo"),
            50,
            0,
            true,
            this.getVelocidad(),
            this.getPeso()
    );

    eco.agregarConejo(nuevoConejo);   // cuenta como nacimiento y entra al historial
    eco.registrarEvento(this.getNombre() + " se reprodujo -> " + nuevoConejo.getNombre());
        }
    @Override
    public boolean puedeReproducirse() {
        return this.getEnergia() > 60 && this.getViva();
    }
}