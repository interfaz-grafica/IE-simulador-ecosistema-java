package interfazgraficaie;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author GZ TIENDA
 */
public abstract class Animal extends Entidad implements Mortal {

    private int velocidad;
    private double peso;
    
    public Animal(String nombre, double energia, int edad, boolean viva, int velocidad, double peso)
    {
        super(nombre, energia, edad, viva);
        
        this.velocidad = velocidad;
        this.peso = peso;   
    }
    
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
    
    public abstract void comer(Ecosistema eco);
  
    public void moverse() {
        
    }
    
    @Override
    public void estaVivo(){
        
    }

    @Override
    public void morir(){
        
    }

    @Override //Habría que hacer override o no? Con default es opcional.
    public void verificarMuerte(){
        
    }
    
}
