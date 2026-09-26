package interfazgraficaie;

public enum Clima {
    SOLEADO("Soleado", 1.2, 1.0),
    LLUVIOSO("Lluvioso", 1.5, 0.9),
    SEQUIA("Sequía", 0.4, 1.3),
    INVIERNO("Invierno", 0.2, 1.5);

    private final String descripcion;
    private final double factorCrecimientoFlora;
    private final double factorGastoEnergiaFauna;

    Clima(String descripcion, double factorCrecimientoFlora, double factorGastoEnergiaFauna) {
        this.descripcion = descripcion;
        this.factorCrecimientoFlora = factorCrecimientoFlora;
        this.factorGastoEnergiaFauna = factorGastoEnergiaFauna;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getFactorCrecimientoFlora() {
        return factorCrecimientoFlora;
    }

    public double getFactorGastoEnergiaFauna() {
        return factorGastoEnergiaFauna;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}