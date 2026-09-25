package interfazgraficaie;

public class Lobo extends Animal implements Peligroso {

    private int exitosCaza;  
       
    public Lobo(String nombre, double energia, int edad, boolean viva, int velocidad, double peso, int exitosCaza) {
        
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
    public int getNivelPeligro() {
        return 0;
    }
    
    //-----------------------------------------------------------------------//
    //Getters y setters
    
    public int getExitosCaza() {
        return exitosCaza;
    }

    public void setExitosCaza(int exitosCaza) {
        this.exitosCaza = exitosCaza;
    }
    
}
