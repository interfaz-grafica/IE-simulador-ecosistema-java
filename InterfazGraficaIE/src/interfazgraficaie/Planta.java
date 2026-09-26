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
    public void reproducirse(Ecosistema eco) {
        // Crea una nueva planta y se agrega al ecosistema
        Planta brote = new Planta(getNombre() + " (Brote)", 10.0, 0, true, 1);
       // eco.agregarEntidad(brote); ESPERAR QUE LOS CHICOS TERMINEN PARA DDESCOMENTAR
        
        // resta energía por reproducirse
        setEnergia(getEnergia() - 10.0);
    }

    @Override
    public boolean puedeReproducirse() {
       return isViva() && getEnergia() >= 20.0;
    }

    //intenta reproducirse si tiene suficiente energía y el clima lo permite
    @Override
    public void actuar(Ecosistema eco) {
        if (isViva()) {
            envejecer(); 
            
            // la validacion del clima la va a manejar Ecosistema
            if (puedeReproducirse()) {
                reproducirse(eco);
            }
        } 
    }
    
    @Override
    public void mostrarEstado() {
        System.out.println("Planta: " + getNombre() + " Tamaño: " + this.tamanio + " Energía: " + getEnergia());
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

    
    
