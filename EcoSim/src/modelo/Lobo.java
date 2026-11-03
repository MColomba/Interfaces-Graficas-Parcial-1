package modelo;

public class Lobo extends Animal implements Peligroso {
    //ATRIBUTOS
    //Heredados: String nombre, double energia, int edad, boolean viva, int velocidad, double peso
    private int exitosCaza;
    
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
    @Override
    public int getNivelPeligro() {
        return 10;
    }
    
    //METODOS
   @Override
    public void actuar(Ecosistema eco) {
        comer(eco);
    }
    @Override
    public void comer(Ecosistema eco) {
        java.util.ArrayList<Conejo> conejosVivos = new java.util.ArrayList<>();

        for (Conejo conejo : eco.getConejos()) {
            if (conejo.estaVivo()) {
                conejosVivos.add(conejo);
            }
        }

        if (conejosVivos.isEmpty()) {
            eco.registrarEvento(this.getNombre()
                    + " no encontro conejos vivos para cazar.");
            return;
        }
        int posicion = (int) (Math.random() * conejosVivos.size());
        Conejo presa = conejosVivos.get(posicion);

        double probabilidadCaza = this.getEnergia() / 100.0;

        if (probabilidadCaza > 0.9) {
            probabilidadCaza = 0.9;
        }

        if (probabilidadCaza < 0.1) {
            probabilidadCaza = 0.1;
        }

        double intento = Math.random();
        if (intento < probabilidadCaza) {
            presa.morir();

            this.setEnergia(this.getEnergia() + 30);
            this.exitosCaza++;

            eco.registrarEvento(this.getNombre()
                    + " cazo a " + presa.getNombre()
                    + " y gano 30 de energia.");
        } else {
            eco.registrarEvento(this.getNombre()
                    + " intento cazar a " + presa.getNombre()
                    + " pero fallo.");
        }
    }
    @Override
    public String describirEstado() {
        return "Lobo: " + this.getNombre() + " - energia: " + this.getEnergia()
                + " - cazas exitosas: " + this.getExitosCaza();
    }
}
