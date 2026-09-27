package ecosim;

public interface Reproducible {
    public boolean puedeReproducirse();
    public void reproducirse(int eco); //falta clase ecosistema
    
    //falta clase ecosistema
    default void intentarReproduccion(int eco){
        if (puedeReproducirse()){
            reproducirse(eco);
        }
    }
}
