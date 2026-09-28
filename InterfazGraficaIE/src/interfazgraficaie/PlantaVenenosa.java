package interfazgraficaie;

public class PlantaVenenosa extends Planta implements Peligroso {
    
    public PlantaVenenosa(String nombre, double energia, int edad, boolean viva, int tamanio) {
        super(nombre, energia, edad, viva, tamanio);
    }
    
    // Sobreescritura del método serComida
    // Aplica una penalización energética de -30.0 al consumidor
    @Override
    public double serComida() {
        if (!isViva()) return 0; 
       
        setViva(false);
        setEnergia(0);
       
        return -30.0;
    }
    
    // Implementación de la interfaz Peligroso
    @Override
    public int getNivelPeligro() {
        return 5;
    }
    
    @Override
    public void mostrarEstado() {
        System.out.println("Planta: " + getNombre() + " Tamaño: " + getTamanio() + " Energía: " + getEnergia());
    }
}
