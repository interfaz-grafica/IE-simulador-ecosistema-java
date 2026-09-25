/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author GZ TIENDA
 */
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
