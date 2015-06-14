package com.soco.ebusiness.soco;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.parse.ParseObject;


public class RezeptSuchenActivity extends ActionBarActivity {

    private EditText rezeptSuchen;
    private RadioButton rb_name;
    private RadioButton rb_kategorie;
    private RadioButton rb_zutaten;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rezept_suchen);
        ParseObject.registerSubclass(Rezept.class);

        rezeptSuchen = (EditText) findViewById(R.id.editText_rezept_suche);
        rb_name = (RadioButton) findViewById(R.id.rb_name);
        rb_kategorie = (RadioButton) findViewById(R.id.rb_beschreibung);
        rb_zutaten = (RadioButton) findViewById(R.id.rb_zutaten);
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
        intent.putExtra("rbName", rb_name.isChecked());
        intent.putExtra("rbKategorie", rb_kategorie.isChecked());
        intent.putExtra("rbZutaten", rb_zutaten.isChecked());
        startActivity(intent);
    }


}
