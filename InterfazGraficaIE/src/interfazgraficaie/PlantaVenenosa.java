package interfazgraficaie;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author GZ TIENDA
 */
public class PlantaVenenosa extends Planta implements Peligroso {
    
    public PlantaVenenosa(String nombre, double energia, int edad, boolean viva, int tamanio) {
        super(nombre, energia, edad, viva, tamanio);
    }
    
    @Override
    public double serComida() {
        return 0;
    }
    
    @Override
    public int getNivelPeligro() {
        return 0;
    }
}
