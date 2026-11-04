package controlador;

/*
    Coordina la comunicación entre la vista (VentanaEcosistema), el modelo
    (Ecosistema) y la persistencia (SimulacionDAO).

    Reglas que respeta: 
    - No importa ni usa nada de Swing (ni JOptionPane ni
    listeners). 
    - La vista nunca recibe objetos del modelo: todo lo que le
    entrega son String, int, boolean o String[][] ya armados. 
    - Los métodos que pueden fallar devuelven un String con el mensaje de error, 
    o null si todo salió bien (la vista decide cómo mostrarlo).
*/

import dao.SimulacionDAO;
import modelo.Clima;
import modelo.Ecosistema;
import java.text.Normalizer;
import java.util.ArrayList;
import modelo.Conejo;
import modelo.Lobo;
import modelo.Planta;
import modelo.PlantaVenenosa;

public class Controlador {
    
    // Rangos válidos de la configuración inicial (consigna)
    public static final int MIN_PLANTAS = 5;
    public static final int MAX_PLANTAS = 30;
    public static final int MIN_CONEJOS = 2;
    public static final int MAX_CONEJOS = 15;
    public static final int MIN_LOBOS = 1;
    public static final int MAX_LOBOS = 5;
    public static final int MIN_TURNOS = 10;
    public static final int MAX_TURNOS = 50;
    public static final int TURNOS_ENTRE_INTERVENCIONES = 3;
    public static final int TOPE_LOBOS = 5;
    
    private SimulacionDAO dao;
    private Ecosistema ecosistema;
    
    private int cantidadInicialPlantas;
    private int cantidadInicialConejos;
    private int cantidadInicialLobos;
    private Clima climaInicial;
    private int turnosTotales;
    

    // Resultado del último guardado automático (null = se guardó bien)
    private String mensajeGuardado = null;

    private int lobosAgregados = 0;
       // Se pone en true cuando el usuario ya intervino en la ventana actual (cada 3 turnos)
    private boolean intervencionRealizada = false; 

    public Controlador(SimulacionDAO dao) {
        this.dao = dao;
    }
    
    //=======================================================================//
    // CONFIGURACIÓN Y CICLO DE VIDA DE LA SIMULACIÓN
    //=======================================================================//

    /*
        Valida los datos ingresados en la pantalla de configuración. No crea la
        simulación: sirve para mostrar el error antes de pedir confirmación.

        @return mensaje de error, o null si todos los datos son válidos.
     */
    public String validarConfiguracion(String plantasStr, String conejosStr, String lobosStr, String turnosStr) {
        if (validarEnteroEnRango(plantasStr, MIN_PLANTAS, MAX_PLANTAS) == -1) {
            return mensajeRango("plantas", plantasStr, MIN_PLANTAS, MAX_PLANTAS);
        }
        if (validarEnteroEnRango(conejosStr, MIN_CONEJOS, MAX_CONEJOS) == -1) {
            return mensajeRango("conejos", conejosStr, MIN_CONEJOS, MAX_CONEJOS);
        }
        if (validarEnteroEnRango(lobosStr, MIN_LOBOS, MAX_LOBOS) == -1) {
            return mensajeRango("lobos", lobosStr, MIN_LOBOS, MAX_LOBOS);
        }
        if (validarEnteroEnRango(turnosStr, MIN_TURNOS, MAX_TURNOS) == -1) {
            return mensajeRango("turnos totales", turnosStr, MIN_TURNOS, MAX_TURNOS);
        }
        return null;
    }

    
    /*
        Valida la configuración, borra la partida guardada anterior y crea una
        simulación nueva. La vista debe llamarlo DESPUÉS de que el usuario
        confirme la nueva partida.

        @param climaTexto texto del combo de clima (por ejemplo "Soleado").
        @return mensaje de error, o null si la simulación se creó.
     */
    public String iniciarNuevaSimulacion(String plantasStr, String conejosStr, String lobosStr,
        String turnosStr, String climaTexto) {
        String error = validarConfiguracion(plantasStr, conejosStr, lobosStr, turnosStr);
        if (error != null) {
            return error;
        }
        Clima clima = obtenerClimaPorNombre(climaTexto);
        if (clima == null) {
            return "(!) Error: Selecciona un clima inicial válido.";
        }
        
        // Al empezar una partida nueva se descarta la guardada (el usuario ya confirmó).
        if (dao != null) {
            dao.borrarPartidaGuardada();
        }

        this.cantidadInicialPlantas = validarEnteroEnRango(plantasStr, MIN_PLANTAS, MAX_PLANTAS);
        this.cantidadInicialConejos = validarEnteroEnRango(conejosStr, MIN_CONEJOS, MAX_CONEJOS);
        this.cantidadInicialLobos = validarEnteroEnRango(lobosStr, MIN_LOBOS, MAX_LOBOS);
        this.turnosTotales = validarEnteroEnRango(turnosStr, MIN_TURNOS, MAX_TURNOS);
        this.climaInicial = clima;
        
        this.lobosAgregados = 0;
        this.intervencionRealizada = false;        

        this.ecosistema = new Ecosistema(cantidadInicialPlantas, cantidadInicialConejos, cantidadInicialLobos, climaInicial);
        
        // El modelo necesita saber cuántos turnos dura la simulación (y el DAO lo guarda)
        ecosistema.setTurnosMaximos(turnosTotales);
        
        guardarProgreso();
        
        return null;
       
    }
    

