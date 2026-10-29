package modelo;

import java.util.ArrayList;
import java.util.Collections;

public class Ecosistema {
    
    private ArrayList<Planta> plantas;
    private ArrayList<Conejo> conejos;
    private ArrayList<Lobo> lobos;
    
    // VARIABLES DE HISTORIAL (ESTADÍSTICAS EN TIEMPO REAL)
    private ArrayList<Integer> historialPlantas;
    private ArrayList<Integer> historialConejos;
    private ArrayList<Integer> historialLobos;
    
    // MVC: Lista para almacenar los eventos del turno sin usar System.out.println
    private ArrayList<String> eventosTurno;
    
    private Clima climaActual;
    
    private int turnoActual;

    // VARIABLES
    private int nacimientosPlantas = 0;
    private int nacimientosConejos = 0;
    private int nacimientosLobos = 0;

    private int muertesPlantas = 0;
    private int muertesConejos = 0;
    private int muertesLobos = 0;

    private int turnoMayorActividad = 1;
    private int maxBajasEnUnTurno = 0;
    
    public Ecosistema(int cantPlantas, int cantConejos, int cantLobos, Clima climaInicial) {
        this.plantas = new ArrayList<>();
        this.conejos = new ArrayList<>();
        this.lobos = new ArrayList<>();
        this.historialPlantas = new ArrayList<>();
        this.historialConejos = new ArrayList<>();
        this.historialLobos = new ArrayList<>();
        this.eventosTurno = new ArrayList<>();
        this.climaActual = climaInicial;
        this.turnoActual = 1;
        
        // Instanciación inicial de las poblaciones base
        for (int i = 0; i < cantPlantas; i++) { agregarEntidad("planta"); }
        for (int i = 0; i < cantConejos; i++) { agregarEntidad("conejo"); }
        for (int i = 0; i < cantLobos; i++) { agregarEntidad("lobo"); }
        
        // Reinicio de métricas para ignorar la inyección base en el reporte estadístico
        this.nacimientosPlantas = 0;
        this.nacimientosConejos = 0;
        this.nacimientosLobos = 0;
        this.eventosTurno.clear();
    }
    
    // MVC: Método para que las entidades y el ecosistema guarden los eventos del turno
    public void registrarEvento(String evento) {
        this.eventosTurno.add(evento);
    }

    public ArrayList<String> getEventosTurno() {
        return eventosTurno;
    }
    
    public void procesarTurno() {
        // MVC: Limpiamos los eventos del turno anterior y registramos la cabecera
        this.eventosTurno.clear();
        registrarEvento("=== TURNO " + turnoActual + " | Clima: " + climaActual + " ===");
        registrarEvento("Plantas: " + plantas.size() + "  Conejos: " + conejos.size() + "  Lobos: " + lobos.size());
        registrarEvento("-- Eventos --");

        // 1. TURNO PLANTAS
        for (int i = 0; i < plantas.size(); i++) {
            Planta p = plantas.get(i);
            if (p.isViva()) {
                p.actuar(this);
            }
        }

        // 2. TURNO CONEJOS
        for (int i = 0; i < conejos.size(); i++) {
            Conejo c = conejos.get(i);
            if (c.isViva()) {
                c.actuar(this);
            }
        }

        // 3. TURNO LOBOS
        for (int i = 0; i < lobos.size(); i++) {
            Lobo l = lobos.get(i);
            if (l.isViva()) {
                l.actuar(this);
            }
        }

        // 4. Fase 4: Procesamiento polimórfico de entidades reproducibles
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

        // 5. ENVEJECIMIENTO Y EFECTOS CLIMATICOS
        for (int i = 0; i < conejos.size(); i++) {
            Conejo c = conejos.get(i);
            boolean estabaVivo = c.isViva();
            c.envejecer(); // Resta -2.0 base
            if (null != climaActual) // Modificadores climáticos para Conejos
            switch (climaActual) {
                case SOLEADO:
                    c.setEnergia(c.getEnergia() + 5);
                    break;
                case LLUVIOSO:
                    c.setEnergia(c.getEnergia() + 3);
                    break;
                case SEQUIA:
                    c.setEnergia(c.getEnergia() - 5);
                    break;
                case INVIERNO:
                    c.setEnergia(c.getEnergia() - 8);
                    break;
                default:
                    break;
            }
            if (estabaVivo && !c.isViva()) {
                registrarEvento(c.getNombre() + " murio de inanicion.");
            }
        }

        for (int i = 0; i < lobos.size(); i++) {
            Lobo l = lobos.get(i);
            boolean estabaVivo = l.isViva();
            l.envejecer(); // Resta -2.0 base
            
            // Modificadores climáticos para Lobos
            if (climaActual == Clima.LLUVIOSO) {
                l.setEnergia(l.getEnergia() - 5);
            }
            if (estabaVivo && !l.isViva()) {
                registrarEvento(l.getNombre() + " murio de inanicion.");
            }
        }

        // 6. LIMPIEZA DE MUERTOS
        int bajasEsteTurno = 0;

        // Uso de copias defensivas para iterar y eliminar entidades de manera segura
        
        ArrayList<Planta> copiasPlantas = new ArrayList<>(plantas);
        for (Planta p : copiasPlantas) {
            if (!p.isViva()) {
                plantas.remove(p);
                muertesPlantas++;
                bajasEsteTurno++;
            }
        }

        ArrayList<Conejo> copiasConejos = new ArrayList<>(conejos);
        for (Conejo c : copiasConejos) {
            if (!c.isViva()) {
                conejos.remove(c);
                muertesConejos++;
                bajasEsteTurno++;
            }
        }

        ArrayList<Lobo> copiasLobos = new ArrayList<>(lobos);
        for (Lobo l : copiasLobos) {
            if (!l.isViva()) {
                lobos.remove(l);
                muertesLobos++;
                bajasEsteTurno++;
            }
        }

        if (bajasEsteTurno > maxBajasEnUnTurno) {
            maxBajasEnUnTurno = bajasEsteTurno;
            turnoMayorActividad = turnoActual;
        }

        // 7. MOSTRAR ESTADO
        registrarEvento(mostrarEstado());
        
        // --- GUARDAR HISTORIAL PARA ESTADÍSTICAS ---
        historialPlantas.add(plantas.size());
        historialConejos.add(conejos.size());
        historialLobos.add(lobos.size());

        turnoActual++;
    }
    
