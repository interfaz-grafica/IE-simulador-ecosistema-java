package modelo;

public abstract class Animal extends Entidad implements Mortal {

    private int velocidad;
    private double peso;
    
    public Animal(String nombre, double energia, int edad, boolean viva, int velocidad, double peso)
    {
        super(nombre, energia, edad, viva);
        
        // Se usan los setters para que la validación también se aplique al crear o cargar
        setVelocidad(velocidad);
        setPeso(peso);
    }
    
    public abstract void comer(Ecosistema eco);
  
    // MVC: Retorna el texto de desplazamiento en lugar de imprimirlo por consola
    public String moverse() {
        return getNombre() + " se desplazo a " + getVelocidad() + " km/h.";
    }
    
    @Override
    public boolean estaVivo() {
        return isViva();
    }

    @Override
    public void morir() {
        setViva(false);
        // Vaciar reservas de energía al morir
        setEnergia(0);
    }
    
    //-----------------------------------------------------------------------//
    //Getters y setters
    
    public int getVelocidad() {
      return velocidad;
    }
    // La velocidad no puede ser negativa
    public void setVelocidad(int velocidad) {
        if (velocidad < 0) {
            this.velocidad = 0;
        } else {
            this.velocidad = velocidad;
        }
    }

    public double getPeso() {
        return peso;
    }
    // El peso no puede ser negativo
    public void setPeso(double peso) {
        if (peso < 0) {
            this.peso = 0;
        } else {
            this.peso = peso;
        }
    }
}