package principal;

import controlador.Controlador;
import vista.VentanaEcosistema;

public class InterfazGraficaIE {

    public static void main(String[] args) {
        // Inicializa el controlador principal de la aplicación
        Controlador controlador = new Controlador();

        // La vista recibe el controlador y se muestra en el hilo de eventos de Swing
        java.awt.EventQueue.invokeLater(() -> new VentanaEcosistema(controlador).setVisible(true));
    }
}