package interfazgraficaie;

public class Conejo extends Animal implements Reproducible {

    public Conejo(String nombre, double energia, int edad, boolean viva, int velocidad, double peso) {
        
        super(nombre, energia, edad, viva, velocidad, peso);
    }
    
    @Override
    public void comer(Ecosistema eco) {
    }

    @Override
    public void actuar(Ecosistema eco) {
    }
    
    @Override
    public void mostrarEstado() {
    }
    
    @Override
    public void reproducirse(Ecosistema eco) {
 
    }

    @Override
    public void intentarReproduccion(Ecosistema eco) {
        
    }
    
    @Override
    public void puedeReproducirse() {
       
    }   
}
