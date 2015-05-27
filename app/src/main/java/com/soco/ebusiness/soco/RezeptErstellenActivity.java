package com.soco.ebusiness.soco;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseObject;


public class RezeptErstellenActivity extends ActionBarActivity {

    EditText titel;
    EditText kategorie;
    EditText zutaten;
    EditText zubereitungszeit;
    Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rezept_erstellen);
        ParseObject.registerSubclass(Rezept.class);

        titel = (EditText) findViewById(R.id.titel);
        kategorie = (EditText) findViewById(R.id.kategorie);
        zutaten = (EditText) findViewById(R.id.zutaten);
        zubereitungszeit = (EditText) findViewById(R.id.zubereitungszeit);
        btn_save = (Button) findViewById(R.id.button_speichern);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Rezept rezept = new Rezept();
                rezept.setTitle(titel.getText().toString());
                rezept.setKategorie(kategorie.getText().toString());
                rezept.setZutaten(zutaten.getText().toString());
                rezept.setZubereitungszeit(zubereitungszeit.getText().toString());
                rezept.saveEventually();

                Intent intent = new Intent(RezeptErstellenActivity.this, RezeptActivity.class);
                startActivity(intent);

                Toast.makeText(RezeptErstellenActivity.this, "Das Rezept " + titel.getText().toString() + " wurde erfolgreich gespeichert", Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rezept_erstellen, menu);
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
