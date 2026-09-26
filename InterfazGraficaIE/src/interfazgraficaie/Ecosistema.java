package interfazgraficaie;

import java.util.ArrayList;

public class Ecosistema {
    
    private ArrayList<Planta> plantas;
    private ArrayList<Conejo> conejos;
    private ArrayList<Lobo> lobos;  
    
    private Clima climaActual;
    
    private int turnoActual;

    // Variables para el reporte final
    private int nacimientosPlantas = 0;
    private int nacimientosConejos = 0;
    private int nacimientosLobos = 0;

    private int muertesPlantas = 0;
    private int muertesConejos = 0;
    private int muertesLobos = 0;

    private int turnoMayorActividad = 1;
    private int maxBajasEnUnTurno = 0;
    
    public Ecosistema() {
        this.plantas = new ArrayList<>();
        this.conejos = new ArrayList<>();
        this.lobos = new ArrayList<>();
        this.climaActual = Clima.SOLEADO;
        this.turnoActual = 1;
    }
    
    public void procesarTurno() {
        System.out.println("Turno: " + turnoActual + " - Clima: " + climaActual);

        // 1. Actuan las plantas
        for (int i = 0; i < plantas.size(); i++) {
            Planta p = plantas.get(i);
            if (p.estaVivo()) {
                p.actuar(this);
            }
        }

        // 2. Actuan los conejos
        for (int i = 0; i < conejos.size(); i++) {
            Conejo c = conejos.get(i);
            if (c.estaVivo()) {
                c.actuar(this);
            }
        }

        // 3. Actuan los lobos
        for (int i = 0; i < lobos.size(); i++) {
            Lobo l = lobos.get(i);
            if (l.estaVivo()) {
                l.actuar(this);
            }
        }

        // 4. Polimorfismo con la interfaz Reproducible (Clase 9)
        ArrayList<Reproducible> listaReproduccion = new ArrayList<>();
        for (int i = 0; i < plantas.size(); i++) {
            listaReproduccion.add(plantas.get(i));
        }
        for (int i = 0; i < conejos.size(); i++) {
            listaReproduccion.add(conejos.get(i));
        }

        for (int i = 0; i < listaReproduccion.size(); i++) {
            listaReproduccion.get(i).intentarReproduccion(this);
        }

        // 5. Envejecimiento
        for (int i = 0; i < conejos.size(); i++) {
            conejos.get(i).envejecer();
        }
        for (int i = 0; i < lobos.size(); i++) {
            lobos.get(i).envejecer();
        }

        // 6. Limpieza de muertos con for hacia atras para no saltar indices
        int bajasEsteTurno = 0;

        for (int i = plantas.size() - 1; i >= 0; i--) {
            if (!plantas.get(i).estaVivo()) {
                plantas.remove(i);
                muertesPlantas++;
                bajasEsteTurno++;
            }
        }

        for (int i = conejos.size() - 1; i >= 0; i--) {
            if (!conejos.get(i).estaVivo()) {
                conejos.remove(i);
                muertesConejos++;
                bajasEsteTurno++;
            }
        }

        for (int i = lobos.size() - 1; i >= 0; i--) {
            if (!lobos.get(i).estaVivo()) {
                lobos.remove(i);
                muertesLobos++;
                bajasEsteTurno++;
            }
        }

        if (bajasEsteTurno > maxBajasEnUnTurno) {
            maxBajasEnUnTurno = bajasEsteTurno;
            turnoMayorActividad = turnoActual;
        }

        // 7. Mostrar estado
        mostrarEstado();

        turnoActual++;
    }
    
    public void mostrarEstado() {
        System.out.println("Plantas: " + plantas.size() + " | Conejos: " + conejos.size() + " | Lobos: " + lobos.size() + " | Clima: " + climaActual);
    }
    
    public void agregarEntidad(String tipo) {
        agregarEntidad(tipo, -1);
    }
    
