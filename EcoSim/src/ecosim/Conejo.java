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
    public void comer(Ecosistema eco){
        //ArrayList<Planta> plantas = eco
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