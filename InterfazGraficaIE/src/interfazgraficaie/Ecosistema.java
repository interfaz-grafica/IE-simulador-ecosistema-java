package interfazgraficaie;

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
        this.climaActual = climaInicial;
        this.turnoActual = 1;
        
        // Poblamos las listas iniciales invocando al método de la clase
        for (int i = 0; i < cantPlantas; i++) { agregarEntidad("planta"); }
        for (int i = 0; i < cantConejos; i++) { agregarEntidad("conejo"); }
        for (int i = 0; i < cantLobos; i++) { agregarEntidad("lobo"); }
        
        // Reseteamos los contadores a 0 para que la inyección inicial no cuente como nacimientos en el reporte final
        this.nacimientosPlantas = 0;
        this.nacimientosConejos = 0;
        this.nacimientosLobos = 0;
    }
    
    public void procesarTurno() {
        // CAMBIO AQUÍ: Encabezado, conteo inicial y subtítulo de eventos según la imagen de referencia
        System.out.println("\n=== TURNO " + turnoActual + " | Clima: " + climaActual + " ===");
        System.out.println("Plantas: " + plantas.size() + "  Conejos: " + conejos.size() + "  Lobos: " + lobos.size());
        System.out.println("-- Eventos --");

        // 1. TURNO PLANTAS
        for (int i = 0; i < plantas.size(); i++) {
            Planta p = plantas.get(i);
            // CAMBIO AQUÍ: Se reemplaza estaVivo() por isViva()
            if (p.isViva()) {
                p.actuar(this);
            }
        }

        // 2. TURNO CONEJOS
        for (int i = 0; i < conejos.size(); i++) {
            Conejo c = conejos.get(i);
            // CAMBIO AQUÍ: Se reemplaza estaVivo() por isViva()
            if (c.isViva()) {
                c.actuar(this);
            }
        }

        // 3. TURNO LOBOS
        for (int i = 0; i < lobos.size(); i++) {
            Lobo l = lobos.get(i);
            // CAMBIO AQUÍ: Se reemplaza estaVivo() por isViva()
            if (l.isViva()) {
                l.actuar(this);
            }
        }

        // 4. INTERFAZ REPRODUCIBLE (polimorfismo)
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
        }

        for (int i = 0; i < lobos.size(); i++) {
            Lobo l = lobos.get(i);
            l.envejecer(); // Resta -2.0 base
            
            // Modificadores climáticos para Lobos
            if (climaActual == Clima.LLUVIOSO) {
                l.setEnergia(l.getEnergia() - 5);
            }
        }

        // 6. LIMPIEZA DE MUERTOS
        int bajasEsteTurno = 0;

        // --- CORRECCIÓN: Uso de COPIAS DEFENSIVAS en lugar de bucle inverso ---
        // Esto cumple explícitamente con lo solicitado en la Issue 21.
        
        ArrayList<Planta> copiasPlantas = new ArrayList<>(plantas);
        for (Planta p : copiasPlantas) {
            // CAMBIO AQUÍ: Se reemplaza estaVivo() por isViva()
            if (!p.isViva()) {
                plantas.remove(p);
                muertesPlantas++;
                bajasEsteTurno++;
            }
        }

        ArrayList<Conejo> copiasConejos = new ArrayList<>(conejos);
        for (Conejo c : copiasConejos) {
            // CAMBIO AQUÍ: Se reemplaza estaVivo() por isViva()
            if (!c.isViva()) {
                conejos.remove(c);
                muertesConejos++;
                bajasEsteTurno++;
            }
        }

        ArrayList<Lobo> copiasLobos = new ArrayList<>(lobos);
        for (Lobo l : copiasLobos) {
            // CAMBIO AQUÍ: Se reemplaza estaVivo() por isViva()
            if (!l.isViva()) {
                lobos.remove(l);
                muertesLobos++;
                bajasEsteTurno++;
            }
        }
        // --- FIN DE LA CORRECCIÓN ---

        if (bajasEsteTurno > maxBajasEnUnTurno) {
            maxBajasEnUnTurno = bajasEsteTurno;
            turnoMayorActividad = turnoActual;
        }

        // 7. MOSTRAR ESTADO
        mostrarEstado();
        
        // --- GUARDAR HISTORIAL PARA ESTADÍSTICAS ---
        historialPlantas.add(plantas.size());
        historialConejos.add(conejos.size());
        historialLobos.add(lobos.size());

        turnoActual++;
    }
    
    public void mostrarEstado() {
        // CAMBIO AQUÍ: Formato de pie de turno para el estado final, según la imagen de referencia
        System.out.println("Estado: Plantas: " + plantas.size() + "  Conejos: " + conejos.size() + "  Lobos: " + lobos.size());
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
            
            // --- CORRECCIÓN: INTEGRACIÓN BONUS 1 (PLANTA VENENOSA) ---
            // Le damos una chance aleatoria (por ejemplo, 15%) de que la planta instanciada sea venenosa.
            // Así, convive polimórficamente en la lista con las plantas normales.
            if (Math.random() <= 0.15) {
                PlantaVenenosa pv = new PlantaVenenosa("Venenosa-" + (plantas.size() + 1), e, 0, true, 2);
                plantas.add(pv);
            } else {
                Planta p = new Planta("Planta-" + (plantas.size() + 1), e, 0, true, 2);
                plantas.add(p);
            }
            // --- FIN DE LA CORRECCIÓN ---
            
            nacimientosPlantas++;
            // CAMBIO AQUÍ: Se borró el System.out.println("Se agrego una planta.") para evitar spam
        } else if (tipo.equalsIgnoreCase("conejo")) {
            double e = (energia > 0) ? energia : 50.0;
            Conejo c = new Conejo("Conejo-" + (conejos.size() + 1), e, 0, true, 5, 2.0);
            conejos.add(c);
            nacimientosConejos++;
            // CAMBIO AQUÍ: Se borró el System.out.println("Se agrego un conejo.") para evitar spam
        } else if (tipo.equalsIgnoreCase("lobo")) {
            if (lobos.size() >= 5) {
                System.out.println("No se pueden agregar mas de 5 lobos.");
                return;
            }
            double e = (energia > 0) ? energia : 70.0;
            Lobo l = new Lobo("Lobo-" + (lobos.size() + 1), e, 0, true, 10, 20.0, 0);
            lobos.add(l);
            nacimientosLobos++;
            // CAMBIO AQUÍ: Se borró el System.out.println("Se agrego un lobo.") para evitar spam
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
        
        // ------------------ NUEVO AGREGADO ----------------------
        // --- BONUS: ORDENAMIENTO DE ENTIDADES PELIGROSAS ---
        System.out.println("\n--- Ranking de Entidades Peligrosas ---");
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

        // 4. Imprimir resultados
        if (listaPeligrosos.isEmpty()) {
            System.out.println("No hay entidades peligrosas vivas en el ecosistema.");
        } else {
            int posicionRanking = 1;
            for (Peligroso entidadPeligrosa : listaPeligrosos) {
                // Hacemos cast a Entidad solo para poder imprimir el nombre heredado
                String nombrePeligroso = ((Entidad) entidadPeligrosa).getNombre(); 
                System.out.println(posicionRanking + ". " + nombrePeligroso + " (Nivel de peligro: " + entidadPeligrosa.getNivelPeligro() + ")");
                posicionRanking++;
            }
        } 
        
        // --- ESTADÍSTICAS EN TIEMPO REAL (MÁXIMOS Y MÍNIMOS) ---
        System.out.println("\n--- Picos Poblacionales Historicos ---");
        calcularYMostrarMaxMinPoblacion("Plantas", historialPlantas);
        calcularYMostrarMaxMinPoblacion("Conejos", historialConejos);
        calcularYMostrarMaxMinPoblacion("Lobos", historialLobos);
    }
    
    private void calcularYMostrarMaxMinPoblacion(String especie, ArrayList<Integer> historial) {
        if (historial.isEmpty()) return;

        // Métodos nativos de Java para encontrar el valor más alto y más bajo
        int max = Collections.max(historial);
        int min = Collections.min(historial);

        // indexOf() busca en qué posición exacta de la lista ocurrió ese número
        int turnoMax = historial.indexOf(max) + 1; // +1 porque el índice empieza en 0
        int turnoMin = historial.indexOf(min) + 1;

        System.out.println(especie + " -> Maximo: " + max + " (Turno " + turnoMax + ") | Minimo: " + min + " (Turno " + turnoMin + ")");
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
