package it.unipd.mpezzutt.stageitrun;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marco on 04/04/16.
 */
public class JSONParser {

    public List readJSON(InputStream asset) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(asset, "UTF-8"));

        try {
            return readArray(reader);
        } finally {
            reader.close();
        }
    }

    protected List readArray(JsonReader reader) throws IOException {
        List list = new ArrayList();

        reader.beginObject();
        String objName = reader.nextName();
        reader.beginArray();
        switch (objName) {
            case "stage":
                while (reader.hasNext()) {
                    list.add(readStage(reader));
                }
                break;
            case "trofei":
                while (reader.hasNext()) {
                    list.add(readTrofeo(reader));
                }
                break;
            case "utenti":
                while (reader.hasNext()) {
                    list.add(readUtente(reader));
                }
                break;
        }
        reader.endArray();
        return list;
    }

    protected Stage readStage(JsonReader reader) throws IOException {
        String id = null;
        String nome = null;
        String azienda = null;
        String descrizione = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "id":
                    id = reader.nextString();
                    break;
                case "nome":
                    nome = reader.nextString();
                    break;
                case "azienda":
                    azienda = reader.nextString();
                    break;
                case "descrizione":
                    descrizione = reader.nextString();
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return new Stage(id, nome, azienda, descrizione);
    }

    protected Trofeo readTrofeo(JsonReader reader) throws IOException {
        String nome = null;
        String descrizione = null;
        String stato = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "nome":
                    nome = reader.nextString();
                    break;
                case "descrizione":
                    descrizione = reader.nextString();
                    break;
                case "stato":
                    stato = reader.nextString();
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return new Trofeo(nome, descrizione, stato);
    }

    protected Utente readUtente(JsonReader reader) throws IOException {
        String id = null;
        String nome = null;
        String cognome = null;
        String email = null;
        String password = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "id":
                    id = reader.nextString();
                    break;
                case "nome":
                    nome = reader.nextString();
                    break;
                case "cognome":
                    cognome = reader.nextString();
                    break;
                case "email":
                    email = reader.nextString();
                    break;
                case "password":
                    password = reader.nextString();
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return new Utente(id, nome, cognome, email, password);
    }
}







