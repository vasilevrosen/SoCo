package com.soco.ebusiness.soco;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class KocheventAnbietenActivity extends FragmentActivity {


    private static EditText titel;
    private static EditText maxTeilnehmer;
    private static EditText plz;
    private static EditText strasse;
    private static EditText hausnummer;
    private static EditText ort;


    private static EditText gesetztesDatum;
    private static EditText gesetzteZeit;



    private static String titelOnPause = "";
    private static String maxTeilnehmerOnPause = "";
    private static String plzOnPause = "";
    private static String ortOnPause = "";
    private static String strasseOnPause = "";
    private static String hausnummerOnPause = "";
    private static String datumOnPause = "";
    private static String zeitOnPause = "";

    private static Date datum;

    private static TextView ausgewaehltesRezeptTitelTextView;

    private static String ausgewaehltesRezeptID;

    private static String ausgewaehltesRezeptTitelText = "Bitte Rezept auswählen";

    private static boolean rezeptAusgewaehlt;


    public static void rezeptFuerKochevent(String rezeptID, String rezeptTitel){


        ausgewaehltesRezeptTitelText = rezeptTitel;
        ausgewaehltesRezeptID = rezeptID;

        rezeptAusgewaehlt = true;

    }

    public static String getAusgewaehltesRezeptID(){

        return ausgewaehltesRezeptID;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kochevent_anbieten);

        ParseObject.registerSubclass(Event.class);

        ausgewaehltesRezeptTitelTextView = (TextView) findViewById(R.id.ausgewählesRezeptTitel);

        ausgewaehltesRezeptTitelTextView.setText(ausgewaehltesRezeptTitelText);




        titel = (EditText) findViewById(R.id.editTextEventTitel);
        maxTeilnehmer = (EditText) findViewById(R.id.editTextMaxTeilnehmerzahl);
        plz= (EditText) findViewById(R.id.editTextEventPLZ);
        strasse = (EditText) findViewById(R.id.editTextEventStraße);
        hausnummer = (EditText) findViewById(R.id.editTextEventNummer);
        ort = (EditText) findViewById(R.id.editTextEventOrt);

        gesetztesDatum = (EditText) findViewById(R.id.editTextEventDatum);
        gesetzteZeit = (EditText) findViewById(R.id.editTextEventUhrzeit);




    }





    @Override
    protected void onResume(){


        super.onResume();

        titel.setText(titelOnPause);
        maxTeilnehmer.setText(maxTeilnehmerOnPause);
        ort.setText(ortOnPause);
        plz.setText(plzOnPause);
        strasse.setText(strasseOnPause);
        hausnummer.setText(hausnummerOnPause);
        gesetztesDatum.setText(datumOnPause);
        gesetzteZeit.setText(zeitOnPause);


        ausgewaehltesRezeptTitelTextView.setClickable(rezeptAusgewaehlt);


        if(rezeptAusgewaehlt){
            ausgewaehltesRezeptTitelTextView.setTextColor(Color.parseColor("#003CFF"));
        }
    }

    @Override
    protected void onRestart(){


        super.onRestart();


        titel.setText(titelOnPause);
        maxTeilnehmer.setText(maxTeilnehmerOnPause);
        ort.setText(ortOnPause);
        plz.setText(plzOnPause);
        strasse.setText(strasseOnPause);
        hausnummer.setText(hausnummerOnPause);
        gesetztesDatum.setText(datumOnPause);
        gesetzteZeit.setText(zeitOnPause);

        ausgewaehltesRezeptTitelTextView.setClickable(rezeptAusgewaehlt);


        if(rezeptAusgewaehlt){
            ausgewaehltesRezeptTitelTextView.setTextColor(Color.parseColor("#003CFF"));
        }

    }

    @Override
    protected void onPause(){

        super.onPause();



        titelOnPause = titel.getText().toString();
        maxTeilnehmerOnPause = maxTeilnehmer.getText().toString();
        plzOnPause = plz.getText().toString();
        ortOnPause = ort.getText().toString();
        strasseOnPause = strasse.getText().toString();
        hausnummerOnPause = hausnummer.getText().toString();
        zeitOnPause = gesetzteZeit.getText().toString();
        datumOnPause = gesetztesDatum.getText().toString();


    }

    @Override
    protected void onStop(){

        super.onStop();

        titelOnPause = titel.getText().toString();
        maxTeilnehmerOnPause = maxTeilnehmer.getText().toString();
        plzOnPause = plz.getText().toString();
        ortOnPause = ort.getText().toString();
        strasseOnPause = strasse.getText().toString();
        hausnummerOnPause = hausnummer.getText().toString();
        zeitOnPause = gesetzteZeit.getText().toString();
        datumOnPause = gesetztesDatum.getText().toString();


    }



    public void eventErstellen(View view){




        Event neuesEvent = new Event();

        neuesEvent.setTitel(titel.getText().toString());
        neuesEvent.setMaxTeilnehmer(Integer.parseInt(maxTeilnehmer.getText().toString()));
        neuesEvent.setPLZ(Integer.parseInt(plz.getText().toString()));
        neuesEvent.setOrt(ort.getText().toString());
        neuesEvent.setStrasse(strasse.getText().toString());
        neuesEvent.setHausnummer(Integer.parseInt(hausnummer.getText().toString()));
        neuesEvent.setUhrzeit(gesetzteZeit.getText().toString());
        neuesEvent.setRezeptID(ausgewaehltesRezeptID);
        neuesEvent.setDatum(gesetztesDatum.getText().toString());




        neuesEvent.saveEventually();

        Intent intent = new Intent(KocheventAnbietenActivity.this, MeineEventsActivity.class);
        startActivity(intent);

        Toast.makeText(KocheventAnbietenActivity.this, "Das Kochevent " + titel.getText().toString() + " wurde erfolgreich angelegt. Jetzt fehlen nur noch die Teilnehmer ;)", Toast.LENGTH_LONG).show();




        ausgewaehltesRezeptID = null;

        ausgewaehltesRezeptTitelTextView.setClickable(false);
        ausgewaehltesRezeptTitelTextView.setTextColor(Color.parseColor("#000000"));

        ortOnPause = "";
        plzOnPause = "";
        zeitOnPause = "";
        datumOnPause = "";
        strasseOnPause = "";
        hausnummerOnPause = "";
        maxTeilnehmerOnPause = "";
        titelOnPause = "";


    }

    public void navigateToRezeptsuche(View view){

        Intent intent = new Intent(KocheventAnbietenActivity.this, RezeptSuchenActivity.class);
        startActivity(intent);
    }

    public void editEventDatum(View view){

        DialogFragment dateFragment = new DatePickerFragment();
        dateFragment.show(getFragmentManager(), "datePicker");
    }


    public void editEventUhrzeit(View view){

        DialogFragment timeFragment = new TimePickerFragment();
        timeFragment.show(getFragmentManager(), "timePicker");
    }

    public static void setDate(int tag, int monat, int jahr){


        String neuesDatum = String.format("%02d", tag) + "." + String.format("%02d", monat + 1) + "." + String.format("%02d", jahr);



        gesetztesDatum.setText(neuesDatum);


    }


    public static void setTime(int stunden, int minuten){



        String neueZeit = String.format("%02d", stunden) + ":" + String.format("%02d", minuten);
        gesetzteZeit.setText(neueZeit);
    }


    public void navigateToRezept(View view){

        Intent intent = new Intent(KocheventAnbietenActivity.this, RezeptDetailsActivity.class);
        intent.putExtra("objectId", ausgewaehltesRezeptID);
        startActivity(intent);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_kochevent_anbieten, menu);
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