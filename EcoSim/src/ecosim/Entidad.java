package ecosim;

public abstract class Entidad {
    //ATRIBUTOS
    private String nombre;
    private double energia;
    private int edad;
    private boolean viva;
    
    //CONSTRUCTOR
    public Entidad(String nombre, double energia, int edad, boolean viva){
        this.nombre = nombre;
        //validaciones en el constructor
        if (energia < 0){
            this.energia = 0;
        }
        else{
            this.energia = energia;
        }
        if (edad < 0){
            this.edad = 0;
        }
        else{
            this.edad = edad;
        }
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
        if (energia < 0){
            this.energia = 0;
        }
        else{
            this.energia = energia;
        }
    }
    
    public void setEdad(int edad){
        if (edad < 0){
            this.edad = 0;
        }
        else{
            this.edad = edad;
        }
    }
    
    public void setViva(boolean viva){
        this.viva = viva;
    }
    
    //METODOS
    public abstract void actuar(Ecosistema eco);
    public abstract void mostrarEstado();
    
    public void envejecer(){
        this.edad += 1;
        setEnergia(this.energia - 2.0); //asi para que pase por la validacion
    }
}
