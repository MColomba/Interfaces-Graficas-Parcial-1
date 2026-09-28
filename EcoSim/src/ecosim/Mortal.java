package ecosim;

public interface Mortal {
    //METODOS
    public boolean estaVivo();
    public void morir();
    
    default void verificarMuerte(){
        if (!estaVivo()){
            morir();
        }
    }
}
