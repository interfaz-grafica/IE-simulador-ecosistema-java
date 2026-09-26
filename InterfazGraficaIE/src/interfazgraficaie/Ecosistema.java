package interfazgraficaie;

import java.util.ArrayList;

public class Ecosistema {

    // Listas principales para guardar las entidades
    private ArrayList<Planta> plantas;
    private ArrayList<Conejo> conejos;
    private ArrayList<Lobo> lobos;

    private Clima climaActual;
    private int turnoActual;

    // Contadores para el reporte final
    private int totalNacimientosPlantas = 0;
    private int totalNacimientosConejos = 0;
    private int totalNacimientosLobos = 0;

    private int totalMuertesPlantas = 0;
    private int totalMuertesConejos = 0;
    private int totalMuertesLobos = 0;

    // Variables para guardar el turno con mas bajas
    private int turnoMayorActividad = 1;
    private int maxCambiosEnUnTurno = 0;

    public Ecosistema() {
        this.plantas = new ArrayList<>();
        this.conejos = new ArrayList<>();
        this.lobos = new ArrayList<>();
        this.climaActual = Clima.SOLEADO;
        this.turnoActual = 0;
    }

    // Sobrecarga: metodo simple sin pasarle energia
    public void agregarEntidad(String tipo) {
        agregarEntidad(tipo, -1);
    }

