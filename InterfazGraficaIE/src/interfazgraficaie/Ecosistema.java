package interfazgraficaie;

import java.util.ArrayList;

public class Ecosistema {

    private ArrayList<Planta> plantas;
    private ArrayList<Conejo> conejos;
    private ArrayList<Lobo> lobos;

    private Clima climaActual;
    private int turnoActual;

    public Ecosistema() {
        this.plantas = new ArrayList<>();
        this.conejos = new ArrayList<>();
        this.lobos = new ArrayList<>();
        this.climaActual = Clima.SOLEADO;
        this.turnoActual = 0;
    }

    // --- Sobrecarga requerida de agregarEntidad ---
    public void agregarEntidad(String tipo) {
        agregarEntidad(tipo, -1); // -1 indica que asigna energía inicial por defecto
    }

    public void agregarEntidad(String tipo, double energia) {
        if (tipo == null) return;
        String t = tipo.trim().toLowerCase();

        switch (t) {
            case "planta":
                double energiaPlanta = (energia > 0) ? energia : (20 + Math.random() * 30);
                int tam = (int) (Math.random() * 5 + 1);
                Planta p = new Planta("Planta-" + (plantas.size() + 1), energiaPlanta, tam);
                plantas.add(p);
                System.out.println("Se agregó la planta '" + p.getNombre() + "' al ecosistema.");
                break;

            case "conejo":
                double energiaConejo = (energia > 0) ? energia : (40 + Math.random() * 30);
                Conejo c = new Conejo("Conejo-" + (conejos.size() + 1), energiaConejo, 10, 2.5);
                conejos.add(c);
                System.out.println("Se agregó el conejo '" + c.getNombre() + "' al ecosistema.");
                break;

            case "lobo":
                // Regla estricta: máximo 5 lobos en total
                if (lobos.size() >= 5) {
                    System.out.println("No se pueden agregar más de 5 lobos en total en la simulación.");
                    return;
                }
                double energiaLobo = (energia > 0) ? energia : (60 + Math.random() * 30);
                Lobo l = new Lobo("Lobo-" + (lobos.size() + 1), energiaLobo, 15, 20.0);
                lobos.add(l);
                System.out.println("Se agregó el lobo '" + l.getNombre() + "' al ecosistema.");
                break;

            default:
                System.out.println("Tipo de entidad no reconocido: " + tipo);
        }
    }

    public void mostrarEstado() {
        System.out.println("Plantas: " + plantas.size() + " | Conejos: " + conejos.size() + " | Lobos: " + lobos.size() + " | Clima: " + climaActual);
    }

    public void cambiarClima(Clima nuevo) {
        this.climaActual = nuevo;
        System.out.println("El clima cambió a: " + nuevo);
    }

    public boolean ecosistemaColapsado() {
        return plantas.isEmpty() || conejos.isEmpty() || lobos.isEmpty();
    }

    public void procesarTurno() {
        // Se completa en el siguiente paso
    }

    public void generarReporteFinal() {
        // Se completa en el siguiente paso
    }

    // --- Getters y Setters ---
    public ArrayList<Planta> getPlantas() {
        return plantas;
    }

    public void setPlantas(ArrayList<Planta> plantas) {
        this.plantas = plantas;
    }

    public ArrayList<Conejo> getConejos() {
        return conejos;
    }

    public void setConejos(ArrayList<Conejo> conejos) {
        this.conejos = conejos;
    }

    public ArrayList<Lobo> getLobos() {
        return lobos;
    }

    public void setLobos(ArrayList<Lobo> lobos) {
        this.lobos = lobos;
    }

    public Clima getClimaActual() {
        return climaActual;
    }

    public void setClimaActual(Clima climaActual) {
        this.climaActual = climaActual;
    }

    public int getTurnoActual() {
        return turnoActual;
    }

    public void setTurnoActual(int turnoActual) {
        this.turnoActual = turnoActual;
    }
}