    // MVC: Retorna el texto del estado en lugar de imprimirlo
    public String mostrarEstado() {
        // Formato de pie de turno
        return "Estado: Plantas: " + plantas.size() + "  Conejos: " + conejos.size() + "  Lobos: " + lobos.size();
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
            
            // Probabilidad del 15% de instanciar una PlantaVenenosa (Polimorfismo)
            // Así, convive polimórficamente en la lista con las plantas normales.
            if (Math.random() <= 0.15) {
                PlantaVenenosa pv = new PlantaVenenosa("Venenosa-" + (plantas.size() + 1), e, 0, true, 2);
                plantas.add(pv);
            } else {
                Planta p = new Planta("Planta-" + (plantas.size() + 1), e, 0, true, 2);
                plantas.add(p);
            }
            
            nacimientosPlantas++;
        } else if (tipo.equalsIgnoreCase("conejo")) {
            double e = (energia > 0) ? energia : 50.0;
            Conejo c = new Conejo("Conejo-" + (conejos.size() + 1), e, 0, true, 5, 2.0);
            conejos.add(c);
            nacimientosConejos++;
        } else if (tipo.equalsIgnoreCase("lobo")) {
            if (lobos.size() >= 5) {
                registrarEvento("No se pueden agregar mas de 5 lobos.");
                return;
            }
            double e = (energia > 0) ? energia : 70.0;
            Lobo l = new Lobo("Lobo-" + (lobos.size() + 1), e, 0, true, 10, 20.0, 0);
            lobos.add(l);
            nacimientosLobos++;
        } else {
            registrarEvento("Tipo no valido: " + tipo);
        }
    }
    
    public void registrarNacimientoConejo() {
        this.nacimientosConejos++;
    }
    
    public void cambiarClima (Clima nuevo) {
        this.climaActual = nuevo;
        registrarEvento("El clima cambio a: " + nuevo);
    }
    
    public boolean ecosistemaColapsado() {
        return plantas.isEmpty() || conejos.isEmpty() || lobos.isEmpty();
    }
    
    // MVC: Retorna todo el reporte como un String manteniendo exactamente el formato original
    public String generarReporteFinal() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Reporte Final ---\n");
        sb.append("Turnos completados: ").append(turnoActual - 1).append("\n");
        sb.append("Clima final: ").append(climaActual).append("\n");

        sb.append("Sobrevivientes -> Plantas: ").append(plantas.size()).append(" | Conejos: ").append(conejos.size()).append(" | Lobos: ").append(lobos.size()).append("\n");
        sb.append("Total nacimientos -> Plantas: ").append(nacimientosPlantas).append(" | Conejos: ").append(nacimientosConejos).append(" | Lobos: ").append(nacimientosLobos).append("\n");
        sb.append("Total muertes -> Plantas: ").append(muertesPlantas).append(" | Conejos: ").append(muertesConejos).append(" | Lobos: ").append(muertesLobos).append("\n");

        if (ecosistemaColapsado()) {
            sb.append("Estado final: Colapso del ecosistema\n");
            if (plantas.isEmpty()) sb.append("Causa: No quedan plantas.\n");
            if (conejos.isEmpty()) sb.append("Causa: No quedan conejos.\n");
            if (lobos.isEmpty()) sb.append("Causa: No quedan lobos.\n");
        } else {
            sb.append("Estado final: Ecosistema en equilibrio.\n");
        }

        sb.append("Turno con mayor cantidad de bajas: Turno ").append(turnoMayorActividad).append("\n");

        // BUSCAR LOS MAS LONJEVOS 
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
            sb.append("Planta mas longeva: ").append(pMax.getNombre()).append(" (").append(pMax.getEdad()).append(" turnos)\n");
        }
        if (cMax != null) {
            sb.append("Conejo mas longevo: ").append(cMax.getNombre()).append(" (").append(cMax.getEdad()).append(" turnos)\n");
        }
        if (lMax != null) {
            sb.append("Lobo mas longevo: ").append(lMax.getNombre()).append(" (").append(lMax.getEdad()).append(" turnos)\n");
        }
        if (cazadorMax != null) {
            sb.append("Lobo mas cazador: ").append(cazadorMax.getNombre()).append(" (").append(cazadorMax.getExitosCaza()).append(" presas)\n");
        }
        
        // Lógica de ordenamiento y ranking para entidades que implementen la interfaz Peligroso
        sb.append("\n--- Ranking de Entidades Peligrosas ---\n");
        ArrayList<Peligroso> listaPeligrosos = new ArrayList<>();

        // 1. Recolectar Lobos (Función nativa addAll reemplaza el for)
        listaPeligrosos.addAll(lobos);

        // 2. Recolectar Plantas Venenosas (Usando for-each más limpio)
        for (Planta plantaActual : plantas) {
            if (plantaActual instanceof PlantaVenenosa plantaVenenosa) {
                listaPeligrosos.add(plantaVenenosa);
            }
        }

        // 3. Ordenamiento Nativo (Reemplaza los 2 bucles for del Método Burbuja)
        listaPeligrosos.sort(java.util.Comparator.comparingInt(Peligroso::getNivelPeligro).reversed());

        // 4. Armar resultados
        if (listaPeligrosos.isEmpty()) {
            sb.append("No hay entidades peligrosas vivas en el ecosistema.\n");
        } else {
            int posicionRanking = 1;
            for (Peligroso entidadPeligrosa : listaPeligrosos) {
                // Hacemos cast a Entidad solo para poder obtener el nombre heredado
                String nombrePeligroso = ((Entidad) entidadPeligrosa).getNombre(); 
                sb.append(posicionRanking).append(". ").append(nombrePeligroso).append(" (Nivel de peligro: ").append(entidadPeligrosa.getNivelPeligro()).append(")\n");
                posicionRanking++;
            }
        } 
        
        // --- ESTADÍSTICAS EN TIEMPO REAL (MÁXIMOS Y MÍNIMOS) ---
        sb.append("\n--- Picos Poblacionales Historicos ---\n");
        sb.append(calcularYMostrarMaxMinPoblacion("Plantas", historialPlantas));
        sb.append(calcularYMostrarMaxMinPoblacion("Conejos", historialConejos));
        sb.append(calcularYMostrarMaxMinPoblacion("Lobos", historialLobos));
        
        return sb.toString();
    }
    
    // MVC: Retorna el texto de máximos y mínimos en vez de imprimirlo
    private String calcularYMostrarMaxMinPoblacion(String especie, ArrayList<Integer> historial) {
        if (historial.isEmpty()) return "";

        // Métodos nativos de Java para encontrar el valor más alto y más bajo
        int max = Collections.max(historial);
        int min = Collections.min(historial);

        // indexOf() busca en qué posición exacta de la lista ocurrió ese número
        int turnoMax = historial.indexOf(max) + 1; // +1 porque el índice empieza en 0
        int turnoMin = historial.indexOf(min) + 1;

        return especie + " -> Maximo: " + max + " (Turno " + turnoMax + ") | Minimo: " + min + " (Turno " + turnoMin + ")\n";
    }

    //-----------------------------------------------------------------------//
    //GETTERS Y SETTERS
    
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