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
    
    public Ecosistema cargarEstado() {
        if (!existePartidaGuardada()) {
            return null; // no hay partida: el Controlador decide qué avisar
        }

        try (BufferedReader lector = new BufferedReader(new FileReader(rutaArchivo, StandardCharsets.UTF_8))) {
            String cabecera = lector.readLine();
            if (cabecera == null || !cabecera.startsWith("# SIMULACION-ECOSISTEMA")) {
                return null; // el archivo no es de nuestra aplicación
            }

            int turnoActual = 0;
            Clima clima = null;
            ArrayList<Planta> plantas = new ArrayList<>();
            ArrayList<Conejo> conejos = new ArrayList<>();
            ArrayList<Lobo> lobos = new ArrayList<>();
            ArrayList<String> eventos = new ArrayList<>();

            String seccion = "";
            String linea;
            while ((linea = lector.readLine()) != null) { // readLine() devuelve null al final
                if (linea.trim().isEmpty()) {
                    continue;
                }
                if (linea.startsWith("[")) {
                    seccion = linea; // empieza una sección nueva
                    continue;
                }

                if (seccion.equals("[CONFIG]")) {
                    String[] partes = linea.split("=", 2);
                    if (partes[0].equals("turnoActual")) {
                        turnoActual = Integer.parseInt(partes[1].trim());
                    } else if (partes[0].equals("clima")) {
                        clima = Clima.valueOf(partes[1].trim());
                    }
                } else if (seccion.equals("[ENTIDADES]")) {
                    cargarEntidad(linea, plantas, conejos, lobos);
                } else if (seccion.equals("[EVENTOS]")) {
                    eventos.add(linea);
                }
            }

            if (clima == null || turnoActual < 1) {
                return null; // faltan datos obligatorios: el archivo está incompleto
            }

            // Ecosistema vacío (0, 0, 0): no genera entidades al azar.
            Ecosistema eco = new Ecosistema(0, 0, 0, clima);
            eco.setTurnoActual(turnoActual);
            eco.setPlantas(plantas);
            eco.setConejos(conejos);
            eco.setLobos(lobos);
            for (String evento : eventos) {
                eco.registrarEvento(evento);
            }
            return eco;
        } catch (IOException | IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            // IllegalArgumentException incluye NumberFormatException y un clima inválido.
            return null; // archivo ilegible o con datos inválidos: no se rompe el programa
        }
    }

// Convierte una línea de texto en la entidad que corresponde y la suma a su lista.
    private void cargarEntidad(String linea, ArrayList<Planta> plantas, ArrayList<Conejo> conejos,
            ArrayList<Lobo> lobos) {
        String[] c = linea.split(";");
        String tipo = c[0];
        String nombre = c[1];
        double energia = Double.parseDouble(c[2]); // puede lanzar NumberFormatException
        int edad = Integer.parseInt(c[3]);
        boolean viva = Boolean.parseBoolean(c[4]);

        switch (tipo) {
            case "PLANTA":
                plantas.add(new Planta(nombre, energia, edad, viva, Integer.parseInt(c[5])));
                break;
            case "PLANTA_VENENOSA":
                plantas.add(new PlantaVenenosa(nombre, energia, edad, viva, Integer.parseInt(c[5])));
                break;
            case "CONEJO":
                conejos.add(new Conejo(nombre, energia, edad, viva, Integer.parseInt(c[5]),
                        Double.parseDouble(c[6])));
                break;
            case "LOBO":
                lobos.add(new Lobo(nombre, energia, edad, viva, Integer.parseInt(c[5]),
                        Double.parseDouble(c[6]), Integer.parseInt(c[7])));
                break;
            default:
                throw new IllegalArgumentException("Tipo de entidad desconocido: " + tipo);
        }
    }
}
