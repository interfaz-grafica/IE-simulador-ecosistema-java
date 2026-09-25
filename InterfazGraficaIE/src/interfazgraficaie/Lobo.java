package interfazgraficaie;

 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author GZ TIENDA
 */
public class Lobo extends Animal implements Peligroso {

    private int exitosCaza;  
       
    public Lobo(String nombre, double energia, int edad, boolean viva, int velocidad, double peso, int exitosCaza) {
        
        super(nombre, energia, edad, viva, velocidad, peso);
    }
    
    public int getExitosCaza() {
        return exitosCaza;
    }

    public void setExitosCaza(int exitosCaza) {
        this.exitosCaza = exitosCaza;
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
    
}
