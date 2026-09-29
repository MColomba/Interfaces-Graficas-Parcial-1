package ecosim;

public interface Reproducible {
    public boolean puedeReproducirse();
    public void reproducirse(Ecosistema eco); //falta clase ecosistema
    
    //falta clase ecosistema
    default void intentarReproduccion(Ecosistema eco){
        if (puedeReproducirse()){
            reproducirse(eco);
        }
    }
}
