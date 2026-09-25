/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author GZ TIENDA
 */
public class Planta extends Entidad implements Reproducible {

    private int tamanio;
    
    
    public int getTamanio() {
        return tamanio;
    }

    public void setTamanio(int tamanio) {
        this.tamanio = tamanio;
    }

    public Planta(String nombre, double energia, int edad, boolean viva, int tamanio) {
        
        super(nombre, energia, edad, viva);
        this.tamanio = tamanio;
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
}

    
    
