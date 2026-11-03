package dao;
import java.io.File;
/**
 * Guarda y recupera el estado de la simulación en un archivo de texto plano (.txt).
 * No usa Swing ni consola: solo informa el resultado y el Controlador decide qué hacer.
 *
 * Estructura del archivo (una sección por bloque, "clave=valor" por línea):
 *   # SIMULACION-ECOSISTEMA v1
 *   [CONFIG]     turnoActual, turnosMaximos, clima
 *   [METRICAS]   nacimientos y muertes por tipo, turno de mayor actividad, etc.
 *   [HISTORIAL]  población de cada especie al final de cada turno
 *   [ENTIDADES]  una línea por entidad viva: TIPO;nombre;energia;edad;viva;datos propios
 *   [EVENTOS]    eventos del último turno, una línea cada uno
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
}
