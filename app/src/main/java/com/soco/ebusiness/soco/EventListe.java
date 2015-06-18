package com.soco.ebusiness.soco;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class EventListe extends ListActivity {

    private ArrayList<String> titelDerEvents = new ArrayList<>();
    private ListView lv;
    private List<ParseObject> allObjects;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_liste);

        ParseObject.registerSubclass(Event.class);

        Bundle i = getIntent().getExtras();

        String gesuchterOrt  =i.getString("Ort");
        lv = getListView();



            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Event");

            query.whereEqualTo("Ort", gesuchterOrt);






            query.findInBackground(new FindCallback<ParseObject>() {


                @Override
                public void done(List<ParseObject> gefundeneEvents, ParseException e) {
                    if (e == null) {

                        Toast.makeText(getApplicationContext(), "Es wurden " + Integer.toString(gefundeneEvents.size()) + " gefunden", Toast.LENGTH_SHORT).show();


                        allObjects = gefundeneEvents;

                        for (int i = 0; i < gefundeneEvents.size(); i++) {


                            ParseObject po = gefundeneEvents.get(i);


                            titelDerEvents.add(po.getString("Titel"));


                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EventListe.this, android.R.layout.simple_list_item_1, titelDerEvents);
                        lv.setAdapter(adapter);


                    } else {



                                    e.getMessage();
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                    }


                }
            });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_liste, menu);
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

        String objectId = allObjects.get(position).getObjectId();

        Intent intent = new Intent(EventListe.this, EventActivity.class);
        intent.putExtra("objectId",objectId);
        startActivity(intent);



    }



}
