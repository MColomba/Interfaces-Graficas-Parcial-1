package ecosim;

abstract class Entidad {
    //ATRIBUTOS
    private String nombre;
    private double energia;
    private int edad;
    private boolean viva;
    
    //CONSTRUCTOR
    public Entidad(String nombre, double energia, int edad, boolean viva){
        this.nombre = nombre;
        this.energia = energia;
        this.edad = edad;
        this.viva = viva;
    }
    
    //GETS
    public String getNombre(){
        return this.nombre;
    }
    
    public double getEnergia(){
        return this.energia;
    }
    
    public int getEdad(){
        return this.edad;
    }
    
    public boolean getViva(){
        return this.viva;
    }
    
    //SETS
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    
    public void setEnergia(double energia){
        this.energia = energia;
    }
    
    public void setEdad(int edad){
        this.edad = edad;
    }
    
    public void setViva(boolean viva){
        this.viva = viva;
    }
    
    //METODOS
    abstract void actuar(Ecosistema eco);
    
    abstract void mostrarEstado();
    
    public void envejecer(){
        this.edad += 1;
        this.energia -= 1;
    }
}
