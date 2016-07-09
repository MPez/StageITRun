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
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });

    }

    private void attemptRegister() {
        // Reset errors.
        nomeView.setError(null);
        cognomeView.setError(null);
        emailView.setError(null);
        passwordView.setError(null);
        passwordConfirmView.setError(null);

        // Store values at the time of the login attempt.
        final String nome = nomeView.getText().toString();
        final String cognome = cognomeView.getText().toString();
        final String email = emailView.getText().toString();
        final String password = passwordView.getText().toString();
        final String password_confirm = passwordConfirmView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(password_confirm) || !password_confirm.equals(password)) {
            passwordConfirmView.setError(getString(R.string.error_required_password));
            focusView = passwordConfirmView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailView.setError(getString(R.string.error_field_required));
            focusView = emailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailView.setError(getString(R.string.error_invalid_email));
            focusView = emailView;
            cancel = true;
        }

        // Check for valid nome e cognome
        if (TextUtils.isEmpty(nome)) {
            nomeView.setError(getString(R.string.error_field_required));
            focusView = nomeView;
            cancel = true;
        }

        if (TextUtils.isEmpty(cognome)) {
            cognomeView.setError(getString(R.string.error_field_required));
            focusView = cognomeView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            queue = RequestQueueSingleton.getInstance(getApplicationContext());
            String userUrl = queue.getURL() + "/user/registra";

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

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
}
