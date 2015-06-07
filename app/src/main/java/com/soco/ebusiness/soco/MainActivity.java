


package com.soco.ebusiness.soco;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO USER RESTRICTED ACCESS
        if(true==false) {
            Button btn = (Button) findViewById(R.id.btn_event_erstellen);
            btn.setEnabled(true);
            btn.setVisibility(View.VISIBLE);
            Button btn2 = (Button) findViewById(R.id.btn_meine_events);
            btn2.setEnabled(true);
            btn2.setVisibility(View.VISIBLE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {


        switch (number) {
            //Hauptseite anzeigen
            case 1:
                mTitle = getString(R.string.app_name);
                break;

                //folgend, alle weiteren Seiten

            case 2:
                mTitle = getString(R.string.terminvermittlung);
                Intent intent = new Intent(this, TerminvermittlungActivity.class);
                startActivity(intent);
                break;
            case 3:
                mTitle = getString(R.string.rezeptkatalog);
                intent = new Intent(this, RezeptActivity.class);
                startActivity(intent);
                break;
            case 4:
                mTitle = getString(R.string.wissensdb);
                intent = new Intent(this, WissensDBActivity.class);
                startActivity(intent);
                break;
            case 5:
                mTitle = getString(R.string.user_profile);
                intent = new Intent(this, FacebookActivity.class);
                startActivity(intent);
                break;
            case 6:
                if(App.login){
                    mTitle = getString(R.string.logout);
                }
                mTitle = getString(R.string.login);
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    public void meine_events(View view) {
        if(App.login) {
            Intent intent = new Intent(this, MeineEventsActivity.class);
            startActivity(intent);
        }
    }

    public void create_event(View view) {
        if(App.login) {
            Intent intent = new Intent(this, KocheventAnbietenActivity.class);
            startActivity(intent);
        }
    }
    public void event_map(View view) {
        // Intent intent = new Intent(this, KocheventsuchenActivity.class);
        // startActivity(intent);
    }

}
