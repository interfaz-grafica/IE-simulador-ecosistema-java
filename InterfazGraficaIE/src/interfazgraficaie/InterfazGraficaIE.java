package interfazgraficaie;

import java.util.Scanner;

public class InterfazGraficaIE {

    private static Scanner scanner;
    private static Ecosistema ecosistema;
    
    
    private static int cantidadInicialPlantas;
    private static int cantidadInicialConejos;
    private static int cantidadInicialLobos;
    private static Clima climaInicial;
    private static int turnosTotales;
    
    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        //ecosistema = new Ecosistema();  

        System.out.println("=========================================");
        System.out.println("   SIMULADOR DE ECOSISTEMA - INICIO      ");
        System.out.println("=========================================\n");

        boolean configuracionConfirmada = false;

        // El main toma el control del flujo del programa
        while (!configuracionConfirmada) {
            
            // 1. Solo recolecta datos
            ingresarDatosIniciales();
            
            // 2. Solo muestra el resumen en pantalla
            mostrarConfiguracionInicial();
            
            // 3. Evalúa la decisión
            int opcion = ingresarEnteroEnRango("\nDeseas confirmar esta configuracion? (1 = Si / 2 = Volver a ingresar)", 1, 2);

            if (opcion == 1) {
                configuracionConfirmada = true;
                System.out.println("\n>> Configuracion guardada exitosamente!\n");
                ejecutarBuclePrincipal();
                
                // Aquí instanciarás el ecosistema más adelante usando las variables de clase
                // ecosistema = new Ecosistema(cantidadPlantas, cantidadConejos, cantidadLobos, climaInicial, turnosTotales);
            } else {
                System.out.println("\n>> Descartando datos. Reiniciando el panel de configuracion...\n");
            }
        }
    }
    
    
    private static void ingresarDatosIniciales() {

        System.out.println("--- CONFIGURACION INICIAL DEL ECOSISTEMA ---\n");    

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
            
            System.out.print(mensaje + " [" + min + " - " + max + "]: ");
            
            String inputUsuario = scanner.nextLine().trim(); //trim() recorta los espacios en blanco accidentales que el usuario haya puesto en los bordes.

            try {
                
                numero = Integer.parseInt(inputUsuario); //convertir el texto en número
                
                if (numero >= min && numero <= max) {
                    esValido = true;
                } else {
                    System.out.println("(!) Error: El valor debe estar comprendido entre " + min + " y " + max + ".");
                }
                
            } catch (NumberFormatException e) {
                System.out.println("(!) Error: Debes ingresar un numero entero."); //Si parseInt explota (porque el usuario tipeó "hola" o letras)
            }
        }
        
        return numero;        
    }
    
    
    private static Clima ingresarClima() {

        System.out.println("\nSelecciona el clima inicial:");
        System.out.println("1. Soleado");
        System.out.println("2. Lluvioso");
        System.out.println("3. Sequia");
        System.out.println("4. Invierno");

        int opcion = ingresarEnteroEnRango("Opcion de clima", 1, 4);

        // Se retorna la constante exacta del Enum dependiendo del número ingresado[cite: 1].
        switch (opcion) {
            case 1: return Clima.SOLEADO;
            case 2: return Clima.LLUVIOSO;
            case 3: return Clima.SEQUIA;
            case 4: return Clima.INVIERNO;
            default: return Clima.SOLEADO;
        }
    }
    
    
    private static void mostrarConfiguracionInicial() {
        System.out.println("\n-------------------------------------------");
        System.out.println("DATOS INGRESADOS:");
        System.out.println("Plantas iniciales: " + cantidadInicialPlantas);
        System.out.println("Conejos iniciales: " + cantidadInicialConejos);
        System.out.println("Lobos iniciales:   " + cantidadInicialLobos);
        System.out.println("Clima inicial:     " + climaInicial);
        System.out.println("Duracion total:    " + turnosTotales + " turnos");
        System.out.println("-------------------------------------------");           
    }
    
    
    private static void ejecutarBuclePrincipal() {
        
        System.out.println("\n--- INICIANDO SIMULACION ---");  
        
        for (int turnoActual = 1; turnoActual <= turnosTotales; turnoActual++) {
            System.out.println("\n=========================================");
            System.out.println("               TURNO " + turnoActual);
            System.out.println("=========================================");            
        }
        
        System.out.println("\n>> Se alcanzó el límite de turnos, la simulación ha finalizado.");
    }
}
