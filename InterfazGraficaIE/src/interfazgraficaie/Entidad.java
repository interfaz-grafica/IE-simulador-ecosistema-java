package interfazgraficaie;

public abstract class Entidad {

    private String nombre;
    private double energia;
    private int edad;
    private boolean viva;
    
    public Entidad(String nombre, double energia, int edad, boolean viva)
    {
        this.nombre = nombre;
        // Modificacion: usamos el setter en vez de this.energia para asegurar que la validación se aplique desde la creación del objeto
        setEnergia(energia);
        this.edad = edad;
        this.viva = viva; 
    }
    
    public abstract void actuar(Ecosistema eco); 
    
    public abstract void mostrarEstado();
    
    public void envejecer() {
        this.edad++;
        setEnergia(this.energia - 2.0);
    }
  
    //-----------------------------------------------------------------------//
    //Getters y setters 
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getEnergia() {
        return energia;
    }
    //modificacion: Se agregó la validación recomendada en clase para que la energía nunca sea negativa
    public void setEnergia(double energia) {
       if (energia < 0) {
            this.energia = 0;
        } else {
            this.energia = energia;
        }
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }
    
    public boolean isViva() {
        return viva;
    }

    public void setViva(boolean viva) {
        this.viva = viva;
    }
}
