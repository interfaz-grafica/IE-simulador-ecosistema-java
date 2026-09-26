package interfazgraficaie;

public class Conejo extends Animal implements Reproducible {

    public Conejo(String nombre, double energia, int edad, boolean viva, int velocidad, double peso) {
        
        super(nombre, energia, edad, viva, velocidad, peso);
    }
    
    @Override
    public void comer(Ecosistema eco) {
        boolean conejoEncontroComida = false;
        
        //Recorremos la planta que está actualmente de la clase Planta
        for(Planta plantaActual : eco.getPlantas())
            {
            // Si está viva la planta entonces, puede ser comida
            if(plantaActual.isViva())
                {
                    // Asignamos cómo obtener el valor nutritivo de la planta
                    double valorNutritivoObtenido = plantaActual.serComida();
                    
                    // Incrementamos a la energía actual el valor nutritivo de la planta y la establecemos usando el método set
                    double nuevaEnergia = getEnergia() + valorNutritivoObtenido;
                    setEnergia(nuevaEnergia);

                    System.out.println("Conejo '" + getNombre() + "' comió la planta'" + plantaActual.getNombre() + "' (" + valorNutritivoObtenido + " energia)");

                    // El conejo encontró comida, entonces asignamos true y rompemos el bucle porque es por turnos.
                    conejoEncontroComida = true;
                    break;
                }
            }
        
        // Si el conejo no encontró comida, entonces su energía se desgasta un -15
        if(!conejoEncontroComida)
                {
                    setEnergia(getEnergia() - 15);
                    System.out.println("Conejo " + getNombre() + " no encontró comida (-15 energía)");
                }
    }

    @Override
    public void actuar(Ecosistema eco) {
        
        this.comer(eco);
        
        this.intentarReproduccion(eco);
    }
    
    @Override
    public void mostrarEstado() {
        
        // Mostramos el nombre del conejo y su estado según su energía
        System.out.print("Conejo '" + getNombre() + "' (Energia: " + getEnergia() + ")");
        if (getEnergia() < 20) {
            System.out.print(" [EN PELIGRO]");
        }
        System.out.println();
        
    }
    
    @Override
    public void reproducirse(Ecosistema eco) {
 
        // Si la energía del conejo es mayor a 60 entonces, puede reproducirse
        if(puedeReproducirse())
        {
            //Recorremos al conejo que está actualmente
            for(Conejo conejoActual : eco.getConejos())
            {
                // Nos aseguremos que el conejo con el que se reproduzca esté vivo y que no se intente reproducir consigo mismo
                if (conejoActual.isViva() && conejoActual != this)
                {
                    // Nace un nuevo conejo. Hereda el nombre del padre + sufijo, inicia con 30 de energía, edad 0, vivo, misma velocidad y peso.
                    Conejo cria = new Conejo(this.getNombre() + "-Cria", 30.0, 0, true, this.getVelocidad(), this.getPeso());
                    
                    // Agregamos la cría a la lista de conejos del ecosistema
                    eco.getConejos().add(cria);
                    
                    // Restamos energía al padre por el esfuerzo de reproducirse
                    this.setEnergia(this.getEnergia() - 20);
                    
                    System.out.println("¡Milagro de la vida! " + this.getNombre() + " se ha reproducido con " + conejoActual.getNombre() + ". Nació: " + cria.getNombre());
                    
                    // Rompemos el bucle para que solo tenga una cría por turno
                    break;
                }
            }
        }
        
    }
    
    @Override
    public boolean puedeReproducirse() {
        
        // Mi forma sin optimizar código
        /*if(getEnergia()>60)
        {
            return true;
        }
        else return false;*/
        
        // Forma optimizada
        return getEnergia()>60;
    } 
}
