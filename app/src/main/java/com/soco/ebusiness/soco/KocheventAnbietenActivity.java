package com.soco.ebusiness.soco;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
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

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SendCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import bolts.Task;


public class KocheventAnbietenActivity extends FragmentActivity implements GPSAdressDialogFragment.NoticeDialogListener{


    private static EditText titel;
    private static EditText maxTeilnehmer;
    private static EditText plz;
    private static EditText strasse;
    private static EditText hausnummer;
    private static EditText ort;
    private static EditText style;


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
    private static String stylOnPause="";

    private static Date datum;

    private static TextView ausgewaehltesRezeptTitelTextView;

    private static String ausgewaehltesRezeptID;

    private static String ausgewaehltesRezeptTitelText = "Bitte Rezept auswählen";

    private static boolean rezeptAusgewaehlt;
    private static String ausgewaehltesRezeptKategorie;

    private static ParseGeoPoint currentGPS = new ParseGeoPoint(0,0);


    public static void rezeptFuerKochevent(String rezeptID, String rezeptTitel, String kategorie){


        ausgewaehltesRezeptTitelText = rezeptTitel;
        ausgewaehltesRezeptID = rezeptID;
        ausgewaehltesRezeptKategorie = kategorie;

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
        style = (EditText) findViewById(R.id.editTextEventStyle);
        maxTeilnehmer = (EditText) findViewById(R.id.editTextMaxTeilnehmerzahl);
        plz= (EditText) findViewById(R.id.editTextEventPLZ);
        strasse = (EditText) findViewById(R.id.editTextEventStraße);
        hausnummer = (EditText) findViewById(R.id.editTextEventNummer);
        ort = (EditText) findViewById(R.id.editTextEventOrt);

        gesetztesDatum = (EditText) findViewById(R.id.editTextEventDatum);
        gesetzteZeit = (EditText) findViewById(R.id.editTextEventUhrzeit);
       showNoticeDialog();
    }



    @Override
    protected void onResume(){


        super.onResume();
        titel.setText(titelOnPause);
        maxTeilnehmer.setText(maxTeilnehmerOnPause);
        ort.setText(ortOnPause);
        plz.setText(plzOnPause);
        strasse.setText(strasseOnPause);
        style.setText(stylOnPause);
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
        style.setText(stylOnPause);
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
        stylOnPause = style.getText().toString();
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
        stylOnPause = style.getText().toString();
        zeitOnPause = gesetzteZeit.getText().toString();
        datumOnPause = gesetztesDatum.getText().toString();


    }



    public void eventErstellen(View view){

        boolean getgps =false;
            plzOnPause = plz.getText().toString();
            ortOnPause = ort.getText().toString();
            strasseOnPause = strasse.getText().toString();
            hausnummerOnPause = hausnummer.getText().toString();
            String address = hausnummerOnPause+ " " +strasseOnPause+" " + ortOnPause + " " + plzOnPause;
            ParseGeoPoint newGPS = MainActivity.convertAddress(KocheventAnbietenActivity.this, address);
            if(newGPS.getLatitude()!=0 || newGPS!=null){
                Toast.makeText(this,getString(R.string.address_found),Toast.LENGTH_LONG).show();
                currentGPS = newGPS;
                getgps=true;

            } else{
                Toast.makeText(this,getString(R.string.no_address_found),Toast.LENGTH_LONG).show();
                getgps=false;
            }

        if(getgps) {

            final Event neuesEvent = new Event();

            neuesEvent.setTitel(titel.getText().toString());
            neuesEvent.setMaxTeilnehmer(Integer.parseInt(maxTeilnehmer.getText().toString()));
            neuesEvent.setPLZ(Integer.parseInt(plz.getText().toString()));
            neuesEvent.setOrt(ort.getText().toString());
            neuesEvent.setStrasse(strasse.getText().toString());
            neuesEvent.setHausnummer(Integer.parseInt(hausnummer.getText().toString()));
            neuesEvent.setstyle(style.getText().toString());
            neuesEvent.setUhrzeit(gesetzteZeit.getText().toString());
            neuesEvent.setRezeptID(ausgewaehltesRezeptID);
            neuesEvent.setDatum(gesetztesDatum.getText().toString());
            neuesEvent.setGEOPoint(currentGPS);

            String neueUserID = ParseUser.getCurrentUser().getObjectId();

            neuesEvent.addNeuerTeilnehmer(neueUserID);

            neuesEvent.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {


                    ParseUser aktuellerUser = ParseUser.getCurrentUser();

                    JSONArray eventsMitAktuellenUser = aktuellerUser.getJSONArray("userEvents");

                    if (eventsMitAktuellenUser == null) {
                        eventsMitAktuellenUser = new JSONArray();
                    }

                    String neuErstelltesEventId = neuesEvent.getObjectId();

                    eventsMitAktuellenUser.put(neuErstelltesEventId);


                    aktuellerUser.put("userEvents", eventsMitAktuellenUser);

                    aktuellerUser.saveEventually();
                    String channel = "Event"+neuErstelltesEventId;
                    ParsePush.subscribeInBackground(channel);
                    LinkedList<String> channels = new LinkedList<>();
                    channels.add("Event" + neuErstelltesEventId);
                    channels.add(neuesEvent.getstyle());
                    channels.add(ausgewaehltesRezeptKategorie);
                    sendPush(channels);

                }
            });


