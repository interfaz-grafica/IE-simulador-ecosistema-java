package interfazgraficaie;

public class Conejo extends Animal implements Reproducible {

    public Conejo(String nombre, double energia, int edad, boolean viva, int velocidad, double peso) {
        
        super(nombre, energia, edad, viva, velocidad, peso);
    }
    
    @Override
    public void comer(Ecosistema eco) {
        boolean conejoEncontroComida = false;
        
        // Búsqueda secuencial de recurso vegetal disponible
        for(Planta plantaActual : eco.getPlantas()) {
            // Si está viva la planta entonces, puede ser comida
            if(plantaActual.isViva()) {
                // Asignamos cómo obtener el valor nutritivo de la planta
                double valorNutritivoObtenido = plantaActual.serComida();
                
                // Incrementamos a la energía actual el valor nutritivo de la planta y la establecemos
                double nuevaEnergia = getEnergia() + valorNutritivoObtenido;

                // El formato del signo en la energía se añade manualmente si el aporte es positivo
                String signo = (valorNutritivoObtenido >= 0) ? "+" : "";
                System.out.println("Conejo '" + getNombre() + "' comio '" + plantaActual.getNombre() + "' (" + signo + valorNutritivoObtenido + " energia)");

                if (nuevaEnergia <= 0 && valorNutritivoObtenido < 0) {
                    System.out.println(getNombre() + " murio intoxicado.");
                    setViva(false); // Para evitar el print genérico de 'inanición'
                }
                
                setEnergia(nuevaEnergia);
                
                // Rompemos el bucle porque es una planta por turno.
                conejoEncontroComida = true;
                break;
            }
        }
        
        // Si el conejo no encontró comida en este turno pierde energía
        if(!conejoEncontroComida) {
            setEnergia(getEnergia() - 15);
            // Anexo del estado de alerta en consola para advertir peligro inminente
            System.out.print("Conejo '" + getNombre() + "' no encontro comida (-15.0 energia)");
            if (getEnergia() < 20 && getEnergia() > 0) {
                System.out.print(" [PELIGRO: energia=" + getEnergia() + "]");
            }
            System.out.println(); // Salto de línea final
        }
    }

    @Override
    public void actuar(Ecosistema eco) {
        
        //Acciones establecidas
        this.comer(eco);
        
        //this.intentarReproduccion(eco); --> Comentado para evitar reproducción extra en el mismo turno
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
            java.util.ArrayList<Conejo> posiblesParejas = new java.util.ArrayList<>();
            
            // Recolectamos todas las parejas viables
            for(Conejo conejoActual : eco.getConejos())
            {
                if (conejoActual.isViva() && conejoActual != this)
                {
                    posiblesParejas.add(conejoActual);
                }
            }
            
            if (!posiblesParejas.isEmpty())
            {
                // Elegimos una pareja al azar
                java.util.Random rand = new java.util.Random();
                Conejo pareja = posiblesParejas.get(rand.nextInt(posiblesParejas.size()));
                
                // Nace un nuevo conejo. Hereda el nombre del padre y su sufijo, inicia con 30 de energía, edad 0, vivo, misma velocidad y peso.
                Conejo cria = new Conejo(this.getNombre() + "-Cria", 30.0, 0, true, this.getVelocidad(), this.getPeso());
                
                // Agregamos la cría a la lista de conejos del ecosistema
                eco.getConejos().add(cria);
                eco.registrarNacimientoConejo(); // Suma al contador del reporte final
                
                // Restamos energía al padre por el esfuerzo de reproducirse
                this.setEnergia(this.getEnergia() - 20);
                
                System.out.println(this.getNombre() + " se ha reproducido con " + pareja.getNombre() + ". Nacio: " + cria.getNombre());
            }
        }
        
    }
    
    @Override
    public boolean puedeReproducirse() {
        
        return isViva() && getEnergia() > 60;
    } 
}