    //Resumen de la configuración inicial en texto.
    public String obtenerResumenConfiguracionInicial() {
        return "-------------------------------------------\n"
                + "DATOS INGRESADOS:\n"
                + "Plantas iniciales: " + cantidadInicialPlantas + "\n"
                + "Conejos iniciales: " + cantidadInicialConejos + "\n"
                + "Lobos iniciales:   " + cantidadInicialLobos + "\n"
                + "Clima inicial:     " + nombreClima(climaInicial) + "\n"
                + "Duracion total:    " + turnosTotales + " turnos\n"
                + "-------------------------------------------";
    }
    
    
    //=======================================================================//
    // UTILIDADES DE VALIDACIÓN Y CONVERSIÓN
    //=======================================================================//

    /** Convierte el texto a entero y verifica el rango. Devuelve -1 si no es válido. */
    public int validarEnteroEnRango(String inputUsuario, int min, int max) {
        if (inputUsuario == null) {
            return -1;
        }
        try {
            int numero = Integer.parseInt(inputUsuario.trim());
            return (numero >= min && numero <= max) ? numero : -1;
        } catch (NumberFormatException e) {
            return -1; // input vacío o no numérico
        }
    }

    
    // Arma el mensaje de error distinguiendo formato inválido de fuera de rango.
    private String mensajeRango(String campo, String valor, int min, int max) {
        String texto = (valor == null) ? "" : valor.trim();
        if (texto.isEmpty()) {
            return "(!) Error: Ingresa la cantidad de " + campo + ".";
        }
        try {
            Integer.parseInt(texto);
            return "(!) Error: La cantidad de " + campo + " debe estar entre " + min + " y " + max + ".";
        } catch (NumberFormatException e) {
            return "(!) Error: La cantidad de " + campo + " debe ser un número entero.";
        }
    }

    
    /**
     * Convierte el texto de un combo en un Clima. Acepta mayúsculas/minúsculas,
     * con o sin tilde, y "Lluvia" como sinónimo de "Lluvioso". Devuelve null si
     * el texto no es un clima (por ejemplo "Selecciona nuevo clima").
     */
    public Clima obtenerClimaPorNombre(String texto) {
        if (texto == null) {
            return null;
        }
        String limpio = Normalizer.normalize(texto.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toUpperCase();
        switch (limpio) {
            case "SOLEADO":
                return Clima.SOLEADO;
            case "LLUVIOSO":
            case "LLUVIA":
                return Clima.LLUVIOSO;
            case "SEQUIA":
                return Clima.SEQUIA;
            case "INVIERNO":
                return Clima.INVIERNO;
            default:
                return null;
        }
    }

    
    /** Nombres de climas para mostrar en combos, en el orden del enum. */
    public String[] obtenerNombresClimas() {
        Clima[] climas = Clima.values();
        String[] nombres = new String[climas.length];
        for (int i = 0; i < climas.length; i++) {
            nombres[i] = nombreClima(climas[i]);
        }
        return nombres;
    }

    
    private String nombreClima(Clima clima) {
        if (clima == null) {
            return "-";
        }
        switch (clima) {
            case SOLEADO:
                return "Soleado";
            case LLUVIOSO:
                return "Lluvioso";
            case SEQUIA:
                return "Sequía";
            case INVIERNO:
                return "Invierno";
            default:
                return clima.name();
        }
    }

    
    /** Indica si hay una partida guardada para habilitar "Retomar simulación". */
    public boolean hayPartidaGuardada() {
        return dao != null && dao.existePartidaGuardada();
    }

    
    /**
     * Pide al DAO la partida guardada y la deja lista para seguir jugando.
     *
     * @return mensaje de error, o null si la partida se cargó.
     */
    public String cargarPartidaGuardada() {
        
        if (!hayPartidaGuardada()) {
            return "(!) No hay ninguna simulación guardada para retomar.";
        }
        
        Ecosistema cargado = dao.cargarEstado();
        
        if (cargado == null) {
            return "(!) La simulación guardada está dañada o incompleta y no se pudo cargar.";
        }
        
        if (cargado.getTurnosMaximos() < MIN_TURNOS || cargado.getTurnosMaximos() > MAX_TURNOS) {
            return "(!) La simulación guardada no tiene una cantidad de turnos válida.";
        }
        
        this.ecosistema = cargado;
        this.climaInicial = cargado.getClimaActual();
        this.cantidadInicialPlantas = cargado.getPlantas().size();
        this.cantidadInicialConejos = cargado.getConejos().size();
        this.cantidadInicialLobos = cargado.getLobos().size();
        // El DAO guarda los turnos totales y los lobos agregados dentro del ecosistema
        this.turnosTotales = cargado.getTurnosMaximos();
        this.lobosAgregados = cargado.getLobosAgregados();
        this.intervencionRealizada = false;
        
        return null;
    }

    
    /**
     * Ordena al modelo procesar un turno y al DAO guardar el progreso. Si la
     * simulación terminó, borra la partida guardada (ya no hay nada que
     * retomar).
     *
     * @return mensaje de error, o null si el turno se procesó.
     */
    public String avanzarTurno() {
        if (ecosistema == null) {
            return "(!) No hay ninguna simulación en curso.";
        }
        if (simulacionTerminada()) {
            return "(!) La simulación ya terminó.";
        }
        ecosistema.procesarTurno();
        intervencionRealizada = false;

        if (simulacionTerminada()) {
            if (dao != null) {
                dao.borrarPartidaGuardada();
            }
            mensajeGuardado = null;
        } else {
            guardarProgreso();
        }
        return null;
    }

    
    // Guarda el estado actual con el DAO y recuerda si falló.
    private void guardarProgreso() {
        if (dao == null) {
            mensajeGuardado = "(!) No hay sistema de guardado configurado.";
        } else if (dao.guardarEstado(ecosistema)) {
            mensajeGuardado = null;
        } else {
            mensajeGuardado = "(!) No se pudo guardar el progreso de la simulación.";
        }
    }

    
    /** Mensaje del último guardado automático, o null si se guardó bien. */
    public String getMensajeGuardado() {
        return mensajeGuardado;
    }

    
    /** Evalúa las condiciones de fin: se alcanzó el límite de turnos o colapsó el ecosistema. */
    public boolean simulacionTerminada() {
        if (ecosistema == null) {
            return true;
        }
        return (ecosistema.getTurnoActual() > turnosTotales) || ecosistema.ecosistemaColapsado();
    }
    
    
    //=======================================================================//
    // INTERVENCIÓN (cada 3 turnos)
    //=======================================================================//

    /** true si el turno recién procesado es múltiplo de 3 y la simulación sigue. */
    public boolean esTurnoDeIntervencion() {
        if (ecosistema == null) {
            return false;
        }
        int turnoRecienProcesado = ecosistema.getTurnoActual() - 1;
        return turnoRecienProcesado > 0
                && (turnoRecienProcesado % TURNOS_ENTRE_INTERVENCIONES == 0)
                && !simulacionTerminada();
    }

    
    /** true si ahora se puede intervenir (es turno de intervención y todavía no se usó). */
    public boolean puedeIntervenir() {
        return esTurnoDeIntervencion() && !intervencionRealizada;
    }

    
    /** Texto para lblEstadoIntervencion. */
    public String obtenerEstadoIntervencion() {
        if (ecosistema == null || simulacionTerminada()) {
            return "Intervención no disponible.";
        }
        if (puedeIntervenir()) {
            return "¡Puedes intervenir este turno!";
        }
        if (esTurnoDeIntervencion()) {
            return "Ya interviniste en este turno.";
        }
        int procesados = ecosistema.getTurnoActual() - 1;
        int faltan = TURNOS_ENTRE_INTERVENCIONES - (procesados % TURNOS_ENTRE_INTERVENCIONES);
        return "Próxima intervención en " + faltan + (faltan == 1 ? " turno." : " turnos.");
    }

    
    /**
     * Aplica la intervención del turno: un cambio de clima, una entidad nueva o
     * ambos. Los valores que no sean válidos (por ejemplo "Selecciona nuevo
     * clima" o "Agregar Entidad") se ignoran.
     *
     * @return mensaje para mostrar al usuario (resultado o error).
     */
    public String intervenir(String climaTexto, String entidadTexto) {
        if (!puedeIntervenir()) {
            return "(!) Solo puedes intervenir una vez cada " + TURNOS_ENTRE_INTERVENCIONES + " turnos.";
        }
        Clima nuevoClima = obtenerClimaPorNombre(climaTexto);
        String tipo = normalizarTipoEntidad(entidadTexto);
        if (nuevoClima == null && tipo == null) {
            return "(!) Selecciona un clima o una entidad para intervenir.";
        }

        // Se valida el tope de lobos ANTES de aplicar cualquier cambio.
        if ("lobo".equals(tipo) && superaTopeLobos()) {
            return mensajeTopeLobos();
        }

        StringBuilder resultado = new StringBuilder();
        if (nuevoClima != null) {
            ecosistema.cambiarClima(nuevoClima);
            resultado.append("El clima cambió a ").append(nombreClima(nuevoClima)).append(". ");
        }
        
        // Elegir el mismo clima que ya hay no es un cambio: se avisa sin gastar la intervención.
        if (nuevoClima != null && nuevoClima == ecosistema.getClimaActual()) {
            return "(!) El clima ya es " + nombreClima(nuevoClima) + ". Elige un clima distinto.";
        }      
        
        if (tipo != null) {
            ecosistema.agregarEntidad(tipo);
            if ("lobo".equals(tipo)) {
                lobosAgregados++;
                ecosistema.setLobosAgregados(lobosAgregados); // para que el DAO lo guarde
            }
            resultado.append("Se agregó un/a ").append(tipo).append(" al ecosistema.");
        }
        intervencionRealizada = true;
        guardarProgreso();
        return resultado.toString().trim();
    }

    
    /** Intervención solo de clima. */
    public String intervenirCambiarClima(String climaTexto) {
        if (obtenerClimaPorNombre(climaTexto) == null) {
            return "(!) Selecciona un clima válido.";
        }
        return intervenir(climaTexto, null);
    }

    
    /** Intervención solo de entidad ("Planta", "Conejo" o "Lobo"). */
    public String intervenirAgregarEntidad(String entidadTexto) {
        if (normalizarTipoEntidad(entidadTexto) == null) {
            return "(!) Opción de entidad inválida.";
        }
        return intervenir(null, entidadTexto);
    }

    
    /** true si agregar un lobo más superaría el tope de 5 lobos en el ecosistema. */
    public boolean superaTopeLobos() {
        return ecosistema != null && ecosistema.getLobos().size() >= TOPE_LOBOS;
    }

    
    private String mensajeTopeLobos() {
        return "(!) Límite máximo de " + TOPE_LOBOS + " lobos alcanzado. No puedes agregar más lobos.";
    }
    
    
    // Devuelve "planta", "conejo" o "lobo", o null si el texto no es una entidad.
    private String normalizarTipoEntidad(String texto) {
        if (texto == null) {
            return null;
        }
        String limpio = texto.trim().toLowerCase();
        if (limpio.equals("planta") || limpio.equals("conejo") || limpio.equals("lobo")) {
            return limpio;
        }
        return null;
    }

    
    //=======================================================================//
    // DATOS EMPAQUETADOS PARA LA VISTA (sin exponer el modelo)
    //=======================================================================//

    /** Turno que se va a jugar a continuación, para lblTurno (ej: "Turno 4 / 20"). */
    /**
     * Último turno jugado, para lblTurno (ej: "TURNO: 3 / 20").
     * Muestra el mismo número que la cabecera de los eventos ("=== TURNO 3") y que la
     * regla de intervención (se habilita cuando este número es 3, 6, 9...).
     * Antes de jugar el primer turno muestra "INICIO / 20".
     */
    public String obtenerTextoTurno() {
        if (ecosistema == null) {
            return "TURNO: - / -";
        }
        int turnosJugados = Math.min(ecosistema.getTurnoActual() - 1, turnosTotales);
        if (turnosJugados == 0) {
            return "INICIO / " + turnosTotales;
        }
        return "TURNO: " + turnosJugados + " / " + turnosTotales;
    }
    
    
    public int getTurnoActual() {
        return ecosistema == null ? 0 : ecosistema.getTurnoActual();
    }

    
    /** Clima actual para lblClima (ej: "Soleado"). */
    public String obtenerTextoClima() {
        return "CLIMA: " + (ecosistema == null ? "-" : nombreClima(ecosistema.getClimaActual()).toUpperCase());
    }

    
    /** Contadores para los labels superiores: {plantas, conejos, lobos}. */
    public int[] obtenerContadores() {
        if (ecosistema == null) {
            return new int[]{0, 0, 0};
        }
        return new int[]{
            ecosistema.getPlantas().size(),
            ecosistema.getConejos().size(),
            ecosistema.getLobos().size()
        };
    }

    /**
     * Filas para tblTablaEntidad. Cada fila respeta el orden de columnas:
     * Nombre, Tipo, Energía, Edad, Detalle.
     */
    public String[][] obtenerFilasEntidades() {
        ArrayList<String[]> filas = new ArrayList<>();
        if (ecosistema != null) {
            for (Planta p : ecosistema.getPlantas()) {
                String tipo = (p instanceof PlantaVenenosa) ? "Planta venenosa" : "Planta";
                filas.add(fila(p.getNombre(), tipo, p.getEnergia(), p.getEdad(), "Tamaño: " + p.getTamanio()));
            }
            for (Conejo c : ecosistema.getConejos()) {
                filas.add(fila(c.getNombre(), "Conejo", c.getEnergia(), c.getEdad(),
                        "Velocidad: " + c.getVelocidad() + " | Peso: " + c.getPeso()));
            }
            for (Lobo l : ecosistema.getLobos()) {
                filas.add(fila(l.getNombre(), "Lobo", l.getEnergia(), l.getEdad(),
                        "Cacerías: " + l.getExitosCaza()));
            }
        }
        return filas.toArray(new String[0][]);
    }

    
    private String[] fila(String nombre, String tipo, double energia, int edad, String detalle) {
        return new String[]{nombre, tipo, String.format("%.1f", energia), String.valueOf(edad), detalle};
    }

    
    /** Eventos del último turno, uno por línea, para txtEventosArea. */
    public String obtenerEventosTurno() {
        if (ecosistema == null) {
            return "";
        }
        return String.join("\n", ecosistema.getEventosTurno());
    }

    
    //=======================================================================//
    // REPORTE FINAL (datos que calcula el modelo, empaquetados como texto)
    //=======================================================================//

    
    /** Causa de fin para lblCausaFin (ej: "LIMITE DE TURNOS ALCANZADO"). */
    public String obtenerCausaFin() {
        return ecosistema == null ? "" : "Causa de Fin: " + ecosistema.getCausaFin();
    }

    
    /** Detalle de la causa para lblDetalleCausa (ej: "Se extinguieron: conejos"). */
    public String obtenerDetalleCausa() {
        return ecosistema == null ? "" : "(" + ecosistema.getDetalleCausaFin() + ")";
    }

    
    /** Texto para lblRecordActividad: el turno con más bajas. */
    public String obtenerRecordActividad() {
        if (ecosistema == null) {
            return "";
        }
        if (ecosistema.getMaxBajasEnUnTurno() == 0) {
            return "Turno de mayor actividad: no hubo bajas.";
        }
        return "Turno de mayor actividad: Turno " + ecosistema.getTurnoMayorActividad()
                + " (" + ecosistema.getMaxBajasEnUnTurno() + " bajas)";
    }

    
    /** Texto para lblRecordCazador: el lobo con más cacerías (vivo o muerto). */
    public String obtenerRecordCazador() {
        if (ecosistema == null) {
            return "";
        }
        if (ecosistema.getRecordCazadorCacerias() == 0) {
            return "Lobo con más cacerías: ningún lobo cazó.";
        }
        return "Lobo con más cacerías: " + ecosistema.getRecordCazadorNombre()
                + " (" + ecosistema.getRecordCazadorCacerias() + " presas)";
    }

    
    /**
     * Filas para tblBalanceVital, en el orden de columnas de la tabla:
     * "", Plantas, Lobos, Conejos, Plantas Venenosas.
     * Filas: Total nacimientos, Total muertes, Más longeva.
     */
    public String[][] obtenerFilasBalanceVital() {
        if (ecosistema == null) {
            return new String[0][];
        }
        // En el modelo, las plantas venenosas están incluidas en el total de plantas
        int nacPlantasNormales = ecosistema.getNacimientosPlantas() - ecosistema.getNacimientosPlantasVenenosas();
        int muertesPlantasNormales = ecosistema.getMuertesPlantas() - ecosistema.getMuertesPlantasVenenosas();

        String[] filaNacimientos = {
            "Total nacimientos",
            String.valueOf(nacPlantasNormales),
            String.valueOf(ecosistema.getNacimientosLobos()),
            String.valueOf(ecosistema.getNacimientosConejos()),
            String.valueOf(ecosistema.getNacimientosPlantasVenenosas())
        };
        String[] filaMuertes = {
            "Total muertes",
            String.valueOf(muertesPlantasNormales),
            String.valueOf(ecosistema.getMuertesLobos()),
            String.valueOf(ecosistema.getMuertesConejos()),
            String.valueOf(ecosistema.getMuertesPlantasVenenosas())
        };
        String[] filaLongevas = {
            "Más longeva",
            textoRecord(ecosistema.getRecordPlantaNombre(), ecosistema.getRecordPlantaEdad()),
            textoRecord(ecosistema.getRecordLoboNombre(), ecosistema.getRecordLoboEdad()),
            textoRecord(ecosistema.getRecordConejoNombre(), ecosistema.getRecordConejoEdad()),
            textoRecord(ecosistema.getRecordPlantaVenenosaNombre(), ecosistema.getRecordPlantaVenenosaEdad())
        };
        return new String[][]{filaNacimientos, filaMuertes, filaLongevas};
    }

    
    // Arma "Nombre (N turnos)", o "-" si todavía no hay récord para ese tipo.
    private String textoRecord(String nombre, int edad) {
        if (nombre == null || nombre.isEmpty()) {
            return "-";
        }
        return nombre + " (" + edad + " turnos)";
    }

    
    /** Reporte completo en texto, para el botón "Exportar / Copiar". */
    public String obtenerReporteFinal() {
        return ecosistema == null ? "" : ecosistema.generarReporteFinal();
    }

    //-----------------------------------------------------------------------//
    // Getters y Setters

    public int getTurnosTotales() {
        return turnosTotales;
    }
    
    public int getLobosAgregados() {
        return lobosAgregados;
    }
}
