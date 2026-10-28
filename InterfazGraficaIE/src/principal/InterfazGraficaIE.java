package principal;

import modelo.Clima;
import modelo.Ecosistema;

public class InterfazGraficaIE {

    private static Ecosistema ecosistema;
    
    private static int cantidadInicialPlantas;
    private static int cantidadInicialConejos;
    private static int cantidadInicialLobos;
    private static Clima climaInicial;
    private static int turnosTotales;
    
    private static int lobosAgregados = 0;
    
    
    public static void main(String[] args) {
        boolean configuracionConfirmada = false;

        // El main toma el control del flujo del programa
        while (configuracionConfirmada == false) {
            
            ingresarDatosIniciales();
            
            mostrarConfiguracionInicial();
            
            boolean confirmacion = pedirConfirmacion("\nDeseas confirmar esta configuracion?"); // Solicitud de confirmación de parámetros al usuario

            if (confirmacion) { 
                
                configuracionConfirmada = true;
                
                ecosistema = new Ecosistema(cantidadInicialPlantas, cantidadInicialConejos, cantidadInicialLobos, climaInicial);
                
                // 
                ejecutarBuclePrincipal();
                
            } else {
            }
        }
    }
    
    
    private static void ingresarDatosIniciales() {

    }
    
    
    private static int ingresarEnteroEnRango(String mensaje, int min, int max) {
        
        int numero = -1;
        
        boolean esValido = false;

        while (esValido == false) {
                        
            try {
                                
                if (numero >= min && numero <= max) {
                    esValido = true;
                } else {
                }
                
            } catch (NumberFormatException e) { 
            }
        }
        
        return numero;        
    }
    
    
    private static Clima ingresarClima() {

        int opcion = ingresarEnteroEnRango("Opcion de clima", 1, 4);

        // Se retorna la constante exacta del Enum dependiendo del número ingresado.
        switch (opcion) {
            case 1: return Clima.SOLEADO;
            case 2: return Clima.LLUVIOSO;
            case 3: return Clima.SEQUIA;
            case 4: return Clima.INVIERNO;
            default: return Clima.SOLEADO;
        }
    }
    
    
    private static void mostrarConfiguracionInicial() {
       
    }
    
    
    private static void ejecutarBuclePrincipal() {
         
        
        for (int turnoActual = 1; turnoActual <= turnosTotales && !ecosistema.ecosistemaColapsado(); turnoActual++) {

            ecosistema.procesarTurno(); // Llama a la lógica del ecosistema.
           
            
            if (turnoActual % 3 == 0) {
                mostrarMenuIntervencion();
            } else {
            }
        }
        
        //Evaluamos por qué se rompió el bucle anterior
        if (ecosistema.ecosistemaColapsado()) {
        } else {
        }
        ecosistema.generarReporteFinal();
    }
    
    
    private static void mostrarMenuIntervencion() {
 
    }
    
    
    private static boolean mostrarMenuAgregarEntidad() {

    }  
    
    private static boolean pedirConfirmacion(String mensaje) {

    }
    

}
