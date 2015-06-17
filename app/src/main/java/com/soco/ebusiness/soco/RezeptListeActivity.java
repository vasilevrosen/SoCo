package com.soco.ebusiness.soco;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class RezeptListeActivity extends ListActivity {

    public ArrayList<String> listOfSharedWord = new ArrayList<String>();
    String x;
    String query;
    ListView lv;
    List<ParseObject> objectsl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_rezept_liste);

        ParseObject.registerSubclass(Rezept.class);

        Bundle i = getIntent().getExtras();
        // Receiving the Data
        String rezeptgesucht = i.getString("RezeptSuche");
        boolean rb_name = i.getBoolean("rbName");
        boolean rb_kategorie = i.getBoolean("rbKategorie");
        boolean rb_zutaten = i.getBoolean("rbZutaten");
        lv = getListView();


        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Rezept");
        if (rb_name) {
            query.whereContains("titel", rezeptgesucht);
        }
        else if (rb_kategorie) {
            query.whereContains("kategorie", rezeptgesucht);
        }
        else {
            query.whereContains("zutaten", rezeptgesucht);
        }

        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    objectsl = objects;
                    for (int i = 0; i < objects.size(); i++) {
                        x = objects.get(i).getString("titel");
                        listOfSharedWord.add(x);
                    }
                    Toast.makeText(getApplicationContext(), "Es wurde(n) " + listOfSharedWord.size() + " Rezept(e) gefunden", Toast.LENGTH_SHORT).show();

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

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String objectId = objectsl.get(position).getObjectId();

        Intent intent = new Intent(RezeptListeActivity.this, RezeptDetailsActivity.class);
        intent.putExtra("objectId",objectId);
        startActivity(intent);

    }
}
