package interfazgraficaie;

public interface Mortal {
    
    boolean estaVivo();
    void morir();
    
    default void verificarMuerte() {
        
    }
}
