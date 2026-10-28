package principal;

import modelo.Clima;
import modelo.Ecosistema;
import java.util.Scanner;

public class InterfazGraficaIE {

    private static Scanner scanner;
    private static Ecosistema ecosistema;
    
    private static int cantidadInicialPlantas;
    private static int cantidadInicialConejos;
    private static int cantidadInicialLobos;
    private static Clima climaInicial;
    private static int turnosTotales;
    
    private static int lobosAgregados = 0;
    
    
    public static void main(String[] args) {
        scanner = new Scanner(System.in); 

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

        cantidadInicialPlantas = ingresarEnteroEnRango("Cantidad inicial de plantas", 5, 30);
        cantidadInicialConejos = ingresarEnteroEnRango("Cantidad inicial de conejos", 2, 15);
        cantidadInicialLobos = ingresarEnteroEnRango("Cantidad inicial de lobos", 1, 5);
        climaInicial = ingresarClima();
        turnosTotales = ingresarEnteroEnRango("Cantidad de turnos totales", 10, 50);

        }
    
    
    private static int ingresarEnteroEnRango(String mensaje, int min, int max) {
        
        int numero = -1;
        
        boolean esValido = false;

        while (esValido == false) {
                        
            String inputUsuario = scanner.nextLine().trim(); // Eliminar espacios finales

            try {
                
                numero = Integer.parseInt(inputUsuario); //Convertir el texto en número
                
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
                scanner.nextLine(); // Pausar la ejecución hasta que el usuario presione Enter. La única forma de liberar ese bloqueo es que el scanner detecte un salto de línea      
            }
        }
        
        //Evaluamos por qué se rompió el bucle anterior
        if (ecosistema.ecosistemaColapsado()) {
        } else {
        }
        ecosistema.generarReporteFinal();
    }
    
    
    private static void mostrarMenuIntervencion() {
        boolean finIntervencion = false;
        
        while(finIntervencion == false) {

            int opcion = ingresarEnteroEnRango("Opcion", 1, 3);

            if (opcion == 1) {
                // Selección del nuevo clima por el usuario
                Clima nuevoClima = ingresarClima();
                
                boolean confirmacion = pedirConfirmacion("\nConfirmas el cambio de clima?");
                if (confirmacion) {
                    // Invocación a la lógica central del ecosistema
                    ecosistema.cambiarClima(nuevoClima);
                    finIntervencion = true; 
                } else {
                }

            } else if (opcion == 2) {
                boolean entidadAgregada = mostrarMenuAgregarEntidad();
                if (entidadAgregada) {
                    finIntervencion = true;
                }

            } else if (opcion == 3) {
                boolean confirmacion = pedirConfirmacion("\nConfirmas que deseas avanzar sin intervenir?");
                if (confirmacion) {
                    finIntervencion = true; 
                } else {
                }
            }
        }
    }
    
    
    private static boolean mostrarMenuAgregarEntidad() {

        int opcionEntidad = ingresarEnteroEnRango("Que entidad agregar? (1=planta/2=conejo/3=lobo)", 1, 3);

        String nombreEntidad = "";
        if (opcionEntidad == 1) {
            nombreEntidad = "Planta";
        } else if (opcionEntidad == 2) {
            nombreEntidad = "Conejo";
        } else if (opcionEntidad == 3) {
            if(lobosAgregados >= 5) {
                return false; // Al retornar false, lo obliga a volver al menú de intervención a elegir otra cosa            
            }
            nombreEntidad = "Lobo";
        }

        boolean confirmacion = pedirConfirmacion("\nConfirmas que deseas inyectar un/a " + nombreEntidad + " al ecosistema?");
        
        if (confirmacion) {
            ecosistema.agregarEntidad(nombreEntidad);
               
            if (opcionEntidad == 3) {
                lobosAgregados++;
            }    
            return true;
        } else {
            return false;
        }
    }  
    
    private static boolean pedirConfirmacion(String mensaje) {
        int numero = -1;
        boolean opcionValida = false;

        while (opcionValida == false) {
            String inputUsuario = scanner.nextLine().trim(); //trim() recorta los espacios en blanco accidentales que el usuario haya puesto en los bordes.

            try {
                numero = Integer.parseInt(inputUsuario);
                
                if (numero == 1 || numero == 2) {
                    opcionValida = true;
                } else {
                }
            } catch (NumberFormatException e) {
            }
        }
        
        return numero == 1;
    }
    

}
