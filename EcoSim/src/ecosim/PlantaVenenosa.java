package ecosim;

public class PlantaVenenosa extends Planta implements Peligroso {
    public PlantaVenenosa(String nombre, double energia, int edad, boolean viva, int tamanio) {
        super(nombre, energia, edad, viva, tamanio);
    }
    
    @Override
    public int serComida() {
        this.setEnergia(0);
        this.setViva(false);
        return -30; // Devuelve -30 para restar energía al conejo al comerla
    }
    
    @Override
    public int getNivelPeligro() {
        return 5;
    }
}
