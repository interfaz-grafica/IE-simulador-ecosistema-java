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
        this.turnoActual = 1;
    }
    
    public void procesarTurno() {
    }
    
    public void mostrarEstado() {
    }
    
    public void agregarEntidad(String tipo) {
    }
    
    public void agregarEntidad(String tipo, double energia) {
    }
    
    public void cambiarClima (Clima nuevo) {
    }
    
    public boolean ecosistemaColapsado() {
        return false;
    }
    
    public void generarReporteFinal() {
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
