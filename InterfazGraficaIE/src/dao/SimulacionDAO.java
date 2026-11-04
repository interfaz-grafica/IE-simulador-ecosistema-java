package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import modelo.Clima;
import modelo.Conejo;
import modelo.Ecosistema;
import modelo.Lobo;
import modelo.Planta;
import modelo.PlantaVenenosa;

/**
 * Guarda y recupera el estado de la simulación en un archivo de texto plano (.txt).
 * No usa Swing ni consola: solo informa el resultado y el Controlador decide qué hacer.
 *
 * Estructura del archivo (una sección por bloque, "clave=valor" por línea):
 *   # SIMULACION-ECOSISTEMA v1
 *   [CONFIG]     turnoActual, turnosMaximos, clima, lobosAgregados
 *   [METRICAS]   nacimientos y muertes por tipo (incluye plantas venenosas),
 *                turnoMayorActividad, maxBajasEnUnTurno
 *   [HISTORIAL]  población de cada especie al final de cada turno (valores separados por coma)
 *   [RECORDS]    récords de longevidad por tipo y del lobo cazador, como "nombre;valor"
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

    public boolean guardarEstado(Ecosistema eco) {
        if (eco == null) {
            return false;
        }

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo, StandardCharsets.UTF_8))) {
            escribirLinea(escritor, "# SIMULACION-ECOSISTEMA v1");

            escribirLinea(escritor, "[CONFIG]");
            escribirLinea(escritor, "turnoActual=" + eco.getTurnoActual());
            escribirLinea(escritor, "turnosMaximos=" + eco.getTurnosMaximos());
            escribirLinea(escritor, "clima=" + eco.getClimaActual().name());
            escribirLinea(escritor, "lobosAgregados=" + eco.getLobosAgregados());

            escribirLinea(escritor, "[METRICAS]");
            escribirLinea(escritor, "nacimientosPlantas=" + eco.getNacimientosPlantas());
            escribirLinea(escritor, "nacimientosConejos=" + eco.getNacimientosConejos());
            escribirLinea(escritor, "nacimientosLobos=" + eco.getNacimientosLobos());
            escribirLinea(escritor, "nacimientosPlantasVenenosas=" + eco.getNacimientosPlantasVenenosas());
            escribirLinea(escritor, "muertesPlantas=" + eco.getMuertesPlantas());
            escribirLinea(escritor, "muertesConejos=" + eco.getMuertesConejos());
            escribirLinea(escritor, "muertesLobos=" + eco.getMuertesLobos());
            escribirLinea(escritor, "muertesPlantasVenenosas=" + eco.getMuertesPlantasVenenosas());
            escribirLinea(escritor, "turnoMayorActividad=" + eco.getTurnoMayorActividad());
            escribirLinea(escritor, "maxBajasEnUnTurno=" + eco.getMaxBajasEnUnTurno());

            escribirLinea(escritor, "[HISTORIAL]");
            escribirLinea(escritor, "historialPlantas=" + unirEnteros(eco.getHistorialPlantas()));
            escribirLinea(escritor, "historialConejos=" + unirEnteros(eco.getHistorialConejos()));
            escribirLinea(escritor, "historialLobos=" + unirEnteros(eco.getHistorialLobos()));

            // Cada récord se guarda como "nombre;valor". El nombre puede estar vacío (aún no hay récord).
            escribirLinea(escritor, "[RECORDS]");
            escribirLinea(escritor, "recordPlanta=" + eco.getRecordPlantaNombre() + ";" + eco.getRecordPlantaEdad());
            escribirLinea(escritor, "recordPlantaVenenosa=" + eco.getRecordPlantaVenenosaNombre() + ";"
                    + eco.getRecordPlantaVenenosaEdad());
            escribirLinea(escritor, "recordConejo=" + eco.getRecordConejoNombre() + ";" + eco.getRecordConejoEdad());
            escribirLinea(escritor, "recordLobo=" + eco.getRecordLoboNombre() + ";" + eco.getRecordLoboEdad());
            escribirLinea(escritor, "recordCazador=" + eco.getRecordCazadorNombre() + ";"
                    + eco.getRecordCazadorCacerias());

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

    public Ecosistema cargarEstado() {
        if (!existePartidaGuardada()) {
            return null; // no hay partida: el Controlador decide qué avisar
        }

        try (BufferedReader lector = new BufferedReader(new FileReader(rutaArchivo, StandardCharsets.UTF_8))) {
            String cabecera = lector.readLine();
            if (cabecera == null || !cabecera.startsWith("# SIMULACION-ECOSISTEMA")) {
                return null; // el archivo no es de nuestra aplicación
            }

            // Datos sueltos "clave=valor" de [CONFIG], [METRICAS], [HISTORIAL] y [RECORDS].
            HashMap<String, String> datos = new HashMap<>();
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

                if (seccion.equals("[ENTIDADES]")) {
                    cargarEntidad(linea, plantas, conejos, lobos);
                } else if (seccion.equals("[EVENTOS]")) {
                    eventos.add(linea);
                } else {
                    // Las demás secciones son "clave=valor": se guardan para usarlas después.
                    String[] partes = linea.split("=", 2);
                    datos.put(partes[0], partes[1]);
                }
            }

            Clima clima = Clima.valueOf(texto(datos, "clima"));
            int turnoActual = entero(datos, "turnoActual");
            if (turnoActual < 1) {
                return null; // dato inválido: el archivo está dañado
            }

            // Constructor limpio de Ecosistema: no genera entidades al azar.
            Ecosistema eco = new Ecosistema(clima);
            eco.setTurnoActual(turnoActual);
            eco.setTurnosMaximos(entero(datos, "turnosMaximos"));
            eco.setLobosAgregados(entero(datos, "lobosAgregados"));

            eco.setNacimientosPlantas(entero(datos, "nacimientosPlantas"));
            eco.setNacimientosConejos(entero(datos, "nacimientosConejos"));
            eco.setNacimientosLobos(entero(datos, "nacimientosLobos"));
            eco.setNacimientosPlantasVenenosas(entero(datos, "nacimientosPlantasVenenosas"));
            eco.setMuertesPlantas(entero(datos, "muertesPlantas"));
            eco.setMuertesConejos(entero(datos, "muertesConejos"));
            eco.setMuertesLobos(entero(datos, "muertesLobos"));
            eco.setMuertesPlantasVenenosas(entero(datos, "muertesPlantasVenenosas"));
            eco.setTurnoMayorActividad(entero(datos, "turnoMayorActividad"));
            eco.setMaxBajasEnUnTurno(entero(datos, "maxBajasEnUnTurno"));

            eco.setHistorialPlantas(listaEnteros(datos, "historialPlantas"));
            eco.setHistorialConejos(listaEnteros(datos, "historialConejos"));
            eco.setHistorialLobos(listaEnteros(datos, "historialLobos"));

            String[] recordPlanta = registro(datos, "recordPlanta");
            eco.setRecordPlantaNombre(recordPlanta[0]);
            eco.setRecordPlantaEdad(Integer.parseInt(recordPlanta[1]));
            String[] recordVenenosa = registro(datos, "recordPlantaVenenosa");
            eco.setRecordPlantaVenenosaNombre(recordVenenosa[0]);
            eco.setRecordPlantaVenenosaEdad(Integer.parseInt(recordVenenosa[1]));
            String[] recordConejo = registro(datos, "recordConejo");
            eco.setRecordConejoNombre(recordConejo[0]);
            eco.setRecordConejoEdad(Integer.parseInt(recordConejo[1]));
            String[] recordLobo = registro(datos, "recordLobo");
            eco.setRecordLoboNombre(recordLobo[0]);
            eco.setRecordLoboEdad(Integer.parseInt(recordLobo[1]));
            String[] recordCazador = registro(datos, "recordCazador");
            eco.setRecordCazadorNombre(recordCazador[0]);
            eco.setRecordCazadorCacerias(Integer.parseInt(recordCazador[1]));

            eco.setPlantas(plantas);
            eco.setConejos(conejos);
            eco.setLobos(lobos);
            for (String evento : eventos) {
                eco.registrarEvento(evento);
            }
            return eco;
        } catch (IOException | IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            // IllegalArgumentException incluye NumberFormatException, un clima inválido y un dato faltante.
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

    // Devuelve el valor de una clave; si falta, el archivo está incompleto.
    private String texto(HashMap<String, String> datos, String clave) {
        String valor = datos.get(clave);
        if (valor == null) {
            throw new IllegalArgumentException("Falta el dato: " + clave);
        }
        return valor;
    }

    private int entero(HashMap<String, String> datos, String clave) {
        return Integer.parseInt(texto(datos, clave).trim());
    }

    // "10,12,15" -> [10, 12, 15]. Un valor vacío es una lista vacía.
    private ArrayList<Integer> listaEnteros(HashMap<String, String> datos, String clave) {
        ArrayList<Integer> lista = new ArrayList<>();
        String valor = texto(datos, clave).trim();
        if (valor.isEmpty()) {
            return lista;
        }
        for (String parte : valor.split(",")) {
            lista.add(Integer.parseInt(parte.trim()));
        }
        return lista;
    }

    // "nombre;valor" -> ["nombre", "valor"]. El -1 conserva los campos vacíos del final.
    private String[] registro(HashMap<String, String> datos, String clave) {
        String[] partes = texto(datos, clave).split(";", -1);
        if (partes.length != 2) {
            throw new IllegalArgumentException("Registro inválido: " + clave);
        }
        return partes;
    }

    private String unirEnteros(ArrayList<Integer> valores) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < valores.size(); i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(valores.get(i));
        }
        return sb.toString();
    }

    // Escribe un texto y salta de línea (evita repetir write + newLine en cada renglón).
    private void escribirLinea(BufferedWriter escritor, String texto) throws IOException {
        escritor.write(texto);
        escritor.newLine();
    }
}