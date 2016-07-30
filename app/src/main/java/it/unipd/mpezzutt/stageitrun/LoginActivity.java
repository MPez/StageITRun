/**
 * StageITRun
 * Progetto per insegnamento Reti Wireless
 * @since Anno accademico 2015/2016
 * @author Pezzutti Marco 1084411
 */
package it.unipd.mpezzutt.stageitrun;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Activity che offre una finestra di login all'utente
 */
public class LoginActivity extends AppCompatActivity {

    private RequestQueueSingleton queue;
    static final int USER_REGISTER = 3;

    private EditText mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        // Imposta il listener per effettuare il login dal campo password tramite tasto invio
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);

        //Imposta il listener per effettuare il login tramite pressione del pulsante di conferma
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }


    /**
     * Prova ad effettuare il login oppure la registrazione del nuovo utente.
     * In caso di errori (email errata, campi mancanti) vengono visualizzati gli errori
     * e non viene effettuato il login
     */
    private void attemptLogin() {

        mEmailView.setError(null);
        mPasswordView.setError(null);

        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Controllo password
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Controllo email valida
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // Errore rilevato: viene evidenziato l'errore.
            focusView.requestFocus();
        } else {

            queue = RequestQueueSingleton.getInstance(getApplicationContext());
            String userUrl = queue.getURL() + "/user";

            // richiesta di login al server
            StringRequest stringRequest = new StringRequest(Request.Method.POST, userUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            switch (response) {
                                // login valido
                                case "success" :
                                    Intent intent = new Intent();
                                    intent.putExtra("email", email);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                    break;
                                // utente non esistente, registrazione utente
                                case "not found" :
                                    registerUser(email, password);
                                    break;
                                // login errato
                                case "failure" :
                                    Toast.makeText(LoginActivity.this,
                                            "Password errata, riprovare",
                                            Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(LoginActivity.this, "attempt login" + error.toString(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("email", email);
                    map.put("password", password);
                    return map;
                }
            };

            queue.addToRequestQueue(stringRequest);
        }
    }

    /**
     * Richiama l'activity di registrazione utente passando email e password del nuovo utente
     * @param email email nuovo utente
     * @param password password nuovo utente
     */
    private void registerUser(String email, String password) {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        startActivityForResult(intent, USER_REGISTER);
    }

    /**
     * Controllo validità email
     * @param email email da verificare
     * @return true se l'email è valida, false altrimenti
     */
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    /**
     * Controlla la validità della password
     * @param password password da controllare
     * @return true se la password è valida, false altrimenti
     */
    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Dopo la registrazione del nuovo utente, termina l'activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == USER_REGISTER) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent();
                intent.putExtra("email", data.getStringExtra("email"));
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}

