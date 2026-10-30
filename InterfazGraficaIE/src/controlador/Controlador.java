package controlador;

import modelo.Clima;
import modelo.Ecosistema;

public class Controlador {

    // Atributos que antes eran estáticos en InterfazGraficaIE
    private Ecosistema ecosistema;
    
    private int cantidadInicialPlantas;
    private int cantidadInicialConejos;
    private int cantidadInicialLobos;
    private Clima climaInicial;
    private int turnosTotales;
    
    private int lobosAgregados = 0;

    public Controlador() {
    }

    // Recibe y valida la configuración inicial (Reemplaza a ingresarDatosIniciales)
    public String configurarSimulacion(String plantasStr, String conejosStr, String lobosStr, int opcionClima, String turnosStr) {
        int plantas = validarEnteroEnRango(plantasStr, 5, 30);
        if (plantas == -1) {
            return "(!) Error: Cantidad inicial de plantas debe ser un entero entre 5 y 30.";
        }

        int conejos = validarEnteroEnRango(conejosStr, 2, 15);
        if (conejos == -1) {
            return "(!) Error: Cantidad inicial de conejos debe ser un entero entre 2 y 15.";
        }

        int lobos = validarEnteroEnRango(lobosStr, 1, 5);
        if (lobos == -1) {
            return "(!) Error: Cantidad inicial de lobos debe ser un entero entre 1 y 5.";
        }

        int turnos = validarEnteroEnRango(turnosStr, 10, 50);
        if (turnos == -1) {
            return "(!) Error: Cantidad de turnos totales debe ser un entero entre 10 y 50.";
        }

        this.cantidadInicialPlantas = plantas;
        this.cantidadInicialConejos = conejos;
        this.cantidadInicialLobos = lobos;
        this.climaInicial = obtenerClimaPorOpcion(opcionClima);
        this.turnosTotales = turnos;
        this.lobosAgregados = 0;

        this.ecosistema = new Ecosistema(cantidadInicialPlantas, cantidadInicialConejos, cantidadInicialLobos, climaInicial);
        return null; // null indica que no hubo errores de validación
    }

    // Mantiene la lógica de ingresarEnteroEnRango sin bloquear con Scanner
    public int validarEnteroEnRango(String inputUsuario, int min, int max) {
        if (inputUsuario == null) {
            return -1;
        }
        try {
            int numero = Integer.parseInt(inputUsuario.trim()); // Sanitización y conversión en número
            if (numero >= min && numero <= max) {
                return numero;
            } else {
                return -1;
            }
        } catch (NumberFormatException e) {
            return -1; // Manejo de excepción por input no numérico
        }
    }

    // Mantiene el switch exacto de ingresarClima()
    public Clima obtenerClimaPorOpcion(int opcion) {
        // Se retorna la constante exacta del Enum dependiendo del número ingresado.
        switch (opcion) {
            case 1: return Clima.SOLEADO;
            case 2: return Clima.LLUVIOSO;
            case 3: return Clima.SEQUIA;
            case 4: return Clima.INVIERNO;
            default: return Clima.SOLEADO;
        }
    }

    // Mantiene el resumen de mostrarConfiguracionInicial() devolviéndolo como String
    public String obtenerResumenConfiguracionInicial() {
        return "-------------------------------------------\n"
             + "DATOS INGRESADOS:\n"
             + "Plantas iniciales: " + cantidadInicialPlantas + "\n"
             + "Conejos iniciales: " + cantidadInicialConejos + "\n"
             + "Lobos iniciales:   " + cantidadInicialLobos + "\n"
             + "Clima inicial:     " + climaInicial + "\n"
             + "Duracion total:    " + turnosTotales + " turnos\n"
             + "-------------------------------------------";
    }

    // Avanza un turno de la simulación (reemplaza la iteración del bucle for de consola)
    public void avanzarTurno() {
        if (ecosistema != null && !simulacionTerminada()) {
            ecosistema.procesarTurno(); // Llama a la lógica del ecosistema.
        }
    }

    // Evalúa si corresponde intervenir cada 3 turnos (turnoActual - 1 es el turno recién procesado)
    public boolean esTurnoDeIntervencion() {
        if (ecosistema == null) return false;
        int turnoRecienProcesado = ecosistema.getTurnoActual() - 1;
        return turnoRecienProcesado > 0 && (turnoRecienProcesado % 3 == 0) && !simulacionTerminada();
    }

    // Evalúa las condiciones de corte que tenía el bucle for en ejecutarBuclePrincipal()
    public boolean simulacionTerminada() {
        if (ecosistema == null) return true;
        return (ecosistema.getTurnoActual() > turnosTotales) || ecosistema.ecosistemaColapsado();
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
