package ecosim;

abstract class Animal extends Entidad implements Mortal{
    //ATRIBUTOS
    //Heredados: String nombre, double energia, int edad, boolean viva
    private int velocidad;
    private double peso;
    
    //CONSTRUCTOR
    public Animal(String nombre, double energia, int edad, boolean viva, int velocidad, double peso){
        super(nombre, energia, edad, viva);
        this.velocidad = velocidad;
        this.peso = peso;
    }
    
    //GETS
    public int getVelocidad(){
        return this.velocidad;
    }
    
    public double getPeso(){
        return this.peso;   
    }
    
    //SETS
    public void setVelocidad(int velocidad){
        this.velocidad = velocidad;
    }
    
    public void setPeso(double peso){
        this.peso = peso;
    }
    
    //METODOS
    abstract void comer(Ecosistema eco);

    public void moverse(){
        System.out.println(this.getNombre() + " se movio");
    }
}