package com.soco.ebusiness.soco;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;


public class BewerberCheckActivity extends ActionBarActivity {

    TextView bewerberNameTV;
    String eventId;
    String bewerberId;
    String bewerberName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bewerber_check);

        Bundle i = getIntent().getExtras();

        eventId = i.getString("eventId");
        bewerberId = i.getString("objectId");

        bewerberNameTV = (TextView) findViewById(R.id.BewerberName);

        ParseQuery<ParseUser> getBewerber = ParseUser.getQuery();
        getBewerber.whereEqualTo("objectId", bewerberId);

        ParseUser bewerber = null;

        try {
            bewerber = getBewerber.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        bewerberName = bewerber.getString("username");

        if(bewerberName.equals("AnB3opymmRJccwAQ7sVRXCrQg")){
            bewerberName = "Benny Foronkel";
        }

        if(bewerberName.equals("GNIM4Sx6meHTS54Og1z5pVJ0v")){
            bewerberName = "Steffen Teichman";
        }

        bewerberNameTV.setText(bewerberName);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bewerber_check, menu);
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

    public void onClickAnnahmen(View view){


        ParseQuery<ParseObject> getEvent = new ParseQuery<ParseObject>("Event");
        getEvent.whereEqualTo("objectId", eventId);

        ParseObject event = null;

        try {
            event = getEvent.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JSONArray aktuelleTeilnehmer = event.getJSONArray("aktuelleTeilnehmer");
        JSONArray aktuelleBewerber = event.getJSONArray("Bewerber");

        aktuelleTeilnehmer.put(bewerberId);



        for(int i = 0; i < aktuelleBewerber.length(); i++){

            String bewerber = null;

            try {
                bewerber = aktuelleBewerber.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(bewerber.equals(bewerberId)){
                aktuelleBewerber.remove(i);
                break;
            }



        }

        event.put("aktuelleTeilnehmer", aktuelleTeilnehmer);
        event.put("Bewerber", aktuelleBewerber);

        try {
            event.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }



        Toast.makeText(this, "Super, damit hast du einen weiteren Gast", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(BewerberCheckActivity.this, EventActivity.class);
        intent.putExtra("objectId", eventId);
        startActivity(intent);



    }

    public void onClickAblehnung(View view){


        ParseQuery<ParseObject> getEvent = new ParseQuery<ParseObject>("Event");
        getEvent.whereEqualTo("objectId", eventId);

        ParseObject event = null;

        try {
            event = getEvent.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JSONArray aktuelleBewerber = event.getJSONArray("Bewerber");


        for(int i = 0; i < aktuelleBewerber.length(); i++){

            String bewerber = null;

            try {
                bewerber = aktuelleBewerber.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(bewerber.equals(bewerberId)){
                aktuelleBewerber.remove(i);
                break;
            }



        }

        event.put("Bewerber", aktuelleBewerber);

        Toast.makeText(this, "Du hast den Bewerber abgelehnt", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(BewerberCheckActivity.this, EventActivity.class);
        intent.putExtra("objectId", eventId);
        startActivity(intent);

    }
}
