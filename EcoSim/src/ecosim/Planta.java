package ecosim;

public class Planta extends Entidad implements Reproducible{
    //ATRIBUTOS
    //Heredados: String nombre, double energia, int edad, boolean viva
    private int tamanio;
    
    //CONSTRUCTOR
    public Planta(String nombre, double energia, int edad, boolean viva, int tamanio) {
        super(nombre, energia, edad, viva);
        setTamanio(tamanio);
    }
    //GETS
    public int getTamanio() {
        return this.tamanio;
    }
    //SETS
    public void setTamanio(int tamanio) {
        if (tamanio < 1) {
            this.tamanio = 1;
        } else if (tamanio > 5) {
            this.tamanio = 5;
        } else {
            this.tamanio = tamanio;
        }
    }
    //METODOS
    @Override
    public void actuar(Ecosistema eco){
        //if(energia and clima suficiente)
        reproducirse(eco);
    }
    
    @Override
    public void mostrarEstado(){
        System.out.println(this.getNombre());
        System.out.println(this.tamanio);
        System.out.println(this.getEnergia());
    }
    
    public int serComida(){
        this.setEnergia(0);
        this.setViva(false);
        return this.tamanio * 10;
    }
    
    @Override
    public boolean puedeReproducirse() {
        return this.getViva() && this.getEnergia() >= 20.0;
    }

    @Override
    public void reproducirse(Ecosistema eco) {
        double factorClima = eco.getMultiplicadorReproduccionPlantas();

        // Si es Invierno (0.0), no se reproducen
        if (factorClima > 0.0) {
            this.setEnergia(this.getEnergia() - (15.0 / factorClima));

            String nombreHijo = eco.generarNombre("planta");
            Planta nuevaPlanta = new Planta(nombreHijo, 25.0, 0, true, 1);
            eco.agregarPlanta(nuevaPlanta);

            eco.registrarEvento("Planta '" + this.getNombre() + "' se reprodujo -> " + nombreHijo);
        }
    }
}
