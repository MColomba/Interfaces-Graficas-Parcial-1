package ecosim;

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

            System.out.println(this.getNombre()
                    + " comio una planta y gano "
                    + energiaGanada + " de energia.");
        } else {
            this.setEnergia(this.getEnergia() - 15);

            System.out.println(this.getNombre()
                    + " no encontro plantas y perdio 15 de energia.");
        }
    }
    
    @Override
    public void actuar(Ecosistema eco){
        comer(eco);
        reproducirse(eco);
    }
    
    @Override
    public void mostrarEstado(){
        System.out.println(this.getNombre());
        System.out.println(this.getEnergia());
        if (this.getEnergia() < 20){
            System.out.println("Peligro!!");
        }
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
            "Conejo nuevo",
            50,
            0,
            true,
            this.getVelocidad(),
            this.getPeso()
    );

    eco.getConejos().add(nuevoConejo);

    System.out.println(this.getNombre()
            + " se reprodujo y nacio un nuevo conejo.");
        }
    @Override
    public boolean puedeReproducirse() {
        return this.getEnergia() > 60 && this.getViva();
    }
}