    public void agregarEntidad(String tipo, double energia) {
        if (tipo == null) {
            return;
        }

        if (tipo.equalsIgnoreCase("planta")) {
            double e = (energia > 0) ? energia : 30.0;
            Planta p = new Planta("Planta-" + (plantas.size() + 1), e, 2);
            plantas.add(p);
            nacimientosPlantas++;
            System.out.println("Se agrego una planta.");
        } else if (tipo.equalsIgnoreCase("conejo")) {
            double e = (energia > 0) ? energia : 50.0;
            Conejo c = new Conejo("Conejo-" + (conejos.size() + 1), e, 10, 2.0);
            conejos.add(c);
            nacimientosConejos++;
            System.out.println("Se agrego un conejo.");
        } else if (tipo.equalsIgnoreCase("lobo")) {
            if (lobos.size() >= 5) {
                System.out.println("No se pueden agregar mas de 5 lobos.");
                return;
            }
            double e = (energia > 0) ? energia : 70.0;
            Lobo l = new Lobo("Lobo-" + (lobos.size() + 1), e, 15, 20.0);
            lobos.add(l);
            nacimientosLobos++;
            System.out.println("Se agrego un lobo.");
        } else {
            System.out.println("Tipo no valido: " + tipo);
        }
    }
    
    public void cambiarClima (Clima nuevo) {
        this.climaActual = nuevo;
        System.out.println("El clima cambio a: " + nuevo);
    }
    
    public boolean ecosistemaColapsado() {
        return plantas.isEmpty() || conejos.isEmpty() || lobos.isEmpty();
    }
    
    public void generarReporteFinal() {
        System.out.println("--- Reporte Final ---");
        System.out.println("Turnos completados: " + (turnoActual - 1));
        System.out.println("Clima final: " + climaActual);

        System.out.println("Sobrevivientes -> Plantas: " + plantas.size() + " | Conejos: " + conejos.size() + " | Lobos: " + lobos.size());
        System.out.println("Total nacimientos -> Plantas: " + nacimientosPlantas + " | Conejos: " + nacimientosConejos + " | Lobos: " + nacimientosLobos);
        System.out.println("Total muertes -> Plantas: " + muertesPlantas + " | Conejos: " + muertesConejos + " | Lobos: " + muertesLobos);

        if (ecosistemaColapsado()) {
            System.out.println("Estado final: Colapso del ecosistema");
            if (plantas.isEmpty()) System.out.println("Causa: No quedan plantas.");
            if (conejos.isEmpty()) System.out.println("Causa: No quedan conejos.");
            if (lobos.isEmpty()) System.out.println("Causa: No quedan lobos.");
        } else {
            System.out.println("Estado final: Ecosistema en equilibrio.");
        }

        System.out.println("Turno con mayor cantidad de bajas: Turno " + turnoMayorActividad);

        // Buscar mas longevos recorriendo con un for clasico
        Planta pMax = null;
        for (int i = 0; i < plantas.size(); i++) {
            if (pMax == null || plantas.get(i).getEdad() > pMax.getEdad()) {
                pMax = plantas.get(i);
            }
        }

        Conejo cMax = null;
        for (int i = 0; i < conejos.size(); i++) {
            if (cMax == null || conejos.get(i).getEdad() > cMax.getEdad()) {
                cMax = conejos.get(i);
            }
        }

        Lobo lMax = null;
        Lobo cazadorMax = null;
        for (int i = 0; i < lobos.size(); i++) {
            Lobo actual = lobos.get(i);
            if (lMax == null || actual.getEdad() > lMax.getEdad()) {
                lMax = actual;
            }
            if (cazadorMax == null || actual.getExitosCaza() > cazadorMax.getExitosCaza()) {
                cazadorMax = actual;
            }
        }

        if (pMax != null) {
            System.out.println("Planta mas longeva: " + pMax.getNombre() + " (" + pMax.getEdad() + " turnos)");
        }
        if (cMax != null) {
            System.out.println("Conejo mas longevo: " + cMax.getNombre() + " (" + cMax.getEdad() + " turnos)");
        }
        if (lMax != null) {
            System.out.println("Lobo mas longevo: " + lMax.getNombre() + " (" + lMax.getEdad() + " turnos)");
        }
        if (cazadorMax != null) {
            System.out.println("Lobo mas cazador: " + cazadorMax.getNombre() + " (" + cazadorMax.getExitosCaza() + " presas)");
        }
    }
    
    //-----------------------------------------------------------------------//
    //Getters y setters
    
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