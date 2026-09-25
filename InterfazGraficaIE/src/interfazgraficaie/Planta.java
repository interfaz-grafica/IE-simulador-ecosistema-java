package interfazgraficaie;

public class Planta extends Entidad implements Reproducible {

    private int tamanio;

    public Planta(String nombre, double energia, int edad, boolean viva, int tamanio) {
        
        super(nombre, energia, edad, viva);
        this.tamanio = tamanio;
    }
    
    public double serComida() {
        return 0; //A modificar
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

    @Override
    public void actuar(Ecosistema eco) {
 
    }

    @Override
    public void mostrarEstado() {
        
    }
    
    //-----------------------------------------------------------------------//
    //Getters y setters
    
    public int getTamanio() {
        return tamanio;
    }

    public void setTamanio(int tamanio) {
        this.tamanio = tamanio;
    }
}

    
    
