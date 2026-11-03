package dao;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import modelo.Conejo;
import modelo.Ecosistema;
import modelo.Lobo;
import modelo.Planta;
import modelo.PlantaVenenosa;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import modelo.Clima;
import java.io.File;

/**
 * Guarda y recupera el estado de la simulación en un archivo de texto plano
 * (.txt). No usa Swing ni consola: solo informa el resultado y el Controlador
 * decide qué hacer.
 *
 * Estructura del archivo (una sección por bloque, "clave=valor" por línea): #
 * SIMULACION-ECOSISTEMA v1 [CONFIG] turnoActual, turnosMaximos, clima
 * [METRICAS] nacimientos y muertes por tipo, turno de mayor actividad, etc.
 * [HISTORIAL] población de cada especie al final de cada turno [ENTIDADES] una
 * línea por entidad viva: TIPO;nombre;energia;edad;viva;datos propios [EVENTOS]
 * eventos del último turno, una línea cada uno
 */
public class SimulacionDAO {

    // Ruta relativa: el archivo queda en la carpeta del proyecto, sin rutas absolutas.
    private String rutaArchivo = "partida_ecosistema.txt";

    public boolean existePartidaGuardada() {
        File archivo = new File(rutaArchivo);
        return archivo.exists() && archivo.isFile();
    }

    public boolean borrarPartidaGuardada() {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            return true; // no hay nada que borrar: ya se cumple lo que se pedía
        }
        return archivo.delete();
    }
    
    public boolean guardarEstado(Ecosistema eco) {
        if (eco == null) {
            return false;
        }

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo, StandardCharsets.UTF_8))) {
            escribirLinea(escritor, "# SIMULACION-ECOSISTEMA v1");

            escribirLinea(escritor, "[CONFIG]");
            escribirLinea(escritor, "turnoActual=" + eco.getTurnoActual());
            escribirLinea(escritor, "clima=" + eco.getClimaActual().name());

            escribirLinea(escritor, "[ENTIDADES]");
            for (Planta p : eco.getPlantas()) {
                if (p.isViva()) {
                    // PlantaVenenosa hereda de Planta: el tipo permite distinguirlas al cargar.
                    String tipo = (p instanceof PlantaVenenosa) ? "PLANTA_VENENOSA" : "PLANTA";
                    escribirLinea(escritor, tipo + ";" + p.getNombre() + ";" + p.getEnergia() + ";"
                            + p.getEdad() + ";" + p.isViva() + ";" + p.getTamanio());
                }
            }
            for (Conejo c : eco.getConejos()) {
                if (c.isViva()) {
                    escribirLinea(escritor, "CONEJO;" + c.getNombre() + ";" + c.getEnergia() + ";"
                            + c.getEdad() + ";" + c.isViva() + ";" + c.getVelocidad() + ";" + c.getPeso());
                }
            }
            for (Lobo l : eco.getLobos()) {
                if (l.isViva()) {
                    escribirLinea(escritor, "LOBO;" + l.getNombre() + ";" + l.getEnergia() + ";"
                            + l.getEdad() + ";" + l.isViva() + ";" + l.getVelocidad() + ";" + l.getPeso()
                            + ";" + l.getExitosCaza());
                }
            }

            escribirLinea(escritor, "[EVENTOS]");
            for (String evento : eco.getEventosTurno()) {
                escribirLinea(escritor, evento);
            }

            return true;
        } catch (IOException e) {
            // El DAO no muestra mensajes: avisa con false y el Controlador decide qué hacer.
            return false;
        }
    }

    // Escribe un texto y salta de línea (evita repetir write + newLine en cada renglón).
    private void escribirLinea(BufferedWriter escritor, String texto) throws IOException {
        escritor.write(texto);
        escritor.newLine();
    }
}
