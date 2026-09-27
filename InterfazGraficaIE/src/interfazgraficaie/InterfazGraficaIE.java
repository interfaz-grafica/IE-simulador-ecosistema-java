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

        System.out.println("=========================================");
        System.out.println("   SIMULADOR DE ECOSISTEMA - INICIO      ");
        System.out.println("=========================================\n");

        boolean configuracionConfirmada = false;

        // El main toma el control del flujo del programa
        while (configuracionConfirmada == false) {
            
            // 1. Solo recolecta datos
            ingresarDatosIniciales();
            
            // 2. Solo muestra el resumen en pantalla
            mostrarConfiguracionInicial();
            
            // 3. Evalúa la decisión
            int opcion = ingresarEnteroEnRango("\nDeseas confirmar esta configuracion? (1 = Si / 2 = Volver a ingresar)", 1, 2);

            if (opcion == 1) {
                
                configuracionConfirmada = true;
                System.out.println("\n>> Configuracion guardada exitosamente!\n");
                
                ecosistema = new Ecosistema(); 
                
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
            
            
            if (turnoActual % 3 == 0) {
                mostrarMenuIntervencion();
            } else {
                System.out.print(">> Presiona [ENTER] para avanzar al siguiente turno.");
                scanner.nextLine(); // Pausar la ejecución hasta que el usuario presione Enter. La única forma de liberar ese bloqueo es que el scanner detecte un salto de línea      
            }
            
            ecosistema.procesarTurno(); // Llama a la lógica del ecosistema.
            ecosistema.mostrarEstado(); // Renderiza los resultados del turno en pantalla.
        }
        
        System.out.println("\n>> Se alcanzo el limite de turnos, la simulacion ha finalizado.");
    }
    
    
    private static void mostrarMenuIntervencion() {
        System.out.println("\n--- INTERVENCION DEL JUGADOR ---");
        System.out.println("1. Cambiar el clima");
        System.out.println("2. Agregar entidad (Planta, Conejo o Lobo)");
        System.out.println("3. Avanzar turno sin intervenir");

        int opcion = ingresarEnteroEnRango("Elige una accion", 1, 3);

        if (opcion == 1) {
            System.out.println("\n[MOCK] Abriendo menu de clima...");
            
        } else if (opcion == 2) {
            mostrarMenuAgregarEntidad();
            
        } else if (opcion == 3) {
            System.out.println("\n>> Avanzando de turno sin intervenir...");
        }
    }
    
    
    private static void mostrarMenuAgregarEntidad() {
        System.out.println("\n--- AGREGAR ENTIDAD ---");
        System.out.println("1. Planta");
        System.out.println("2. Conejo");
        System.out.println("3. Lobo");

        int opcionEntidad = ingresarEnteroEnRango("Que entidad deseas agregar?", 1, 3);

        
        if (opcionEntidad == 1) {
            System.out.println("\n[MOCK] Preparando para agregar Planta...");
        } else if (opcionEntidad == 2) {
            System.out.println("\n[MOCK] Preparando para agregar Conejo...");
        } else if (opcionEntidad == 3) {
            System.out.println("\n[MOCK] Preparando para agregar Lobo...");
        }
    }
}
