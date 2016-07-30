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
 * Classe che rappresenta il form di registrazione nuovo utente
 */
public class RegisterActivity extends AppCompatActivity {

    private RequestQueueSingleton queue;

    private EditText nomeView;
    private EditText cognomeView;
    private EditText emailView;
    private EditText passwordView;
    private EditText passwordConfirmView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");

        nomeView = (EditText) findViewById(R.id.nome_register);
        cognomeView = (EditText) findViewById(R.id.cognome_register);
        emailView = (EditText) findViewById(R.id.email_register);
        emailView.setText(email);
        passwordView = (EditText) findViewById(R.id.password_register);
        passwordView.setText(password);
        passwordConfirmView = (EditText) findViewById(R.id.password_register_confirm);

        // imposta il listener per effettuare la registrazione dal campo conferma password
        // tramite tasto invio
        passwordConfirmView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == R.id.register || actionId == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });

        Button registerButton = (Button) findViewById(R.id.email_register_button);
        // imposta il listener per effettuare la registrazione tramite pulsante di conferma
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });

    }

    /**
     * Prova ad effettuare la registrazione del nuovo utente
     */
    private void attemptRegister() {

        nomeView.setError(null);
        cognomeView.setError(null);
        emailView.setError(null);
        passwordView.setError(null);
        passwordConfirmView.setError(null);

        final String nome = nomeView.getText().toString();
        final String cognome = cognomeView.getText().toString();
        final String email = emailView.getText().toString();
        final String password = passwordView.getText().toString();
        final String password_confirm = passwordConfirmView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Controllo password
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;
        }

        // Controllo password di conferma
        if (TextUtils.isEmpty(password_confirm) || !password_confirm.equals(password)) {
            passwordConfirmView.setError(getString(R.string.error_required_password));
            focusView = passwordConfirmView;
            cancel = true;
        }

        // controllo email
        if (TextUtils.isEmpty(email)) {
            emailView.setError(getString(R.string.error_field_required));
            focusView = emailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailView.setError(getString(R.string.error_invalid_email));
            focusView = emailView;
            cancel = true;
        }

        // Controllo nome
        if (TextUtils.isEmpty(nome)) {
            nomeView.setError(getString(R.string.error_field_required));
            focusView = nomeView;
            cancel = true;
        }

        // Controllo cognome
        if (TextUtils.isEmpty(cognome)) {
            cognomeView.setError(getString(R.string.error_field_required));
            focusView = cognomeView;
            cancel = true;
        }

        if (cancel) {
            // Errore rilevato: viene evidenziato l'errore
            focusView.requestFocus();
        } else {

            queue = RequestQueueSingleton.getInstance(getApplicationContext());
            String userUrl = queue.getURL() + "/user/registra";

            // richiesta di registrazione al server
            StringRequest stringRequest = new StringRequest(Request.Method.POST, userUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("success")) {
                                Toast.makeText(getApplicationContext(),
                                        "Registrazione effettuata", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent();
                                intent.putExtra("email", email);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(RegisterActivity.this, "attempt register" + error.toString(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("nome", nome);
                    map.put("cognome", cognome);
                    map.put("email", email);
                    map.put("password", password);
                    return map;
                }
            };

            queue.addToRequestQueue(stringRequest);
        }
    }

    /**
     * Controllo validità email
     * @param email email da verificare
     * @return true se email valida, false altrimenti
     */
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    /**
     * Controllo validità password
     * @param password password da verificare
     * @return true se password valida, false altrimenti
     */
    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
}
