package principal;

import dao.SimulacionDAO;
import controlador.Controlador;
import vista.VentanaEcosistema;

public class InterfazGraficaIE {

    public static void main(String[] args) {
        // Look and Feel del sistema operativo (si falla, queda el de Java por defecto)
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // se sigue con el Look and Feel por defecto
        }

        // Capa de persistencia: guarda y lee la partida en un .txt
        SimulacionDAO dao = new SimulacionDAO();

        // El Controlador recibe el DAO; el Ecosistema lo crea al iniciar o retomar.
        Controlador controlador = new Controlador(dao);

        // La ventana recibe el Controlador y se muestra en el hilo de Swing.
        java.awt.EventQueue.invokeLater(() -> new VentanaEcosistema(controlador).setVisible(true));
    }
}