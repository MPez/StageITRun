/**
 * StageITRun
 * Progetto per insegnamento Reti Wireless
 * @since Anno accademico 2015/2016
 * @author Pezzutti Marco 1084411
 */
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
import java.util.Map;
import java.util.TimeZone;

import it.unipd.mpezzutt.stageitrun.model.Stage;
import it.unipd.mpezzutt.stageitrun.model.Trofeo;
import it.unipd.mpezzutt.stageitrun.model.Utente;

/**
 * Parser JSON
 */
public class JSONParser {

    /**
     * Legge un array JSON e ritorna una lista di stringhe
     * @param jsonArray array JSON da leggere
     * @return lista di stringhe contenute in jsonArray
     * @throws JSONException
     */
    public static List<String> toList(JSONArray jsonArray) throws JSONException {
        List<String> list = new ArrayList<>();

        if (jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.getString(i));
            }
        }

        return list;
    }

    /**
     * Legge un arra JSON e ritorna una mappa di stringhe a stringhe
     * @param jsonArray array JSON da leggere
     * @return mappa di stringhe a stringhe contenute in jsonArray
     * @throws JSONException
     */
    public static Map<String, String> toMap(JSONArray jsonArray) throws JSONException {
        Map<String, String> map = new HashMap<>();

        DateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DateFormat writeFormat = new SimpleDateFormat("HH:mm:ss");
        writeFormat.setTimeZone(TimeZone.getTimeZone("GMT+04"));

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







