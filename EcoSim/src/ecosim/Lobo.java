package ecosim;

public class Lobo extends Animal{
    //ATRIBUTOS
    //Heredados: String nombre, double energia, int edad, boolean viva, int velocidad, double peso
    public int exitosCaza;
    
    //CONSTRUCTOR
    public Lobo(String nombre, double energia, int edad, boolean viva, int velocidad, double peso, int exitosCaza){
        super(nombre, energia, edad, viva, velocidad, peso);
        this.exitosCaza = exitosCaza;
    }
    
    //GETS
    public int getExitosCaza(){
        return this.exitosCaza;
    }
    
    //SETS
    public void setExitosCaza(int exitosCaza){
        this.exitosCaza = exitosCaza;
    }
    
    //METODOS
    public void actuar(){
        //intenta cazar un conejo vivo
    }
    public void comer(){
        //selecciona un conejo aleatorio; si la caza es exitosa (probabilidad según energía), lo mata y gana energía
    }
    @Override
    public void mostrarEstado(){
        System.out.println(this.getNombre());
        System.out.println(this.getEnergia());
        System.out.println(this.exitosCaza);
    }
}
