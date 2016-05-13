package it.unipd.mpezzutt.stageitrun;

import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marco on 04/04/16.
 */
public class JSONParser {

    public List readJSON(JSONObject jsonObject, String tipo) throws JSONException {
        List list = new ArrayList();

        JSONArray jsonArray = jsonObject.getJSONArray(tipo);

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
                if (jsonArray.getJSONObject(i) != null) {
                    list.add(jsonArray.getJSONObject(i).getString("stage_id"));
                } else {
                    list.add(jsonArray.getString(i));
                }
            }
        }

        return list;
    }
}







