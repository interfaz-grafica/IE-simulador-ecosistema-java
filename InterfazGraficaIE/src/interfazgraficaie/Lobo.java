package interfazgraficaie;

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
        
        //Recolección de presas (conejos) vivas
        ArrayList<Conejo> conejosVivos = new ArrayList<>();
        
        for (Conejo conejoActual : eco.getConejos())
            {
                if(conejoActual.isViva())
                {
                    conejosVivos.add(conejoActual);
                }   
            }
        if (conejosVivos.isEmpty())
            {
                setEnergia((getEnergia()-15));
                System.out.println("Lobo '" + getNombre() + "' no encontró conejos vivos (-15 energia)");
                return; 
            }
        Random generadorAleatorio = new Random();
        
        int conejoAleatorio = generadorAleatorio.nextInt(conejosVivos.size());
        Conejo presa = conejosVivos.get(conejoAleatorio);
        
        double probabilidadExitoCaza = getEnergia() / 100;
        
        if(eco.getClimaActual() == Clima.INVIERNO)
        {
            probabilidadExitoCaza = probabilidadExitoCaza + 0.20;
        }
        
        double intentoCaza = generadorAleatorio.nextDouble();
        
        if(intentoCaza <= probabilidadExitoCaza)
        {
            presa.morir();
            
            setEnergia(getEnergia()+ 40);
            this.exitosCaza++;
            System.out.println("Lobo '" + getNombre() + "' CAZÓ EXITOSAMENTE a Conejo '" + presa.getNombre() + "' (+40 energia) [Exitos totales: " + this.exitosCaza + "]");
        }
        else
        {
            // El lobo falló el ataque
            setEnergia(getEnergia() - 10); // Pierde energía por el esfuerzo en vano
            System.out.println("Lobo '" + getNombre() + "' intentó cazar a Conejo '" + presa.getNombre() + "' y FALLÓ (-10 energia)");
        }
    }

    @Override
    public void actuar(Ecosistema eco) {
        
        this.comer(eco);
        
    }
    
    @Override
    public void mostrarEstado() {
        
        // Mostramos el nombre, estado y cantidad de cazas exitosas de un lobo
        System.out.print("Lobo '" + getNombre() + "' (Energia: " + getEnergia() + ") [Cacerias exitosas: " + this.exitosCaza + "]");
        System.out.println();
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
