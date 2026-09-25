package interfazgraficaie;

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
