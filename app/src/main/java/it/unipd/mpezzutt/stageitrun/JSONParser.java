package it.unipd.mpezzutt.stageitrun;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

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
                list.add(jsonArray.getString(i));
            }
        }

        return list;
    }

    public static Map<String, String> toMap(JSONArray jsonArray) throws JSONException {
        Map<String, String> map = new HashMap<>();
        DateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DateFormat writeFormat = new SimpleDateFormat("HH:mm:ss");
        writeFormat.setTimeZone(TimeZone.getTimeZone("GMT+02"));

        if (jsonArray.length() > 0) {
            for (int i = 0; i <jsonArray.length() ; i++) {
                try {
                    Date date = parseFormat.parse(jsonArray.getJSONObject(i).getString("time"));
                    map.put(jsonArray.getJSONObject(i).getString("stage_id"),
                            writeFormat.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return map;
    }
}







