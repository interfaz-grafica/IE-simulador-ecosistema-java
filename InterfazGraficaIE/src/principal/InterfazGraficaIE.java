package principal;

import dao.SimulacionDAO;
import controlador.Controlador;
import vista.VentanaEcosistema;

public class InterfazGraficaIE {

    public static void main(String[] args) {
        
        // Capa de persistencia: guarda y lee la partida en un .txt
        SimulacionDAO dao = new SimulacionDAO();

        // Inicializa el controlador principal inyectándole el DAO.
        // El modelo (Ecosistema) lo crea el Controlador cuando se inicia o retoma
        // una simulación, porque necesita la configuración inicial para construirse.
        Controlador controlador = new Controlador(dao);

    }
}