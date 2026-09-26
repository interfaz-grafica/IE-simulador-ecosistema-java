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
        turnoActual++;
        System.out.println("\n==========================================");
        System.out.println(">>> INICIANDO TURNO " + turnoActual + " [Clima: " + climaActual + "] <<<");
        System.out.println("==========================================");

        // 1. Accion de la Flora segun Clima
        System.out.println("\n--- 1. Fase de Flora ---");
        for (Planta p : plantas) {
            if (p.estaVivo()) {
                p.fotosintesis(climaActual);
            }
        }

        // 2. Accion de Conejos (Herbívoros buscando plantas)
        System.out.println("\n--- 2. Fase de Herbívoros (Conejos) ---");
        for (Conejo c : conejos) {
            if (c.estaVivo()) {
                // El conejo intenta comer si hay plantas disponibles
                Planta plantaDisponible = obtenerPlantaDisponible();
                c.alimentarse(plantaDisponible);
            }
        }

        // 3. Accion de Lobos (Carnívoros cazando conejos)
        System.out.println("\n--- 3. Fase de Carnívoros (Lobos) ---");
        for (Lobo l : lobos) {
            if (l.estaVivo()) {
                // El lobo intenta cazar un conejo vivo
                Conejo presaDisponible = obtenerConejoDisponible();
                l.cazar(presaDisponible);
            }
        }

        // 4. Envejecimiento y consumo general de turno
        System.out.println("\n--- 4. Consumo metabólico y ciclo vital ---");
        aplicarGastoMetabolico();

        // 5. Limpieza segura de bajas
        limpiarEntidadesMuertas();

        // 6. Resumen del turno actual
        System.out.println("\n--- Estado al cierre del Turno " + turnoActual + " ---");
        mostrarEstado();

        // 7. Evento climático aleatorio (30% de probabilidad de cambio por turno)
        verificarCambioClimatico();
    }

    // --- Metodos de apoyo internos para el motor ---

    private Planta obtenerPlantaDisponible() {
        for (Planta p : plantas) {
            if (p.estaVivo()) {
                return p;
            }
        }
        return null;
    }

    private Conejo obtenerConejoDisponible() {
        for (Conejo c : conejos) {
            if (c.estaVivo()) {
                return c;
            }
        }
        return null;
    }

    private void aplicarGastoMetabolico() {
        for (Conejo c : conejos) {
            c.gastoTurno();
        }
        for (Lobo l : lobos) {
            l.gastoTurno();
        }
    }

    private void limpiarEntidadesMuertas() {
        int plantasMuertas = 0;
        int conejosMuertos = 0;
        int lobosMuertos = 0;

        // removeIf recorre de forma segura y evita ConcurrentModificationException
        for (Planta p : new ArrayList<>(plantas)) {
            if (!p.estaVivo()) {
                plantas.remove(p);
                plantasMuertas++;
            }
        }
        for (Conejo c : new ArrayList<>(conejos)) {
            if (!c.estaVivo()) {
                conejos.remove(c);
                conejosMuertos++;
            }
        }
        for (Lobo l : new ArrayList<>(lobos)) {
            if (!l.estaVivo()) {
                lobos.remove(l);
                lobosMuertos++;
            }
        }

        if (plantasMuertas > 0 || conejosMuertos > 0 || lobosMuertos > 0) {
            System.out.println("[Bajas del turno] Plantas: -" + plantasMuertas + " | Conejos: -" + conejosMuertos + " | Lobos: -" + lobosMuertos);
        }
    }

    private void verificarCambioClimatico() {
        if (Math.random() < 0.35) { // 35% de chance de rotar clima
            Clima[] climas = Clima.values();
            Clima nuevoClima = climas[(int) (Math.random() * climas.length)];
            if (nuevoClima != this.climaActual) {
                cambiarClima(nuevoClima);
            }
        }
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