package interfazgraficaie;

public class Planta extends Entidad implements Reproducible {

    private int tamanio;

    public Planta(String nombre, double energia, int edad, boolean viva, int tamanio) {
        
        super(nombre, energia, edad, viva);
        // se usa el setter para garantizar que nazca con un tamaño de 1 a 5
        setTamanio(tamanio);
    }
    
    public double serComida() {
      if (!isViva()) return 0; 
      
      double valorNutritivo = this.tamanio * 10.0; 
        
        setViva(false);
        setEnergia(0); // Reduce energía al mínimo
        
        return valorNutritivo;
    }
    
    
@Override
    public boolean puedeReproducirse() {
        // Devuelve verdadero si la planta está viva y tiene la energía mínima requerida
        return isViva() && getEnergia() >= 20.0;
    }

    @Override
    public void reproducirse(Ecosistema eco) {
        
        // 1. Regla de Invierno: las plantas no se reproducen con este clima.
        if (eco.getClimaActual() == Clima.INVIERNO) {
            return; // Termina la ejecución del método aquí mismo. No hace nada.
        } 

        // 2. Establecer una base de nacimientos (1 brote por defecto)
        int brotes = 1; 
        
        // 3. Evaluar el impacto del clima actual en la cantidad de brotes
        switch (eco.getClimaActual()) {
            
            case LLUVIOSO:
                // Multiplicador x2: La planta tiene 2 brotes asegurados.
                brotes = 2; 
                break;
                
            case SOLEADO:
                // Multiplicador x1.5: 1 brote asegurado + 50% de probabilidad de tener un segundo brote.
                // Math.random() genera un número entre 0.0 y 1.0. Si es mayor a 0.5, significa que "ganó" el 50%.
                if (Math.random() > 0.5) {
                    brotes = 2;
                } else {
                    brotes = 1;
                }
                break;
                
            case SEQUIA:
                // Multiplicador x0.5: Hay un 50% de probabilidad de que nazca 1 brote, de lo contrario nacen 0.
                if (Math.random() > 0.5) {
                    brotes = 1;
                } else {
                    brotes = 0;
                }
                break;
        }

        // 4. Instanciar los brotes calculados
        // Se utiliza un for para crear tantas plantas como el clima haya dictaminado.
        // Al usar el método "agregarEntidad" del ecosistema, nos aseguramos de que
        // la variable interna de "nacimientosPlantas" se incremente correctamente para el reporte.
        for (int i = 0; i < brotes; i++) {
            eco.agregarEntidad("planta", 10.0); 
        }
        
        // 5. Penalización biológica
        // A la planta original se le descuenta energía por el esfuerzo de intentar reproducirse,
        // sin importar cuántos brotes hayan nacido finalmente.
        setEnergia(getEnergia() - 10.0);
    }

    //intenta reproducirse si tiene suficiente energía y el clima lo permite
    @Override
    public void actuar(Ecosistema eco) {
        if (isViva()) {
            envejecer(); 
            
            /* --> Comentado para evitar reproducción extra en el mismo turno
             la validacion del clima la va a manejar Ecosistema
            if (puedeReproducirse()) {
                reproducirse(eco);
            }
            */
        } 
    }
    
    @Override
    public void mostrarEstado() {
        System.out.println("Planta: " + getNombre() + " Tamanio: " + this.tamanio + " Energia: " + getEnergia());
    }
    
    //-----------------------------------------------------------------------//
    //Getters y setters
    
    public int getTamanio() {
        return tamanio;
    }

    public void setTamanio(int tamanio) {
        if (tamanio < 1) {
            this.tamanio = 1;
        } else if (tamanio > 5) {
            this.tamanio = 5;
        } else {
            this.tamanio = tamanio;
        }
    }
}

    
    
