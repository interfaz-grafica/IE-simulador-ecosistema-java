package interfazgraficaie;

import java.util.Scanner;

public class InterfazGraficaIE {

    private static Scanner scanner;
    private static Ecosistema ecosistema;
    
    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        ecosistema = new Ecosistema();
    }
    
}
