package interfazgraficaie;

public abstract class Animal extends Entidad implements Mortal {

    private int velocidad;
    private double peso;
    
    public Animal(String nombre, double energia, int edad, boolean viva, int velocidad, double peso)
    {
        super(nombre, energia, edad, viva);
        
        this.velocidad = velocidad;
        this.peso = peso;   
    }
    
    public abstract void comer(Ecosistema eco);
  
    public void moverse() {
        
    }
    
    @Override
    public boolean estaVivo() {
        return false;
    }

    @Override
    public void morir() {
        
    }
    
    //-----------------------------------------------------------------------//
    //Getters y setters
    
    public int getVelocidad() {
      return velocidad;
    }
    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    public double getPeso() {
        return peso;
    }
    public void setPeso(double peso) {
        this.peso = peso;
    }  

    
}
