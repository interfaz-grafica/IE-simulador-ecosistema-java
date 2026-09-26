package interfazgraficaie;

public class PlantaVenenosa extends Planta implements Peligroso {
    
    public PlantaVenenosa(String nombre, double energia, int edad, boolean viva, int tamanio) {
        super(nombre, energia, edad, viva, tamanio);
    }
    
    //sobrescribe el comportamiento al ser comido
    //en lugar de sumar energia, resta -30.0 como castigo
    @Override
    public double serComida() {
        if (!isViva()) return 0; 
       
        setViva(false);
        setEnergia(0);
       
        return -30.0;
    }
    
    //metodo de la inetrfaz Peligroso
    @Override
    public int getNivelPeligro() {
        return 5;
    }
    
    @Override
    public void mostrarEstado() {
        System.out.println("Planta: " + getNombre() + " Tamaño: " + getTamanio() + " Energía: " + getEnergia());
    }
}
