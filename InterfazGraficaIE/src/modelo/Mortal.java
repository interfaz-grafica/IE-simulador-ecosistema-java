package modelo;

public interface Mortal {
    
    boolean estaVivo();
    void morir();
    
    // MVC: Retorna el mensaje de muerte en lugar de imprimirlo por consola
    default String verificarMuerte(double energia) {
        if (energia <= 0) {
            morir();
            return "Una entidad ha muerto por falta de energia.";
        }
        return "";
    }
}
