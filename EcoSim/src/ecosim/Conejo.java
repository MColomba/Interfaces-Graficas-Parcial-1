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
    
    public void actuar(Ecosistema eco){
        comer(eco);
        reproduccion(eco);
    }
    
    @Override
    public void mostrarEstado(){
        System.out.println(this.getNombre());
        System.out.println(this.getEnergia());
        if (this.getEnergia() < 20){
            System.out.println("Peligro!!");
        }
    }
    public void reproduccion(Ecosistema eco){
        // si energía > 60 y hay al menos otro conejo vivo, puede generar un nuevo Conejo
    }
}