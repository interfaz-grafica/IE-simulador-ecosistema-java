package modelo;

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
    
    // MVC: Pasa de void a String para devolver el estado sin usar System.out.println
    public abstract String mostrarEstado();
    
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

    //modificacion: Se agrego la validacion recomendada en clase para que la energia nunca sea negativa
    public void setEnergia(double energia) {
        if (energia <= 0) {
            // El evento de muerte por inanición se registra en Ecosistema al limpiar las entidades muertas
            // para respetar que el modelo no imprima en consola.
            this.energia = 0;
            this.viva = false; // Muere automaticamente por inanicion
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