package es.lanyu.json;

import java.io.File;

import com.esotericsoftware.jsonbeans.Json;
import com.esotericsoftware.jsonbeans.OutputType;

import es.lanyu.commons.io.DeserializadorArchivo;
import es.lanyu.commons.io.IoUtils;

public class JsonDeserializer extends Json implements DeserializadorArchivo {

    public JsonDeserializer() {
        super(OutputType.json);
        setIgnoreUnknownFields(true);
    }
    
    @Override
    public <T> T deserializarJson(Class<T> tipo, String json) {
        return fromJson(tipo, json);
    }

    @Override
    public <T> T deserializarArchivoJson(Class<T> tipo, File json) {
        return deserializarJson(tipo, IoUtils.leerArchivoComoString(json.getAbsolutePath()));
    }

}
