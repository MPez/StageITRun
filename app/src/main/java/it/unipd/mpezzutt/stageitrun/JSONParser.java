package it.unipd.mpezzutt.stageitrun;

import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by marco on 04/04/16.
 */
public class JSONParser {

    public List readJSON(JSONArray jsonArray, String tipo) throws JSONException {
        List list = new ArrayList();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            switch (tipo) {
                case "stage":
                    list.add(Stage.toStage(obj));
                    break;
                case "trofeo":
                    list.add(Trofeo.toTrofeo(obj));
                    break;
                case "utente":
                    list.add(Utente.toUtente(obj));
                    break;
            }
        }

        return list;
    }

    public static List<String> toList(JSONArray jsonArray) throws JSONException {
        List<String> list = new ArrayList<>();

        if (jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.getString(i));
            }
        }

        return list;
    }

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
                case "_id":
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
        String id = null;
        String nome = null;
        String descrizione = null;
        String descrLunga = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "_id":
                    id = reader.nextString();
                    break;
                case "nome":
                    nome = reader.nextString();
                    break;
                case "descrizione":
                    descrizione = reader.nextString();
                    break;
                case "descrizione_lunga":
                    descrLunga = reader.nextString();
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return new Trofeo(id, nome, descrizione, descrLunga);
    }

    protected Utente readUtente(JsonReader reader) throws IOException {
        String id = null;
        String nome = null;
        String cognome = null;
        String email = null;
        String password = null;
        List<String> stages = null;
        List<String> trofei = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "_id":
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
                case "stage_id":
                    stages = readStringArray(reader);
                    break;
                case "trofei_id":
                    trofei = readStringArray(reader);
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return new Utente(id, nome, cognome, email, password, stages, trofei);
    }

    protected List<String> readStringArray(JsonReader reader) throws IOException {
        List<String> list = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            list.add(reader.nextString());
        }
        reader.endArray();

        return list;
    }
}







