/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author GZ TIENDA
 */
public abstract class Entidad {

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getEnergia() {
        return energia;
    }

    public void setEnergia(double energia) {
        this.energia = energia;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public boolean isViva() {
        return viva;
    }

    public void setViva(boolean viva) {
        this.viva = viva;
    }

    private String nombre;
    private double energia;
    private int edad;
    private boolean viva;
    
    public Entidad(String nombre, double energia, int edad, boolean viva)
    {
        this.nombre = nombre;
        this.energia = energia;
        this.edad = edad;
        this.viva = viva; 
    }
    
    public abstract void actuar(Ecosistema eco); 
    public abstract void mostrarEstado();
    public void envejecer()
    {
        
    }
}
