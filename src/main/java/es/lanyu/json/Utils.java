package es.lanyu.json;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import com.esotericsoftware.jsonbeans.Json;
import com.esotericsoftware.jsonbeans.JsonSerializer;
import com.esotericsoftware.jsonbeans.JsonValue;

import es.lanyu.commons.identificable.AbstractNombrable;
import es.lanyu.commons.identificable.GestorIdentificables;
import es.lanyu.commons.identificable.Identificable;
import es.lanyu.commons.io.Deserializador;

/**
 * Clase de utilidades para la carga de {@link Identificables} desde archivos JSON.
 * 
 * @author <a href="https://github.com/Awes0meM4n">Awes0meM4n</a>
 * @version 1.0
 * @since 1.0
 */
class Utils {

    /**
     * Carga los recursos de tipo {@link Identificable} almacenados en cada fila del fichero
     * {@code rutaArchivo} en formato .json y los almacena en el {@code gestor} como un recurso
     * del tipo {@code claseMapa}. Puede especializarse el recurso a guardar con {@code claseEspecializacion}
     * si esta serializado un subtipo de {@code claseMapa}
     * 
     * @param <K>
     *            Tipo del identificador del {@code Identificable}
     * @param <T>
     *            Tipo de la {@code claseMapa}
     * @param <S>
     *            Tipo de la especializacion. Puede ser el igual a {@code T}
     * @param json
     *            Serializador de tipo {@link Json} que implementa la lectura y escritura en formato json
     * @param rutaArchivo
     *            ruta al archivo .json
     * @param claseMapa
     *            Clase que sirve para mapear el recurso (normalmente la mas generica)
     * @param claseEspecializacion
     *            Clase subtipo de claseMapa para gestionar recursos mas especializados
     * @param gestor
     *            Gestor que administrara los recursos a cargar
     */
    @SuppressWarnings("unchecked")
    static <K extends Comparable<K>, T extends Identificable<K>, S extends T> void cargarIdentificables(
            Deserializador json, String rutaArchivo,
            Class<T> claseMapa, Class<S> claseEspecializacion,
            GestorIdentificables gestor) {

        String linea = null;
        try (BufferedReader buffer = new BufferedReader(
                new InputStreamReader(new FileInputStream(rutaArchivo), "UTF-8"))) {
            while ((linea = buffer.readLine()) != null) {
                T objeto = null;
                try {
                    objeto = json.deserializarJson(claseEspecializacion, linea);
                } catch (Exception e) {
                    e.printStackTrace();
                    objeto = getSerializador((Class<AbstractNombrable>) claseMapa)
                            .fromJson(claseEspecializacion, linea);
                }
                if (objeto != null) {
                    gestor.addIdentificable(claseMapa, objeto);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Serializador por defecto para los {@link AbstractNombrable}
     * 
     * @param <T>
     *            Subtipo de {@code AbstractNombrable}
     * @param clase
     *            Subtipo de {@code AbstractNombrable}
     * @return {@link Json} para el tipo pasado
     */
    private static <T extends AbstractNombrable> Json getSerializador(Class<T> clase) {
        Json json = new Json();
        json.setSerializer(clase, new JsonSerializer<T>() {
            @SuppressWarnings("rawtypes")
            public void write(Json json, T nombrable, Class knownType) {
                json.writeObjectStart();
                json.writeValue("id", nombrable.getIdentificador());
                json.writeValue("nombre", nombrable.getNombre());
                json.writeObjectEnd();
            }

            @SuppressWarnings({ "rawtypes", "unchecked" })
            public T read(Json json, JsonValue jsonData, Class type) {
                T nombrable;
                try {
                    nombrable = (T) type.newInstance();
                    nombrable.setIdentificador(jsonData.getString("id"));
                    nombrable.setNombre(jsonData.getString("nombre"));
                    return nombrable;
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    return null;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        });

        return json;
    }

}
