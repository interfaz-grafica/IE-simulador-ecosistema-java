package interfazgraficaie;

public enum Clima {
    SOLEADO("Soleado"),
    LLUVIOSO("Lluvioso"),
    SEQUIA("Sequía"),
    INVIERNO("Invierno");

    private final String descripcion;

    Clima(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}