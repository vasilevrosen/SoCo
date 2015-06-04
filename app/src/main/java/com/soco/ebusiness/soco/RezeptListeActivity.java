package com.soco.ebusiness.soco;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class RezeptListeActivity extends ActionBarActivity {

    public ArrayList<String> listOfSharedWord = new ArrayList<String>();
    String x;
    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rezept_liste);
        ParseObject.registerSubclass(Rezept.class);

        Bundle i = getIntent().getExtras();
        // Receiving the Data
        String rezeptgesucht = i.getString("RezeptSuche");
        boolean sw_name = i.getBoolean("SwitchName");
        boolean sw_kategorie = i.getBoolean("SwitchKategorie");
        boolean sw_zutaten = i.getBoolean("SwitchZutaten");

        findViewById(R.id.rezept_liste);
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Rezept");
        if (sw_name) {
            query.whereEqualTo("titel", rezeptgesucht);
        }
        if (sw_kategorie) {
            query.whereEqualTo("kategorie", rezeptgesucht);
        }
        if (sw_zutaten) {
            query.whereEqualTo("zutaten", rezeptgesucht);
        }
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        x = objects.get(i).getString("titel");
                        listOfSharedWord.add(x);
                    }
                    Toast.makeText(getApplicationContext(), "Es wurden " + listOfSharedWord.size() + " Rezepte gefunden", Toast.LENGTH_LONG).show();
                    ListView lv = (ListView) findViewById(R.id.rezept_liste);
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(RezeptListeActivity.this, android.R.layout.simple_list_item_1, listOfSharedWord);
                    lv.setAdapter(arrayAdapter);
                } else {
                    e.getMessage();
                    Toast.makeText(getApplicationContext(), "failed!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rezept_liste, menu);
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
