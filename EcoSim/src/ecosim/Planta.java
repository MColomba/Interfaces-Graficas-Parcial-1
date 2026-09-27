package ecosim;

public class Planta extends Entidad implements Reproducible{
    //ATRIBUTOS
    //Heredados: String nombre, double energia, int edad, boolean viva
    private int tamanio;
    
    //CONSTRUCTOR
    
    //GETS
    
    //SETS
    
    //METODOS
    public void actuar(){
        //if(energia and clima suficiente)
        reproducirse(tamanio);
    }
    
    @Override
    public void mostrarEstado(){
        System.out.println(this.getNombre());
        System.out.println(this.tamanio);
        System.out.println(this.getEnergia());
    }
    
    public int serComida(){
        this.setEnergia(0);
        return tamanio * 10;
    }
}
