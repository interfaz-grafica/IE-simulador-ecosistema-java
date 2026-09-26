package interfazgraficaie;

public interface Mortal {
    
    boolean estaVivo();
    void morir();
    
    default void verificarMuerte(double energia) {
        if (energia <= 0) {
            morir();
            System.out.println("Una entidad ha muerto por falta de energía.");
        } 
    }
}
