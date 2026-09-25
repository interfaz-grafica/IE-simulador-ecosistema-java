package interfazgraficaie;

public interface Mortal {
    
    void estaVivo();
    void morir();
    
    default void verificarMuerte() {
        
    }
}
