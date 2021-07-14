package es.lanyu.json;

import es.lanyu.commons.identificable.GestorIdentificables;
import es.lanyu.commons.identificable.Identificable;
import es.lanyu.commons.identificable.Nombrable;
import es.lanyu.commons.io.Deserializador;
import es.lanyu.commons.servicios.entidad.CargadorIdentificables;

public class CargadorIdentificablesJson implements CargadorIdentificables {

    Deserializador jsonDeserializer = new JsonDeserializer();

    @Override
    public Deserializador getDeserializadorDefecto() {
        return jsonDeserializer;
    }

    @Override
    public <K extends Comparable<K>, T extends Identificable<K> & Nombrable, S extends T> void cargarNombrables(
            Deserializador deserializador, String rutaArchivo, Class<T> claseMapa, Class<S> claseEspecializacion,
            GestorIdentificables gestor) {
        Utils.cargarIdentificables(deserializador, rutaArchivo, claseMapa, claseEspecializacion, gestor);
    }

}