    // Metodo principal para crear y meter entidades a las listas
    public void agregarEntidad(String tipo, double energia) {
        if (tipo == null) return;
        String t = tipo.trim().toLowerCase();

        switch (t) {
            case "planta":
                double energiaPlanta = (energia > 0) ? energia : (20 + Math.random() * 30);
                int tam = (int) (Math.random() * 5 + 1);
                Planta p = new Planta("Planta-" + (plantas.size() + 1), energiaPlanta, tam);
                plantas.add(p);
                totalNacimientosPlantas++;
                System.out.println("Se agregó la planta '" + p.getNombre() + "' al ecosistema.");
                break;

            case "conejo":
                double energiaConejo = (energia > 0) ? energia : (40 + Math.random() * 30);
                Conejo c = new Conejo("Conejo-" + (conejos.size() + 1), energiaConejo, 10, 2.5);
                conejos.add(c);
                totalNacimientosConejos++;
                System.out.println("Se agregó el conejo '" + c.getNombre() + "' al ecosistema.");
                break;

            case "lobo":
                // Tope estricto de 5 lobos en todo el juego
                if (lobos.size() >= 5) {
                    System.out.println("No se pueden agregar más de 5 lobos en total en la simulación.");
                    return;
                }
                double energiaLobo = (energia > 0) ? energia : (60 + Math.random() * 30);
                Lobo l = new Lobo("Lobo-" + (lobos.size() + 1), energiaLobo, 15, 20.0);
                lobos.add(l);
                totalNacimientosLobos++;
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

    // Devuelve true si alguna poblacion se extinguio
    public boolean ecosistemaColapsado() {
        return plantas.isEmpty() || conejos.isEmpty() || lobos.isEmpty();
    }

    public void procesarTurno() {
        turnoActual++;
        System.out.println("\n==========================================");
        System.out.println(">>> INICIANDO TURNO " + turnoActual + " [Clima: " + climaActual + "] <<<");
        System.out.println("==========================================");

        // 1. Cada entidad hace lo suyo con actuar()
        System.out.println("\n--- 1. Fase de Flora ---");
        for (Planta p : plantas) {
            if (p.estaVivo()) {
                p.actuar(this);
            }
        }

        System.out.println("\n--- 2. Fase de Herbívoros (Conejos) ---");
        for (Conejo c : conejos) {
            if (c.estaVivo()) {
                c.actuar(this);
            }
        }

        System.out.println("\n--- 3. Fase de Carnívoros (Lobos) ---");
        for (Lobo l : lobos) {
            if (l.estaVivo()) {
                l.actuar(this);
            }
        }

        // 2. Aca aplicamos polimorfismo juntando todo lo que sea Reproducible
        System.out.println("\n--- 4. Intento de Reproducción (Polimorfismo Reproducible) ---");
        ArrayList<Reproducible> reproducibles = new ArrayList<>();
        reproducibles.addAll(plantas);
        reproducibles.addAll(conejos);
        for (Reproducible r : reproducibles) {
            r.intentarReproduccion(this);
        }

        // 3. Envejecen y gastan energia base
        System.out.println("\n--- 5. Envejecimiento y consumo metabólico ---");
        for (Conejo c : conejos) {
            c.envejecer();
        }
        for (Lobo l : lobos) {
            l.envejecer();
        }

        // 4. Sacamos a los que murieron en este turno
        limpiarEntidadesMuertas();

        // 5. Estado rapido por consola
        System.out.println("\n--- Estado al cierre del Turno " + turnoActual + " ---");
        mostrarEstado();

        // 6. Chance de que cambie el clima solo
        verificarCambioClimatico();
    }

    // Limpiamos las listas usando copias para que no tire ConcurrentModificationException
    private void limpiarEntidadesMuertas() {
        int plantasMuertasTurno = 0;
        int conejosMuertosTurno = 0;
        int lobosMuertosTurno = 0;

        for (Planta p : new ArrayList<>(plantas)) {
            if (!p.estaVivo()) {
                plantas.remove(p);
                plantasMuertasTurno++;
            }
        }
        for (Conejo c : new ArrayList<>(conejos)) {
            if (!c.estaVivo()) {
                conejos.remove(c);
                conejosMuertosTurno++;
            }
        }
        for (Lobo l : new ArrayList<>(lobos)) {
            if (!l.estaVivo()) {
                lobos.remove(l);
                lobosMuertosTurno++;
            }
        }

        totalMuertesPlantas += plantasMuertasTurno;
        totalMuertesConejos += conejosMuertosTurno;
        totalMuertesLobos += lobosMuertosTurno;

        // Vemos si este turno tuvo mas bajas que los anteriores
        int bajasTotalesTurno = plantasMuertasTurno + conejosMuertosTurno + lobosMuertosTurno;
        if (bajasTotalesTurno > maxCambiosEnUnTurno) {
            maxCambiosEnUnTurno = bajasTotalesTurno;
            turnoMayorActividad = turnoActual;
        }

        if (bajasTotalesTurno > 0) {
            System.out.println("[Bajas del turno] Plantas: -" + plantasMuertasTurno + " | Conejos: -" + conejosMuertosTurno + " | Lobos: -" + lobosMuertosTurno);
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
        System.out.println("\n========================================================");
        System.out.println("            REPORTE FINAL DE LA SIMULACION             ");
        System.out.println("========================================================");
        System.out.println("Turnos completados: " + turnoActual);
        System.out.println("Clima final: " + climaActual.getDescripcion());

        System.out.println("\n--- Estado Final de Poblaciones ---");
        System.out.println("Plantas sobrevivientes : " + plantas.size());
        System.out.println("Conejos sobrevivientes : " + conejos.size());
        System.out.println("Lobos sobrevivientes   : " + lobos.size());

        System.out.println("\n--- Total de Nacimientos / Ingresos Registrados ---");
        System.out.println("Total plantas incorporadas : " + totalNacimientosPlantas);
        System.out.println("Total conejos nacidos      : " + totalNacimientosConejos);
        System.out.println("Total lobos incorporados   : " + totalNacimientosLobos);

        System.out.println("\n--- Bajas Historicas Registradas ---");
        System.out.println("Plantas extinguidas/consumidas : " + totalMuertesPlantas);
        System.out.println("Conejos cazados/fallecidos     : " + totalMuertesConejos);
        System.out.println("Lobos fallecidos               : " + totalMuertesLobos);

        System.out.println("\n--- Diagnostico del Ecosistema ---");
        if (ecosistemaColapsado()) {
            System.out.println("ESTADO: COLAPSO ECOLOGICO DETECTADO");
            if (plantas.isEmpty()) System.out.println("-> Causa principal: Extincion de flora.");
            if (conejos.isEmpty()) System.out.println("-> Causa principal: Extincion de herbivoros (conejos).");
            if (lobos.isEmpty()) System.out.println("-> Causa principal: Extincion de depredadores tope (lobos).");
        } else {
            System.out.println("ESTADO: EQUILIBRIO SOSTENIBLE ALCANZADO");
            System.out.println("Todas las poblaciones conservaron especimenes activos.");
        }

        System.out.println("\n--- Records de la Simulación ---");
        System.out.println("Turno con mayor actividad/bajas: Turno " + turnoMayorActividad);

        // Busqueda de los mas viejos recorriendo cada lista
        Planta plantaLongeva = null;
        for (Planta p : plantas) {
            if (plantaLongeva == null || p.getEdad() > plantaLongeva.getEdad()) {
                plantaLongeva = p;
            }
        }
        Conejo conejoLongevo = null;
        for (Conejo c : conejos) {
            if (conejoLongevo == null || c.getEdad() > conejoLongevo.getEdad()) {
                conejoLongevo = c;
            }
        }
        Lobo loboLongevo = null;
        Lobo loboMasCazador = null;
        for (Lobo l : lobos) {
            if (loboLongevo == null || l.getEdad() > loboLongevo.getEdad()) {
                loboLongevo = l;
            }
            if (loboMasCazador == null || l.getExitosCaza() > loboMasCazador.getExitosCaza()) {
                loboMasCazador = l;
            }
        }

        System.out.println("Planta más longeva : " + (plantaLongeva != null ? plantaLongeva.getNombre() + " (" + plantaLongeva.getEdad() + " turnos)" : "Ninguna"));
        System.out.println("Conejo más longevo : " + (conejoLongevo != null ? conejoLongevo.getNombre() + " (" + conejoLongevo.getEdad() + " turnos)" : "Ninguno"));
        System.out.println("Lobo más longevo   : " + (loboLongevo != null ? loboLongevo.getNombre() + " (" + loboLongevo.getEdad() + " turnos)" : "Ninguno"));
        System.out.println("Lobo más cazador   : " + (loboMasCazador != null ? loboMasCazador.getNombre() + " con " + loboMasCazador.getExitosCaza() + " cacerias" : "Ninguno"));
        System.out.println("========================================================\n");
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