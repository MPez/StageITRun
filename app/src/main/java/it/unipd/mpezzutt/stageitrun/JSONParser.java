package it.unipd.mpezzutt.stageitrun;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marco on 04/04/16.
 */
public class JSONParser {

    public List readStageJSON (InputStream asset) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(asset, "UTF-8"));

        try {
            return readStageArray(reader);
        } finally {
            reader.close();
        }
    }

    protected List readStageArray(JsonReader reader) throws IOException {
        List stageList = new ArrayList();

        reader.beginArray();
        while (reader.hasNext()) {
            stageList.add(readStage(reader));
        }
        reader.endArray();
        return stageList;
    }

    protected Stage readStage(JsonReader reader) throws IOException {
        String id = null;
        String nome = null;
        String azienda = null;
        String descrizione = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                id = reader.nextString();
            } else if (name.equals("nome")) {
                nome = reader.nextString();
            } else if (name.equals("azienda")) {
                azienda = reader.nextString();
            } else if (name.equals("descrizione")) {
                descrizione = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Stage(id, nome, azienda, descrizione);
    }

    public List readUtentiJSON (InputStream asset) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(asset, "UTF-8"));

        try {
            return readUtentiArray(reader);
        } finally {
            reader.close();
        }
    }

    protected List readUtentiArray(JsonReader reader) throws IOException {
        List utentiList = new ArrayList();

        reader.beginArray();
        while (reader.hasNext()) {
            utentiList.add(readUtente(reader));
        }
        reader.endArray();
        return utentiList;
    }

    protected Utente readUtente(JsonReader reader) throws IOException {
        String id = null;
        String nome = null;
        String cognome = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                id = reader.nextString();
            } else if (name.equals("nome")) {
                nome = reader.nextString();
            } else if (name.equals("azienda")) {
                cognome = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Utente(id, nome, cognome);
    }
}







