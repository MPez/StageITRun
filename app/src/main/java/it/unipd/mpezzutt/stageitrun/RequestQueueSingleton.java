/**
 * StageITRun
 * Progetto per insegnamento Reti Wireless
 * @since Anno accademico 2015/2016
 * @author Pezzutti Marco 1084411
 */
package it.unipd.mpezzutt.stageitrun;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Classe singleton che rappresenta la coda di richieste da fare al server
 */
public class RequestQueueSingleton {
    private static RequestQueueSingleton mInstance;
    private RequestQueue mRequestQueue;

    // URL del server
    private final String URL = "http://192.168.0.131:3000";
    //private final String URL = "http://marcopez.ddns.net:3000";

    private static Context mCtx;

    /**
     * Costruttore privato
     * @param context contesto applicazione
     */
    private RequestQueueSingleton(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    /**
     * Crea l'oggetto coda e lo ritorna
     * @param context contesto applicazione
     * @return oggetto singleton coda
     */
    public static synchronized RequestQueueSingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RequestQueueSingleton(context);
        }
        return mInstance;
    }

    /**
     * Ritorna la coda di richieste
     * @return
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    /**
     * Aggiunge una richiesta in coda
     * @param req riciesta da aggiungere
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    /**
     * Ritorna l'URL del server
     * @return URL del server
     */
    public String getURL() {
        return URL;
    }
}
