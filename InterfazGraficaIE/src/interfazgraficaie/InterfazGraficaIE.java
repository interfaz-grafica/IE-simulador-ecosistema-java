package interfazgraficaie;

import java.util.Scanner;

public class InterfazGraficaIE {

    private static Scanner scanner;
    private static Ecosistema ecosistema;
    
    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        //ecosistema = new Ecosistema();  

        System.out.println("=========================================");
        System.out.println("   SIMULADOR DE ECOSISTEMA - INICIO      ");
        System.out.println("=========================================\n");
        
        ingresarDatosIniciales();
    }
    
   private static void ingresarDatosIniciales() {
        System.out.println("--- CONFIGURACIÓN INICIAL DEL ECOSISTEMA ---\n");    
        
        int cantidadInicialPlantas = ingresarEnteroEnRango("Cantidad inicial de plantas", 5, 30);
        int cantidadInicialConejos = ingresarEnteroEnRango("Cantidad inicial de conejos", 2, 15);
        int cantidadInicialLobos = ingresarEnteroEnRango("Cantidad inicial de lobos", 1, 5);
        Clima climaInicial = ingresarClima();
        int turnosTotales = ingresarEnteroEnRango("Cantidad de turnos totales", 10, 50);
        
        mostrarConfiguracionInicial(cantidadInicialPlantas, cantidadInicialConejos, cantidadInicialLobos, climaInicial, turnosTotales);
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
                System.out.println("(!) Error: Debes ingresar un número entero."); //Si parseInt explota (porque el usuario tipeó "hola" o letras)
            }
        }
        
        return numero;        
    }
    
    
    private static Clima ingresarClima() {

        System.out.println("\nSelecciona el clima inicial:");
        System.out.println("1. Soleado");
        System.out.println("2. Lluvioso");
        System.out.println("3. Sequía");
        System.out.println("4. Invierno");

        int opcion = ingresarEnteroEnRango("Opción de clima", 1, 4);

        // Se retorna la constante exacta del Enum dependiendo del número ingresado[cite: 1].
        switch (opcion) {
            case 1: return Clima.SOLEADO;
            case 2: return Clima.LLUVIOSO;
            case 3: return Clima.SEQUIA;
            case 4: return Clima.INVIERNO;
            default: return Clima.SOLEADO;
        }
    }
    
    private static void mostrarConfiguracionInicial(int cantidadInicialPlantas, int cantidadInicialConejos, int cantidadInicialLobos, Clima climaInicial, int turnosTotales) {
        System.out.println("\n-------------------------------------------");
        System.out.println("DATOS INGRESADOS:");
        System.out.println("• Plantas iniciales: " + cantidadInicialPlantas);
        System.out.println("• Conejos iniciales: " + cantidadInicialConejos);
        System.out.println("• Lobos iniciales:   " + cantidadInicialLobos);
        System.out.println("• Clima inicial:     " + climaInicial);
        System.out.println("• Duración total:    " + turnosTotales + " turnos");
        System.out.println("-------------------------------------------");           
    }
}
