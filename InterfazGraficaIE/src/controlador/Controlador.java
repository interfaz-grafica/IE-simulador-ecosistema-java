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


public class Controlador {
    
    // Rangos válidos de la configuración inicial (consigna)
    public static int MIN_PLANTAS = 5;
    public static int MAX_PLANTAS = 30;
    public static int MIN_CONEJOS = 2;
    public static int MAX_CONEJOS = 15;
    public static int MIN_LOBOS = 1;
    public static int MAX_LOBOS = 5;
    public static int MIN_TURNOS = 10;
    public static int MAX_TURNOS = 50;
    
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

    public Controlador(SimulacionDAO dao) {
        this.dao = dao;
    }


    // Evalúa si corresponde intervenir cada 3 turnos (turnoActual - 1 es el turno recién procesado)
    public boolean esTurnoDeIntervencion() {
        if (ecosistema == null) return false;
        int turnoRecienProcesado = ecosistema.getTurnoActual() - 1;
        return turnoRecienProcesado > 0 && (turnoRecienProcesado % 3 == 0) && !simulacionTerminada();
    }


    // Devuelve el mensaje de fin de simulación tal como estaba en ejecutarBuclePrincipal()
    public String obtenerMensajeFinSimulacion() {
        if (ecosistema.ecosistemaColapsado()) {
            return ">> FIN DE LA SIMULACION: El ecosistema ha colapsado (una poblacion se extinguio).";
        } else {
            return ">> FIN DE LA SIMULACION: Se alcanzo el limite maximo de " + turnosTotales + " turnos.";
        }
    }

    // Intervención Opción 1: Cambiar clima
    public void intervenirCambiarClima(Clima nuevoClima) {
        if (ecosistema != null && nuevoClima != null) {
            ecosistema.cambiarClima(nuevoClima);
        }
    }

    // Intervención Opción 2: Agregar entidad (Mantiene la validación de lobosAgregados >= 5)
    public String intervenirAgregarEntidad(int opcionEntidad) {
        String nombreEntidad = "";
        if (opcionEntidad == 1) {
            nombreEntidad = "Planta";
        } else if (opcionEntidad == 2) {
            nombreEntidad = "Conejo";
        } else if (opcionEntidad == 3) {
            if (lobosAgregados >= 5) {
                return "(!) Limite maximo de lobos alcanzado. No puedes agregar mas lobos en esta simulacion.";
            }
            nombreEntidad = "Lobo";
        } else {
            return "(!) Opcion de entidad invalida.";
        }

        ecosistema.agregarEntidad(nombreEntidad);
        if (opcionEntidad == 3) {
            lobosAgregados++;
        }
        return "Se agrego '" + nombreEntidad + "' al ecosistema.";
    }

    // Obtiene el reporte final desde el modelo
    public String obtenerReporteFinal() {
        if (ecosistema == null) return "";
        return ecosistema.generarReporteFinal();
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
            String climaTexto, String turnosStr) {
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

        this.ecosistema = new Ecosistema(cantidadInicialPlantas, cantidadInicialConejos, cantidadInicialLobos, climaInicial);
        
        guardarProgreso();
        
        return null;
       
    }
    

    //Versión que recibe el índice del combo de clima (0 = Soleado ... 3 = Invierno).
    public String iniciarNuevaSimulacion(String plantasStr, String conejosStr, String lobosStr,
            int indiceClima, String turnosStr) {
        Clima[] climas = Clima.values();
        String nombre = (indiceClima >= 0 && indiceClima < climas.length) ? climas[indiceClima].name() : null;
        return iniciarNuevaSimulacion(plantasStr, conejosStr, lobosStr, nombre, turnosStr);
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
    public String retomarSimulacion() {
        if (!hayPartidaGuardada()) {
            return "(!) No hay ninguna simulación guardada para retomar.";
        }
        Ecosistema cargado = dao.cargarEstado();
        if (cargado == null) {
            return "(!) La simulación guardada está dañada o incompleta y no se pudo cargar.";
        }
        this.ecosistema = cargado;
        this.climaInicial = cargado.getClimaActual();
        this.cantidadInicialPlantas = cargado.getPlantas().size();
        this.cantidadInicialConejos = cargado.getConejos().size();
        this.cantidadInicialLobos = cargado.getLobos().size();
        // TODO(DAO): el archivo todavía no guarda los turnos totales. Hasta que lo haga,
        // se usa el máximo permitido para que la partida retomada pueda terminar.
        this.turnosTotales = MAX_TURNOS;
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
    

    //-----------------------------------------------------------------------//
    // Getters y Setters
    public Ecosistema getEcosistema() {
        return ecosistema;
    }

    public void setEcosistema(Ecosistema ecosistema) {
        this.ecosistema = ecosistema;
    }

    public int getTurnosTotales() {
        return turnosTotales;
    }

    public int getLobosAgregados() {
        return lobosAgregados;
    }
}
