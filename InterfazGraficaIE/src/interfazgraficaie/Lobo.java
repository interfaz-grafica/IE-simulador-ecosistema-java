package interfazgraficaie;

public class Lobo extends Animal implements Peligroso {

    private int exitosCaza;  
       
    public Lobo(String nombre, double energia, int edad, boolean viva, int velocidad, double peso, int exitosCaza) {
        
        super(nombre, energia, edad, viva, velocidad, peso);
        this.exitosCaza = exitosCaza;
    }
    
    @Override
    public void comer(Ecosistema eco) {
    }

    @Override
    public void actuar(Ecosistema eco) {
        
        this.comer(eco);
        
    }
    
    @Override
    public void mostrarEstado() {
        
        // Mostramos el nombre, estado y cantidad de cazas exitosas de un lobo
        System.out.print("Lobo '" + getNombre() + "' (Energia: " + getEnergia() + ") [Cacerias exitosas: " + this.exitosCaza + "]");
        System.out.println();
    }
    
    @Override
    public int getNivelPeligro() {
        
        // Mientras más energía y cazas exitosas tenga, más peligroso es
        return (int)(getEnergia()) + (this.exitosCaza * 10);
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
