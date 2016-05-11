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
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements StageFragment.OnStageFragmentInteraction,
        TrophyFragment.OnTrophyFragmentInteraction {

    private Utente utente;
    private UserLogin userLogin = UserLogin.getInstance();

    static final int USER_LOGIN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (userLogin.getUtente() != null) {
            utente = userLogin.getUtente();
        } else {
            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivityForResult(loginIntent, USER_LOGIN);
        }

        RequestQueueSingleton queue = RequestQueueSingleton.getInstance(this.getApplicationContext());

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == USER_LOGIN) {
            if (resultCode == RESULT_OK) {
                utente = (Utente) data.getSerializableExtra("utente");
                userLogin.setUtente(utente);
            }
        }
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
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        Fragment stageFragment = StageFragment.newInstance(utente);
        viewPagerAdapter.addFragment(stageFragment, "STAGE");
        Fragment trofeoFragment = TrophyFragment.newInstance(utente);
        viewPagerAdapter.addFragment(trofeoFragment, "TROFEI");
        viewPager.setAdapter(viewPagerAdapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment (Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitle.add(title);
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
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_qrcode) {
            Intent intent = new Intent();
        }

        return super.onOptionsItemSelected(item);
    }

}