            Intent intent = new Intent(KocheventAnbietenActivity.this, FirstActivity.class);
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
            stylOnPause ="";
            maxTeilnehmerOnPause = "";
            titelOnPause = "";
        } else {
            Toast.makeText(this,getString(R.string.no_address_found),Toast.LENGTH_LONG).show();
        }
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

    public void onclickgps(View view){
        if(MainActivity.getmAdressRequested()){
         String adress =   MainActivity.getmAdressOutput();
            Toast.makeText(this, adress,Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this,getString(R.string.gps_error),Toast.LENGTH_LONG);
        }
    }

    public void sendPush(LinkedList<String> channels){

        ParsePush push = new ParsePush();
        push.setChannels(channels); // Notice we use setChannels not setChannel
        push.setMessage("Neues Event");
        push.sendInBackground();
    }
    private LatLng getLastKnownLocation() {
        LocationManager lm = (LocationManager) App.getAppContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        String provider = lm.getBestProvider(criteria, true);
        if (provider == null) {
            return null;
        }
        Location loc = lm.getLastKnownLocation(provider);
        if (loc != null) {
            LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
            return latLng;
        }
        return null;
    }
    private void creatAdressonGPS() {
        LatLng getlastlocation = getLastKnownLocation();
        Geocoder geoCoder = new Geocoder(
                getBaseContext(), Locale.getDefault());
        if(getlastlocation!=null) {
            try {

                List<Address> addresses = geoCoder.getFromLocation(
                        getlastlocation.latitude,
                        getlastlocation.longitude, 1);


                String add = "";
                if (addresses.size() == 1) {
                    if (addresses.get(0).getPostalCode() != null) {
                        plzOnPause = addresses.get(0).getPostalCode();
                        plz.setText(plzOnPause);
                    }
                    ortOnPause = addresses.get(0).getLocality();
                    ort.setText(ortOnPause);
                    if (addresses.get(0).getThoroughfare() != null) {
                        strasseOnPause = addresses.get(0).getThoroughfare();
                        strasse.setText(strasseOnPause);
                    }
                    if (addresses.get(0).getSubThoroughfare() != null) {
                        hausnummerOnPause = addresses.get(0).getSubThoroughfare();
                        hausnummer.setText(hausnummerOnPause);
                    }
                } else if (addresses.size() > 0) {

                    for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex();
                         i++)
                        add += addresses.get(0).getAddressLine(i) + "\n";

                } else {
                    Toast.makeText(this, getString(R.string.no_address_found), Toast.LENGTH_LONG).show();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        GPSAdressDialogFragment dialog = new GPSAdressDialogFragment();
        dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface


    @Override
    public void onDialogPositiveClick(android.support.v4.app.DialogFragment dialog) {
        creatAdressonGPS();
    }

    @Override
    public void onDialogNegativeClick(android.support.v4.app.DialogFragment dialog) {

    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        creatAdressonGPS();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}