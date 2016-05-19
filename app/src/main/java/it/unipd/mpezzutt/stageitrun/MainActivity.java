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

public class MainActivity extends AppCompatActivity
        implements StageFragment.OnStageFragmentInteraction,
        TrophyFragment.OnTrophyFragmentInteraction {

    static final int USER_LOGIN = 1;
    static final int USER_PROFILE = 2;

    private UserLogin userLogin;
    private RequestQueueSingleton queue;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userLogin = UserLogin.getInstance();

        if (userLogin.getUtente() == null) {
            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivityForResult(loginIntent, USER_LOGIN);
        }

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(viewPager);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sort_stage, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String order = parent.getItemAtPosition(position).toString();
                StageFragment stageFragment = (StageFragment) viewPagerAdapter.getRegisteredFragment(viewPager.getCurrentItem());
                stageFragment.updateStageList(order);
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

                int pos = tab.getPosition();
                if (pos == 1) {
                    spinner.setVisibility(View.GONE);
                } else {
                    spinner.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public void onStageItemSelected(Stage item) {
        Intent stageSpecIntent = new Intent(this, StageSpecActivity.class);
        stageSpecIntent.putExtra("stage", item);
        startActivity(stageSpecIntent);
    }

    @Override
    public void onTrophyItemSelected(Trofeo item) {
        Intent trophySpecIntent = new Intent(this, TrophySpecActivity.class);
        trophySpecIntent.putExtra("trofeo", item);
        startActivity(trophySpecIntent);
    }

    private void setupViewPager (ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        Fragment stageFragment = StageFragment.newInstance();
        viewPagerAdapter.addFragment(stageFragment, "STAGE");
        Fragment trofeoFragment = TrophyFragment.newInstance();
        viewPagerAdapter.addFragment(trofeoFragment, "TROFEI");
        viewPager.setAdapter(viewPagerAdapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();

        private SparseArray<Fragment> registeredFragment = new SparseArray<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment (Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitle.add(title);
        }

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_qrcode) {
            if (userLogin.getUtente() == null) {
                Toast.makeText(getApplicationContext(),
                        "Necessario effettuare il login per registrare uno stage.",
                        Toast.LENGTH_LONG).show();
            } else {
                IntentIntegrator integrator = new IntentIntegrator(this);
                integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);
            }

        } else if (id == R.id.action_user) {
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
        // QRCODE scan result
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

        if (requestCode == USER_PROFILE) {
            if (resultCode == RESULT_CANCELED) {
                userLogin.setUtente(null);
                Toast.makeText(getApplicationContext(), "Logout effettuato", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == USER_LOGIN) {
            if (resultCode == RESULT_OK) {
                updateUser(data.getStringExtra("email"));
                Toast.makeText(getApplicationContext(), "Login effettuato", Toast.LENGTH_SHORT).show();
            }
        }
    }

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
                        Toast.makeText(MainActivity.this, error.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                });
        queue.addToRequestQueue(jsonObjectRequest);
    }

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
                        switch (response) {
                            case "stage iniziato":
                                updateUser(userLogin.getUtente().getEmail());
                                Toast.makeText(getApplicationContext(), "Stage iniziato",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case "stage presente":
                                Toast.makeText(getApplicationContext(), "Stage già effettuato",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case "stage terminato":
                                updateUser(userLogin.getUtente().getEmail());
                                Toast.makeText(getApplicationContext(), "Stage terminato",
                                        Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(),
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
}
