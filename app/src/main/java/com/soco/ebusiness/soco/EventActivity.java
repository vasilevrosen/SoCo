package com.soco.ebusiness.soco;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;


public class EventActivity extends ActionBarActivity {

    TextView titel;
    TextView beschreibung;
    TextView plzUndOrt;
    TextView datumUndUhrzeit;
    TextView rezeptLink;
    TextView teilnehmerText;
    String ausgewaehltesRezeptID;
    Button btnAnfrage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        titel = (TextView) findViewById(R.id.eventTitel);
        beschreibung = (TextView) findViewById(R.id.beschreibungsText);
        plzUndOrt = (TextView) findViewById(R.id.TV_PLZundOrt);
        datumUndUhrzeit = (TextView) findViewById(R.id.TV_UhrzeitUndDatum);
        rezeptLink = (TextView) findViewById(R.id.ausgewählesRezept);
        teilnehmerText = (TextView) findViewById(R.id.TeilnehmerText);

        btnAnfrage = (Button) findViewById(R.id.btnAnfrage);

        Bundle i = getIntent().getExtras();
        String id = i.getString("objectId");

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        query.getInBackground(id, new GetCallback<ParseObject>() {
            public void done(final ParseObject object, ParseException e) {
                if (e == null) {

                    titel.setText(object.get("Titel").toString());

                    try {
                        beschreibung.setText(object.get("Beschreibung").toString());
                    }
                    catch(Exception ex) {

                        beschreibung.setText("Vom Event-Ersteller wurde keine Beschreibung hinzugefügt");
                    }

                    String plzUndOrtString = "In " + object.get("PLZ").toString() + " " + object.get("Ort").toString();

                    plzUndOrt.setText(plzUndOrtString);

                    String datumUndUhrzeitString = "Am " + object.get("Datum").toString() + " um " + object.get("Uhrzeit").toString();

                    datumUndUhrzeit.setText(datumUndUhrzeitString);

                    ausgewaehltesRezeptID = object.get("Rezept_ID").toString();


                    int aktuelleTeilnehmerAnzahl = object.getJSONArray("aktuelleTeilnehmer").length();
                    int maxTeilnehmerAnzahl = object.getInt("MaxTeilnehmer");

                    String teilnehmerString = "Aktuelle Teilnehmerzahl: " + Integer.toString(aktuelleTeilnehmerAnzahl) + " von "  + Integer.toString(maxTeilnehmerAnzahl);

                    if(aktuelleTeilnehmerAnzahl >= maxTeilnehmerAnzahl){

                        teilnehmerString += "\nEs hat leider keine Plätze mehr frei. Schau später noch mal vorbei.";
                        btnAnfrage.setClickable(false);
                    }
                    else {
                        teilnehmerString += "\nEs hat für dich noch einen Platz frei";

                    }

                    teilnehmerText.setText(teilnehmerString);



                    ParseQuery<ParseObject> rezeptQuery = ParseQuery.getQuery("Rezept");
                    rezeptQuery.getInBackground(ausgewaehltesRezeptID, new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject parseObject, ParseException e) {
                            rezeptLink.setText(parseObject.get("titel").toString());
                        }
                    });




                } else {

                }
            }
        });






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event, menu);
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

    public void navigateToRezept2(View view){

        Intent intent = new Intent(EventActivity.this, RezeptDetailsActivity.class);
        intent.putExtra("objectId", ausgewaehltesRezeptID);
        startActivity(intent);


    }
}
