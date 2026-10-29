package modelo;

import java.util.ArrayList;
import java.util.Random;

public class Lobo extends Animal implements Peligroso {

    private int exitosCaza;  
        
    public Lobo(String nombre, double energia, int edad, boolean viva, int velocidad, double peso, int exitosCaza) {
        
        super(nombre, energia, edad, viva, velocidad, peso);
        this.exitosCaza = exitosCaza;
    }
    
    @Override
    public void comer(Ecosistema eco) {
        
        ArrayList<Conejo> conejosVivos = obtenerConejosVivos(eco);
        
        // Si no puede comer, pierde energía
        if (conejosVivos.isEmpty()) {
            setEnergia(getEnergia() - 15);
            // MVC: Se guarda en la lista de eventos del ecosistema
            eco.registrarEvento("Lobo '" + getNombre() + "' no encontro conejos vivos (-15 energia)");
            return;
        }

        // Se elige un conejo
        Random generadorAleatorio = new Random();
        Conejo presa = conejosVivos.get(generadorAleatorio.nextInt(conejosVivos.size()));
        
        // Se come el conejo
        ejecutarAtaque(eco, presa, generadorAleatorio);
    }
    
    // Métodos auxiliares de caza
    private ArrayList<Conejo> obtenerConejosVivos(Ecosistema eco) {
        ArrayList<Conejo> vivos = new ArrayList<>();
        for (Conejo conejoActual : eco.getConejos()) {
            if (conejoActual.isViva()) {
                vivos.add(conejoActual);
            }
        }
        return vivos;
    }

    private void ejecutarAtaque(Ecosistema eco, Conejo presa, Random generador) {
        double probabilidadExitoCaza = getEnergia() / 100.0;
        
        // Bonificación del 20% en probabilidad de caza durante clima invernal
        if (eco.getClimaActual() == Clima.INVIERNO) {
            probabilidadExitoCaza = probabilidadExitoCaza + 0.20; 
        }
        
        double intentoCaza = generador.nextDouble();
        
        // Analizamos si el lobo logra cazar al conejo o falla
        if (intentoCaza <= probabilidadExitoCaza) {
            presa.morir();
            setEnergia(getEnergia() + 40);
            this.exitosCaza++;
            // MVC: Registro del evento de caza exitosa
            eco.registrarEvento("Lobo '" + getNombre() + "' cazo a Conejo '" + presa.getNombre() + "' (+40.0 energia) [cacerias: " + this.exitosCaza + "]");
        } else {
            setEnergia(getEnergia() - 10);
            // MVC: Registro del evento de caza fallida
            eco.registrarEvento("Lobo '" + getNombre() + "' fallo la caza (-10.0 energia)");
        }
    }
    
    @Override
    public void actuar(Ecosistema eco) {
        
        this.comer(eco);
        
    }
    
    @Override
    public String mostrarEstado() {
        
        // Mostramos el nombre, estado y cantidad de cazas exitosas de un lobo
        return "Lobo '" + getNombre() + "' (Energia: " + getEnergia() + ") [Cacerias exitosas: " + this.exitosCaza + "]";
    }
    
    @Override
    public int getNivelPeligro() {
        
        // Mientras más energía y cazas exitosas tenga, más peligroso es
        return (int)(getEnergia()) + (this.exitosCaza * 10);
    }
    
    //-----------------------------------------------------------------------//
    //Getters y setters
    
    public int getExitosCaza() {
        return exitosCaza;
    }

    public void setExitosCaza(int exitosCaza) {
        this.exitosCaza = exitosCaza;
    }
    
}