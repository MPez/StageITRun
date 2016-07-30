/**
 * StageITRun
 * Progetto per insegnamento Reti Wireless
 * @since Anno accademico 2015/2016
 * @author Pezzutti Marco 1084411
 */
package it.unipd.mpezzutt.stageitrun;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.unipd.mpezzutt.stageitrun.model.Stage;
import it.unipd.mpezzutt.stageitrun.model.Trofeo;
import it.unipd.mpezzutt.stageitrun.model.Utente;

/**
 * Activity principale
 */
public class MainActivity extends AppCompatActivity
        implements StageFragment.OnStageFragmentInteraction,
        TrophyFragment.OnTrophyFragmentInteraction,
        UserRankFragment.OnUserRankFragmentInteraction {

    static final int USER_LOGIN = 1;
    static final int USER_PROFILE = 2;

    private UserLogin userLogin;
    private RequestQueueSingleton queue;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // recupera l'utente corrente
        userLogin = UserLogin.getInstance();

        if (userLogin.getUtente() == null) {
            // se l'utente non ha fatto il login, esegue l'activity di login
            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivityForResult(loginIntent, USER_LOGIN);
        }

        viewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(viewPager);

        // crea e imposta lo spinner per l'ordinamento della lista stage
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sort_stage, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // recupera il fragment che contiene la lista stage e aggiorna la lista in base all'ordinamento scelto
                StageFragment stageFragment = (StageFragment) viewPagerAdapter.getRegisteredFragment(viewPager.getCurrentItem());
                stageFragment.updateStageList(getOrder(parent.getItemAtPosition(position).toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);

                // mostra lo spinner solo quando è aperto il fragment con la lista stage
                int pos = tab.getPosition();
                if (pos != 0) {
                    spinner.setVisibility(View.GONE);
                } else {
                    spinner.setVisibility(View.VISIBLE);
                }

                updateFragment(viewPagerAdapter.getRegisteredFragment(viewPager.getCurrentItem()));
            }
        });
    }

    /**
     * Richiama l'activity di specifica di uno stage
     * @param item stage di cui si vuole la specifica
     */
    @Override
    public void onStageItemSelected(Stage item) {
        Intent stageSpecIntent = new Intent(this, StageSpecActivity.class);
        stageSpecIntent.putExtra("stage", item);
        startActivity(stageSpecIntent);
    }

    /**
     * Richiama l'actovity di specifica di un trofeo
     * @param item trofeo di cui si vuole la specifica
     */
    @Override
    public void onTrophyItemSelected(Trofeo item) {
        Intent trophySpecIntent = new Intent(this, TrophySpecActivity.class);
        trophySpecIntent.putExtra("trofeo", item);
        startActivity(trophySpecIntent);
    }

    @Override
    public void onUserItemSelected(Utente item) {

    }

    /**
     * Impostazione gestore pagine nel tablayout
     * @param viewPager
     */
    private void setupViewPager (ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        Fragment stageFragment = StageFragment.newInstance();
        viewPagerAdapter.addFragment(stageFragment, "STAGE");
        Fragment trofeoFragment = TrophyFragment.newInstance();
        viewPagerAdapter.addFragment(trofeoFragment, "TROFEI");
        Fragment userFragment = UserRankFragment.newInstance();
        viewPagerAdapter.addFragment(userFragment, "CLASSIFICA");
        viewPager.setAdapter(viewPagerAdapter);
    }

    /**
     * Classe che rappresenta il gestore delle pagine nel tabLayout
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();
        private SparseArray<Fragment> registeredFragment = new SparseArray<>();

        /**
         * Costruttore
         * @param fm gestore dei fragment
         */
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Aggiunge un fragment alla lista
         * @param fragment fragment da aggiungere
         * @param title titolo del fragment da visualizzare nel tab
         */
        public void addFragment (Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitle.add(title);
        }

        /**
         * Ritorna il fragment specificato dalla posizione
         * @param position posizione del fragment da ritornare
         * @return fragment richiesto
         */
        public Fragment getRegisteredFragment(int position) {
            return registeredFragment.get(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragment.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragment.remove(position);
            super.destroyItem(container, position, object);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // risponde alla pressione del pulsante per la scansione dei QR code
        // avviando la scansione solo se l'utente ha effettuato il login
        if (id == R.id.action_qrcode) {
            if (userLogin.getUtente() == null) {
                Toast.makeText(getApplicationContext(),
                        "Necessario effettuare il login per registrare uno stage.",
                        Toast.LENGTH_LONG).show();
            } else {
                IntentIntegrator integrator = new IntentIntegrator(this);
                integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);
            }
        }
        // risponde alla pressione del pulsante del profilo utente
        // avviando l'activity che visualizza il profilo utente solo se l'utente ha effettuato il login,
        // altrimenti apre la pagina di login
        else if (id == R.id.action_user) {
            if (userLogin.getUtente() != null) {
                Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
                startActivityForResult(intent, USER_PROFILE);
            } else {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent, USER_LOGIN);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // gestisce il risultato dalla scansione del QR code
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            String contents = result.getContents();
            if (contents != null) {
                try {
                    JSONObject jsonObject = new JSONObject(contents);
                    registraStage(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "La scansione non ha prodotto alcun risultato oppure è stata annullata", Toast.LENGTH_LONG).show();
            }
        }

        // gestisce il risultato della chiusura del profilo utente
        if (requestCode == USER_PROFILE) {
            if (resultCode == RESULT_CANCELED) {
                userLogin.setUtente(null);
                updateFragment(viewPagerAdapter.getRegisteredFragment(viewPager.getCurrentItem()));
                Toast.makeText(getApplicationContext(), "Logout effettuato", Toast.LENGTH_SHORT).show();
            }
        }
        // gestisce il risultato del login utente
        else if (requestCode == USER_LOGIN) {
            if (resultCode == RESULT_OK) {
                updateUser(data.getStringExtra("email"));
                updateFragment(viewPagerAdapter.getRegisteredFragment(viewPager.getCurrentItem()));
                Toast.makeText(getApplicationContext(), "Login effettuato", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Aggiorna l'utente corrente effettuando una richiesta al server
     * @param email email dell'utente corrente
     */
    private void updateUser(String email) {
        queue = RequestQueueSingleton.getInstance(getApplicationContext());

        String userUrl = queue.getURL() + "/user/" + email;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                userUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            userLogin.setUtente(Utente.toUtente(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "update user" + error.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                });
        queue.addToRequestQueue(jsonObjectRequest);
    }

    /**
     * Registra l'inizio o la fine di uno stage effettuando una richiesta al server
     * @param jsonObject oggetto JSON che contiene i dati dello stage presi dal QR code
     * @throws JSONException
     */
    private void registraStage(final JSONObject jsonObject) throws JSONException {
        queue = RequestQueueSingleton.getInstance(this.getApplicationContext());

        String stageUrl = queue.getURL() + "/user/" +
                userLogin.getUtente().getEmail() + "/stage";

        if (jsonObject.getString("tag").equals("start")) {
            stageUrl += "/inizia";
        } else {
            stageUrl += "/termina";
        }

        final StringRequest request = new StringRequest(Request.Method.POST, stageUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Fragment fragment = viewPagerAdapter.getRegisteredFragment(viewPager.getCurrentItem());
                        switch (response) {
                            case "stage iniziato":
                                updateUser(userLogin.getUtente().getEmail());
                                updateFragment(fragment);
                                Toast.makeText(getApplicationContext(), "Stage iniziato",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case "stage presente":
                                Toast.makeText(getApplicationContext(), "Stage già effettuato",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case "stage terminato":
                                updateUser(userLogin.getUtente().getEmail());
                                updateFragment(fragment);
                                controllaTrofei();
                                Toast.makeText(getApplicationContext(), "Stage terminato",
                                        Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "registra stage" + error.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                try {
                    map.put("stage_id", jsonObject.getString("stage_id"));
                    map.put("tag", jsonObject.getString("tag"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return map;
            }
        };
        queue.addToRequestQueue(request);
    }

    /**
     * Aggiorna il contenuto del fragment visualizzato nel tabLayout
     * @param fragment
     */
    public void updateFragment(Fragment fragment) {
        if (fragment instanceof StageFragment) {
            ((StageFragment) fragment).updateStageList(getOrder(spinner.getSelectedItem().toString()));
        } else if (fragment instanceof UserRankFragment) {
            ((UserRankFragment) fragment).updateRankList();
        } else if (fragment instanceof TrophyFragment) {
            ((TrophyFragment) fragment).updateTrophyList();
        }
    }

    /**
     * Ritorna il tipo di ordinamento da applicare alla lista stage,
     * applicando una coversione
     * @param order ordinamento scelto
     * @return ordinamento convertito
     */
    public String getOrder(String order) {
        switch (order) {
            case "Abc \u2191":
                order = "abc/asc";
                break;
            case "Abc \u2193":
                order = "abc/desc";
                break;
            case "Coda \u2191":
                order = "coda/asc";
                break;
            case "Coda \u2193":
                order = "coda/desc";
                break;
            default:
                break;
        }
        return order;
    }

    /**
     * Controlla se l'utente sta per vincere un trofeo,
     * in caso affermativo lo visualizza e lo registra nel database, altrimenti non fa nulla
     */
    public void controllaTrofei() {
        final String trofeo_id;

        switch (userLogin.getUtente().getStages_end().size()) {
            case 0:
                trofeo_id = "T001";
                break;
            case 4:
                trofeo_id = "T002";
                break;
            case 9:
                trofeo_id = "T003";
                break;
            case 14:
                trofeo_id = "T004";
                break;
            case 19:
                trofeo_id = "T005";
                break;
            default:
                trofeo_id = null;
                break;
        }

        String url = queue.getURL() + "/user/" + userLogin.getUtente().getEmail() + "/trofeo/aggiungi";

        if (trofeo_id != null) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("trofeo aggiunto")) {
                                Fragment fragment = viewPagerAdapter.getRegisteredFragment(viewPager.getCurrentItem());
                                updateUser(userLogin.getUtente().getEmail());
                                if (fragment instanceof TrophyFragment) {
                                    ((TrophyFragment) fragment).updateTrophyList();
                                }

                                Intent intent = new Intent(getApplicationContext(), TrophyWinActivity.class);
                                TrophyFragment trophyFragment = (TrophyFragment) viewPagerAdapter.getRegisteredFragment(1);
                                intent.putExtra("nome", trophyFragment.getNomeTrofeo(trofeo_id));
                                startActivity(intent);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "controlla trofeo" + error.toString(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("trofeo_id", trofeo_id);
                    return map;
                }
            };
            queue.addToRequestQueue(stringRequest);
        }
    }

}
