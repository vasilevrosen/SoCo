package com.soco.ebusiness.soco;

import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class TerminvermittlungActivity extends MainActivity {

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminvermittlung);
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load
        // titles
        // from
        // strings.xml

        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);// load icons from
        // strings.xml

        set(navMenuTitles, navMenuIcons);
    }


    // OnClick Handler der die RezeptActivity startet
    public void navigateToMeineEvents(View view)
    {
        if(App.get_loginstate()) {
            Intent intent = new Intent(TerminvermittlungActivity.this, MeineEventsActivity.class);
            startActivity(intent);
        } else sendToast(getString(R.string.msg_loginmissing));
    }

    public void navigateToKocheventAnbieten(View view)
    {
        if(App.get_loginstate()) {
            Intent intent = new Intent(TerminvermittlungActivity.this, KocheventAnbietenActivity.class);
            startActivity(intent);
        } else sendToast(getString(R.string.msg_loginmissing));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_terminvermittlung, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }
}
