package com.soco.ebusiness.soco;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import com.parse.ParseObject;


public class RezeptSuchenActivity extends ActionBarActivity {

    private EditText rezeptSuchen;
    private Switch sw_name;
    private Switch sw_kategorie;
    private Switch sw_zutaten;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rezept_suchen);
        ParseObject.registerSubclass(Rezept.class);

        rezeptSuchen = (EditText) findViewById(R.id.editText_rezept_suche);
        sw_name = (Switch) findViewById(R.id.switch_name_suchen);
        sw_kategorie = (Switch) findViewById(R.id.switch_beschreibung_durchsuchen);
        sw_zutaten = (Switch) findViewById(R.id.switch_zutaten_durchsuchen);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rezept_suchen, menu);
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

    public void rezeptSuchen(View view)
    {
        Intent intent = new Intent(this, RezeptListeActivity.class);
        intent.putExtra("RezeptSuche", rezeptSuchen.getText().toString());
        intent.putExtra("SwitchName", sw_name.isChecked());
        intent.putExtra("SwitchKategorie", sw_kategorie.isChecked());
        intent.putExtra("SwitchZutaten", sw_zutaten.isChecked());
        startActivity(intent);
    }


}
