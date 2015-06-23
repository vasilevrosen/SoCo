package com.soco.ebusiness.soco;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.SendButton;
import com.google.zxing.WriterException;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
public class EventActivity extends ListActivity {
    TextView titel;
    TextView beschreibung;
    TextView plzUndOrt;
    TextView datumUndUhrzeit;
    TextView rezeptLink;
    TextView teilnehmerText;
    String ausgewaehltesRezeptID;
    Button btnAnfrage;
    Bitmap img_qrcode;
    Bitmap bmp_qrcode;
    ImageView favoritStern;
    boolean istFavorit;
    private static String id;
    TextView bewerbenHeader;
    boolean nutzerIstErsteller;
    boolean nutzerIstBewerber;

    ListView lv;
    TextView anfragenHeader;
    TextView anzahlAnfragen;


    List<String> alleBewerbernamen;
    TextView listeDerTeilnehmer;

    List<ParseUser> alleBewerber;


    private CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        //Empfehlung für Facebook
        SendButton sendButton = (SendButton)findViewById(R.id.btn_event_empfehlen);
        String msg= getString(R.string.app_name)+":" + titel+" : Wann: "+datumUndUhrzeit;
        Bitmap bmp = bmp_qrcode;
        SharePhotoContent shareContent= MainActivity.publishPhoto(bmp_qrcode,msg);
        sendButton.setShareContent(shareContent);
        callbackManager = CallbackManager.Factory.create();
        sendButton.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onCancel() {
                Log.d("HelloFacebook", "Canceled");
                Toast.makeText(EventActivity.this,getString(R.string.error),Toast.LENGTH_LONG).show();
            }
            @Override
            public void onError(FacebookException error) {
                Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
                String title = getString(R.string.error);
                String alertMessage = error.getMessage();
                showResult(title, alertMessage);
            }
            @Override
            public void onSuccess(Sharer.Result result) {
                Log.d("HelloFacebook", "Success!");
                if (result.getPostId() != null) {
                    String title = getString(R.string.success);
                    String id = result.getPostId();
                    String alertMessage = getString(R.string.successfully_posted_post, id);
                    showResult(title, alertMessage);
                }
            }
            private void showResult(String title, String alertMessage) {
                new AlertDialog.Builder(EventActivity.this)
                        .setTitle(title)
                        .setMessage(alertMessage)
                        .setPositiveButton(R.string.ok, null)
                        .show();
            }
        });
        titel = (TextView) findViewById(R.id.eventTitel);
        beschreibung = (TextView) findViewById(R.id.beschreibungsText);
        plzUndOrt = (TextView) findViewById(R.id.TV_PLZundOrt);
        datumUndUhrzeit = (TextView) findViewById(R.id.TV_UhrzeitUndDatum);
        rezeptLink = (TextView) findViewById(R.id.ausgewählesRezept);
        teilnehmerText = (TextView) findViewById(R.id.TeilnehmerText);
        ImageView img_qrcode = (ImageView) findViewById(R.id.img_qrcode);
        favoritStern = (ImageView) findViewById(R.id.favoritStern);
        istFavorit = false;
        btnAnfrage = (Button) findViewById(R.id.btnAnfrage);
        bewerbenHeader = (TextView) findViewById(R.id.BewerbenHeader);
        listeDerTeilnehmer = (TextView) findViewById(R.id.listeDerTeilnehmer);



        lv = getListView();

        anfragenHeader = (TextView) findViewById(R.id.AnfragenHeader);
        anzahlAnfragen = (TextView) findViewById(R.id.AnzahlAnfragen);

        Bundle i = getIntent().getExtras();
        id = i.getString("objectId");

        try {
            bmp_qrcode = App.generateQrCode("Event-"+id);
            img_qrcode.setImageBitmap(bmp_qrcode);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        ParseObject object = null;

        try {
            object = query.get(id);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

        JSONArray aktuelleTeilnehmer = object.getJSONArray("aktuelleTeilnehmer");


        String erstellerID = null;

        try {
            erstellerID = aktuelleTeilnehmer.getString(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        geterstellerbewerberstatus(erstellerID,object);
        int aktuelleTeilnehmerAnzahl = aktuelleTeilnehmer.length();
        int maxTeilnehmerAnzahl = object.getInt("MaxTeilnehmer");
        String teilnehmerString = "Aktuelle Teilnehmerzahl: " + Integer.toString(aktuelleTeilnehmerAnzahl) + " von "  + Integer.toString(maxTeilnehmerAnzahl);


        if(aktuelleTeilnehmerAnzahl >= maxTeilnehmerAnzahl){
            if(nutzerIstErsteller){
                teilnehmerString += "\nGlückwunsch! Dein Event ist ausgebucht.";
                btnAnfrage.setVisibility(View.INVISIBLE);
                favoritStern.setVisibility(View.INVISIBLE);
            }
            else {
                teilnehmerString += "\nEs hat leider keine Plätze mehr frei. Schau später noch mal vorbei.";
                btnAnfrage.setClickable(false);
            }
        }
        else {
            if(nutzerIstErsteller){
                teilnehmerString += "\nFüge weitere Nutzer dem Event hinzu.";
                btnAnfrage.setVisibility(View.INVISIBLE);
                favoritStern.setVisibility(View.INVISIBLE);
            }
            else {
                teilnehmerString += "\nEs hat für dich noch einen Platz frei";
            }
        }


        teilnehmerText.setText(teilnehmerString);

        JSONArray aktuelleBewerber = object.getJSONArray("Bewerber");

        if(aktuelleBewerber == null){
            aktuelleBewerber = new JSONArray();
        }


        if(nutzerIstErsteller){
            String alleTeilnehmernahmen = "";
            String teilnehmerID = "";
            listeDerTeilnehmer.setVisibility(View.VISIBLE);
            bewerbenHeader.setText("Aktuelle Teilnehmer");
            for(int j = 0; j < aktuelleTeilnehmer.length(); j++){

                try {
                   teilnehmerID = aktuelleTeilnehmer.getString(j);

                   ParseQuery<ParseUser> getUsername = ParseUser.getQuery();
                    getUsername.whereEqualTo("objectId", teilnehmerID);



                    try {
                        ParseUser teilnehmer = getUsername.getFirst();

                        String username = teilnehmer.getString("username");

                        if(username.equals("AnB3opymmRJccwAQ7sVRXCrQg")){
                            username = "Benny Foronkel";
                        }

                        if(username.equals("GNIM4Sx6meHTS54Og1z5pVJ0v")){
                            username = "Steffen Teichman";
                        }

                        alleTeilnehmernahmen += username + "\n";
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            listeDerTeilnehmer.setText(alleTeilnehmernahmen);



            anfragenHeader.setVisibility(View.VISIBLE);
            anzahlAnfragen.setVisibility(View.VISIBLE);
            lv.setVisibility(View.VISIBLE);
            lv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            JSONArray aktuelleAnfragen = object.getJSONArray("Bewerber");

            alleBewerber = new ArrayList<>();
            alleBewerbernamen = new ArrayList<>();

            if(aktuelleAnfragen != null){

                for(int k = 0; k < aktuelleAnfragen.length(); k++){

                    String bewerberID = "";

                    try {
                        bewerberID = aktuelleAnfragen.getString(k);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ParseQuery<ParseUser> getBewerber = ParseUser.getQuery();
                    getBewerber.whereEqualTo("objectId", bewerberID);

                    try {
                        ParseUser neuerBewerber = getBewerber.getFirst();

                        alleBewerber.add(neuerBewerber);
                        String username = neuerBewerber.getString("username");

                        if(username.equals("AnB3opymmRJccwAQ7sVRXCrQg")){
                            username = "Benny Foronkel";
                        }
                        if(username.equals("GNIM4Sx6meHTS54Og1z5pVJ0v")){
                            username = "Steffen Teichman";
                        }

                        alleBewerbernamen.add(username);


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }




            }

            if(alleBewerbernamen.size() > 0) {
                anzahlAnfragen.setText("Derzeit gibt es " + Integer.toString(alleBewerbernamen.size()) + " Anfrage(n)");
            }


            lv.setAdapter(null);

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(EventActivity.this, android.R.layout.simple_list_item_1, alleBewerbernamen);


            lv.setAdapter(arrayAdapter);

        }
        JSONArray userFavoriten=createFavoriten();


        if(userFavoriten != null) {
            for (int j = 0; j < userFavoriten.length(); j++) {
                if (userFavoriten.optString(j).equals(id)) {
                    favoritStern.setImageResource(R.drawable.apptheme_btn_rating_star_on_normal_holo_light);
                    istFavorit = true;
                    break;
                }
            }
        }
        ParseQuery<ParseObject> rezeptQuery = ParseQuery.getQuery("Rezept");
        ParseObject ausgewRezept = null;
        try {
            ausgewRezept = rezeptQuery.get(ausgewaehltesRezeptID);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        rezeptLink.setText(ausgewRezept.get("titel").toString());
    }


    @Override
    public void onResume(){
        super.onResume();

        SendButton sendButton = (SendButton)findViewById(R.id.btn_event_empfehlen);
        String msg= getString(R.string.app_name)+":" + titel+" : Wann: "+datumUndUhrzeit;
        Bitmap bmp = bmp_qrcode;
        SharePhotoContent shareContent= MainActivity.publishPhoto(bmp_qrcode,msg);
        sendButton.setShareContent(shareContent);
        callbackManager = CallbackManager.Factory.create();
        sendButton.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onCancel() {
                Log.d("HelloFacebook", "Canceled");
                Toast.makeText(EventActivity.this,getString(R.string.error),Toast.LENGTH_LONG).show();
            }
            @Override
            public void onError(FacebookException error) {
                Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
                String title = getString(R.string.error);
                String alertMessage = error.getMessage();
                showResult(title, alertMessage);
            }
            @Override
            public void onSuccess(Sharer.Result result) {
                Log.d("HelloFacebook", "Success!");
                if (result.getPostId() != null) {
                    String title = getString(R.string.success);
                    String id = result.getPostId();
                    String alertMessage = getString(R.string.successfully_posted_post, id);
                    showResult(title, alertMessage);
                }
            }
            private void showResult(String title, String alertMessage) {
                new AlertDialog.Builder(EventActivity.this)
                        .setTitle(title)
                        .setMessage(alertMessage)
                        .setPositiveButton(R.string.ok, null)
                        .show();
            }
        });
        titel = (TextView) findViewById(R.id.eventTitel);
        beschreibung = (TextView) findViewById(R.id.beschreibungsText);
        plzUndOrt = (TextView) findViewById(R.id.TV_PLZundOrt);
        datumUndUhrzeit = (TextView) findViewById(R.id.TV_UhrzeitUndDatum);
        rezeptLink = (TextView) findViewById(R.id.ausgewählesRezept);
        teilnehmerText = (TextView) findViewById(R.id.TeilnehmerText);
        ImageView img_qrcode = (ImageView) findViewById(R.id.img_qrcode);
        favoritStern = (ImageView) findViewById(R.id.favoritStern);
        istFavorit = false;
        btnAnfrage = (Button) findViewById(R.id.btnAnfrage);
        bewerbenHeader = (TextView) findViewById(R.id.BewerbenHeader);
        listeDerTeilnehmer = (TextView) findViewById(R.id.listeDerTeilnehmer);


        lv = getListView();
        anfragenHeader = (TextView) findViewById(R.id.AnfragenHeader);
        anzahlAnfragen = (TextView) findViewById(R.id.AnzahlAnfragen);

        Bundle i = getIntent().getExtras();
        id = i.getString("objectId");

        try {
            bmp_qrcode = App.generateQrCode("Event-"+id);
            img_qrcode.setImageBitmap(bmp_qrcode);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        ParseObject object = null;

        try {
            object = query.get(id);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

        JSONArray aktuelleTeilnehmer = object.getJSONArray("aktuelleTeilnehmer");


        String erstellerID = null;

        try {
            erstellerID = aktuelleTeilnehmer.getString(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
       geterstellerbewerberstatus(erstellerID,object);


        int aktuelleTeilnehmerAnzahl = aktuelleTeilnehmer.length();
        int maxTeilnehmerAnzahl = object.getInt("MaxTeilnehmer");
        String teilnehmerString = "Aktuelle Teilnehmerzahl: " + Integer.toString(aktuelleTeilnehmerAnzahl) + " von "  + Integer.toString(maxTeilnehmerAnzahl);


        if(aktuelleTeilnehmerAnzahl >= maxTeilnehmerAnzahl){
            if(nutzerIstErsteller){
                teilnehmerString += "\nGlückwunsch! Dein Event ist ausgebucht.";
                btnAnfrage.setVisibility(View.INVISIBLE);
                favoritStern.setVisibility(View.INVISIBLE);
            }
            else {
                teilnehmerString += "\nEs hat leider keine Plätze mehr frei. Schau später noch mal vorbei.";
                btnAnfrage.setClickable(false);
            }
        }
        else {
            if(nutzerIstErsteller){
                teilnehmerString += "\nFüge weitere Nutzer dem Event hinzu.";
                btnAnfrage.setVisibility(View.INVISIBLE);
                favoritStern.setVisibility(View.INVISIBLE);
            }
            else {
                teilnehmerString += "\nEs hat für dich noch einen Platz frei";
            }
        }


        teilnehmerText.setText(teilnehmerString);

        JSONArray aktuelleBewerber = object.getJSONArray("Bewerber");

        if(aktuelleBewerber == null){
            aktuelleBewerber = new JSONArray();
        }


        if(nutzerIstErsteller){
            String alleTeilnehmernahmen = "";
            String teilnehmerID = "";
            listeDerTeilnehmer.setVisibility(View.VISIBLE);
            bewerbenHeader.setText("Aktuelle Teilnehmer");
            for(int j = 0; j < aktuelleTeilnehmer.length(); j++){

                try {
                    teilnehmerID = aktuelleTeilnehmer.getString(j);

                    ParseQuery<ParseUser> getUsername = ParseUser.getQuery();
                    getUsername.whereEqualTo("objectId", teilnehmerID);



                    try {
                        ParseUser teilnehmer = getUsername.getFirst();

                        String username = teilnehmer.getString("username");

                        if(username.equals("AnB3opymmRJccwAQ7sVRXCrQg")){
                            username = "Benny Foronkel";
                        }
                        if(username.equals("GNIM4Sx6meHTS54Og1z5pVJ0v")){
                            username = "Steffen Teichman";
                        }

                        alleTeilnehmernahmen += username + "\n";
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            listeDerTeilnehmer.setText(alleTeilnehmernahmen);



            anfragenHeader.setVisibility(View.VISIBLE);
            anzahlAnfragen.setVisibility(View.VISIBLE);
            lv.setVisibility(View.VISIBLE);
            lv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            JSONArray aktuelleAnfragen = object.getJSONArray("Bewerber");

            alleBewerber = new ArrayList<>();
            alleBewerbernamen = new ArrayList<>();

            if(aktuelleAnfragen != null){

                for(int k = 0; k < aktuelleAnfragen.length(); k++){

                    String bewerberID = "";

                    try {
                        bewerberID = aktuelleAnfragen.getString(k);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ParseQuery<ParseUser> getBewerber = ParseUser.getQuery();
                    getBewerber.whereEqualTo("objectId", bewerberID);

                    try {
                        ParseUser neuerBewerber = getBewerber.getFirst();

                        alleBewerber.add(neuerBewerber);
                        String username = neuerBewerber.getString("username");

                        if(username.equals("AnB3opymmRJccwAQ7sVRXCrQg")){
                            username = "Benny Foronkel";
                        }
                        if(username.equals("GNIM4Sx6meHTS54Og1z5pVJ0v")){
                            username = "Steffen Teichman";
                        }

                        alleBewerbernamen.add(username);


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }




            }

            if(alleBewerbernamen.size() > 0) {
                anzahlAnfragen.setText("Derzeit gibt es " + Integer.toString(alleBewerbernamen.size()) + " Anfrage(n)");
            }

            lv.setAdapter(null);

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(EventActivity.this, android.R.layout.simple_list_item_1, alleBewerbernamen);

            lv.setAdapter(arrayAdapter);

        }
        JSONArray userFavoriten =createFavoriten();

        if(userFavoriten != null) {
            for (int j = 0; j < userFavoriten.length(); j++) {
                if (userFavoriten.optString(j).equals(id)) {
                    favoritStern.setImageResource(R.drawable.apptheme_btn_rating_star_on_normal_holo_light);
                    istFavorit = true;
                    break;
                }
            }
        }
        ParseQuery<ParseObject> rezeptQuery = ParseQuery.getQuery("Rezept");
        ParseObject ausgewRezept = null;
        try {
            ausgewRezept = rezeptQuery.get(ausgewaehltesRezeptID);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        rezeptLink.setText(ausgewRezept.get("titel").toString());

    }

    @Override
    public void onRestart(){
        super.onRestart();

        SendButton sendButton = (SendButton)findViewById(R.id.btn_event_empfehlen);
        String msg= getString(R.string.app_name)+":" + titel+" : Wann: "+datumUndUhrzeit;
        Bitmap bmp = bmp_qrcode;
        SharePhotoContent shareContent= MainActivity.publishPhoto(bmp_qrcode,msg);
        sendButton.setShareContent(shareContent);
        callbackManager = CallbackManager.Factory.create();
        sendButton.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onCancel() {
                Log.d("HelloFacebook", "Canceled");
                Toast.makeText(EventActivity.this,getString(R.string.error),Toast.LENGTH_LONG).show();
            }
            @Override
            public void onError(FacebookException error) {
                Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
                String title = getString(R.string.error);
                String alertMessage = error.getMessage();
                showResult(title, alertMessage);
            }
            @Override
            public void onSuccess(Sharer.Result result) {
                Log.d("HelloFacebook", "Success!");
                if (result.getPostId() != null) {
                    String title = getString(R.string.success);
                    String id = result.getPostId();
                    String alertMessage = getString(R.string.successfully_posted_post, id);
                    showResult(title, alertMessage);
                }
            }
            private void showResult(String title, String alertMessage) {
                new AlertDialog.Builder(EventActivity.this)
                        .setTitle(title)
                        .setMessage(alertMessage)
                        .setPositiveButton(R.string.ok, null)
                        .show();
            }
        });
        titel = (TextView) findViewById(R.id.eventTitel);
        beschreibung = (TextView) findViewById(R.id.beschreibungsText);
        plzUndOrt = (TextView) findViewById(R.id.TV_PLZundOrt);
        datumUndUhrzeit = (TextView) findViewById(R.id.TV_UhrzeitUndDatum);
        rezeptLink = (TextView) findViewById(R.id.ausgewählesRezept);
        teilnehmerText = (TextView) findViewById(R.id.TeilnehmerText);
        ImageView img_qrcode = (ImageView) findViewById(R.id.img_qrcode);
        favoritStern = (ImageView) findViewById(R.id.favoritStern);
        istFavorit = false;
        btnAnfrage = (Button) findViewById(R.id.btnAnfrage);
        bewerbenHeader = (TextView) findViewById(R.id.BewerbenHeader);
        listeDerTeilnehmer = (TextView) findViewById(R.id.listeDerTeilnehmer);


        lv = getListView();
        anfragenHeader = (TextView) findViewById(R.id.AnfragenHeader);
        anzahlAnfragen = (TextView) findViewById(R.id.AnzahlAnfragen);

        Bundle i = getIntent().getExtras();
        id = i.getString("objectId");

        try {
            bmp_qrcode = App.generateQrCode("Event-"+id);
            img_qrcode.setImageBitmap(bmp_qrcode);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        ParseObject object = null;

        try {
            object = query.get(id);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

        JSONArray aktuelleTeilnehmer = object.getJSONArray("aktuelleTeilnehmer");


        String erstellerID = null;

        try {
            erstellerID = aktuelleTeilnehmer.getString(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        geterstellerbewerberstatus(erstellerID,object);

        int aktuelleTeilnehmerAnzahl = aktuelleTeilnehmer.length();
        int maxTeilnehmerAnzahl = object.getInt("MaxTeilnehmer");
        String teilnehmerString = "Aktuelle Teilnehmerzahl: " + Integer.toString(aktuelleTeilnehmerAnzahl) + " von "  + Integer.toString(maxTeilnehmerAnzahl);


        if(aktuelleTeilnehmerAnzahl >= maxTeilnehmerAnzahl){
            if(nutzerIstErsteller){
                teilnehmerString += "\nGlückwunsch! Dein Event ist ausgebucht.";
                btnAnfrage.setVisibility(View.INVISIBLE);
                favoritStern.setVisibility(View.INVISIBLE);
            }
            else {
                teilnehmerString += "\nEs hat leider keine Plätze mehr frei. Schau später noch mal vorbei.";
                btnAnfrage.setClickable(false);
            }
        }
        else {
            if(nutzerIstErsteller){
                teilnehmerString += "\nFüge weitere Nutzer dem Event hinzu.";
                btnAnfrage.setVisibility(View.INVISIBLE);
                favoritStern.setVisibility(View.INVISIBLE);
            }
            else {
                teilnehmerString += "\nEs hat für dich noch einen Platz frei";
            }
        }


        teilnehmerText.setText(teilnehmerString);


        if(nutzerIstErsteller){
            String alleTeilnehmernahmen = "";
            String teilnehmerID = "";
            listeDerTeilnehmer.setVisibility(View.VISIBLE);
            bewerbenHeader.setText("Aktuelle Teilnehmer");
            for(int j = 0; j < aktuelleTeilnehmer.length(); j++){

                try {
                    teilnehmerID = aktuelleTeilnehmer.getString(j);

                    ParseQuery<ParseUser> getUsername = ParseUser.getQuery();
                    getUsername.whereEqualTo("objectId", teilnehmerID);



                    try {
                        ParseUser teilnehmer = getUsername.getFirst();

                        String username = teilnehmer.getString("username");

                        if(username.equals("AnB3opymmRJccwAQ7sVRXCrQg")){
                            username = "Benny Foronkel";
                        }
                        if(username.equals("GNIM4Sx6meHTS54Og1z5pVJ0v")){
                            username = "Steffen Teichman";
                        }

                        alleTeilnehmernahmen += username + "\n";
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            listeDerTeilnehmer.setText(alleTeilnehmernahmen);



            anfragenHeader.setVisibility(View.VISIBLE);
            anzahlAnfragen.setVisibility(View.VISIBLE);
            lv.setVisibility(View.VISIBLE);
            lv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            JSONArray aktuelleAnfragen = object.getJSONArray("Bewerber");

            alleBewerber = new ArrayList<>();
            alleBewerbernamen = new ArrayList<>();

            if(aktuelleAnfragen != null){

                for(int k = 0; k < aktuelleAnfragen.length(); k++){

                    String bewerberID = "";

                    try {
                        bewerberID = aktuelleAnfragen.getString(k);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ParseQuery<ParseUser> getBewerber = ParseUser.getQuery();
                    getBewerber.whereEqualTo("objectId", bewerberID);

                    try {
                        ParseUser neuerBewerber = getBewerber.getFirst();

                        alleBewerber.add(neuerBewerber);
                        String username = neuerBewerber.getString("username");

                        if(username.equals("AnB3opymmRJccwAQ7sVRXCrQg")){
                            username = "Benny Foronkel";
                        }
                        if(username.equals("GNIM4Sx6meHTS54Og1z5pVJ0v")){
                            username = "Steffen Teichman";
                        }

                        alleBewerbernamen.add(username);


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }




            }

            if(alleBewerbernamen.size() > 0) {
                anzahlAnfragen.setText("Derzeit gibt es " + Integer.toString(alleBewerbernamen.size()) + " Anfrage(n)");
            }

            lv.setAdapter(null);

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(EventActivity.this, android.R.layout.simple_list_item_1, alleBewerbernamen);

            lv.setAdapter(arrayAdapter);

        }
        JSONArray userFavoriten = createFavoriten();


        if(userFavoriten != null) {
            for (int j = 0; j < userFavoriten.length(); j++) {
                if (userFavoriten.optString(j).equals(id)) {
                    favoritStern.setImageResource(R.drawable.apptheme_btn_rating_star_on_normal_holo_light);
                    istFavorit = true;
                    break;
                }
            }
        }
        ParseQuery<ParseObject> rezeptQuery = ParseQuery.getQuery("Rezept");
        ParseObject ausgewRezept = null;
        try {
            ausgewRezept = rezeptQuery.get(ausgewaehltesRezeptID);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        rezeptLink.setText(ausgewRezept.get("titel").toString());

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

    public void anfrageStarten(View view){
        if ( !App.get_loginstate()) {
            openLogin();
        } else {

            ParseQuery<ParseObject> getEvent = new ParseQuery<ParseObject>("Event");
            getEvent.whereEqualTo("objectId", id);

            ParseObject aktuellesEvent = null;

            try {
                aktuellesEvent = getEvent.getFirst();


            } catch (ParseException e) {
                e.printStackTrace();
            }


            JSONArray aktuelleBewerber = aktuellesEvent.getJSONArray("Bewerber");

            if (aktuelleBewerber == null) {
                aktuelleBewerber = new JSONArray();
            }


            if (!nutzerIstBewerber) {

                aktuelleBewerber.put(ParseUser.getCurrentUser().getObjectId());
                nutzerIstBewerber = true;
                btnAnfrage.setText("Anfrage zurückziehen");
            } else {

                int index = 0;

                for (int i = 0; i < aktuelleBewerber.length(); i++) {

                    try {
                        if (aktuelleBewerber.getString(i).equals(ParseUser.getCurrentUser().getObjectId())) {
                            index = i;
                            break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                aktuelleBewerber.remove(index);
                btnAnfrage.setText("Anfrage senden");
                nutzerIstBewerber = false;

            }

            aktuellesEvent.put("Bewerber", aktuelleBewerber);
            try {
                aktuellesEvent.save();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void onClickFavorit(View view) {
        if (!App.get_loginstate()) {
            openLogin();
        } else {
            ParseUser aktuellerUser = ParseUser.getCurrentUser();
            JSONArray userFavoriten = createFavoriten();
            if (userFavoriten == null) {
                userFavoriten = new JSONArray();
            }
            if (istFavorit) {
                int index = 0;
                for (int i = 0; i < userFavoriten.length(); i++) {
                    if (userFavoriten.optString(i).equals(id)) {
                        index = i;
                        break;
                    }
                }
                userFavoriten.remove(index);
                favoritStern.setImageResource(R.drawable.apptheme_btn_rating_star_off_normal_holo_light);
                istFavorit = false;
            } else {
                userFavoriten.put(id);
                favoritStern.setImageResource(R.drawable.apptheme_btn_rating_star_on_normal_holo_light);
                istFavorit = true;
            }
            aktuellerUser.put("userFavoriten", userFavoriten);
            aktuellerUser.saveEventually();
        }
    }

    public void onclicksaveqr(View view) {
        App.saveqrcode(this, bmp_qrcode);
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id2) {
        super.onListItemClick(l, v, position, id2);

        String objectId = alleBewerber.get(position).getObjectId();

        Intent intent = new Intent(EventActivity.this, BewerberCheckActivity.class);
        intent.putExtra("objectId", objectId);
        intent.putExtra("eventId", id);
        startActivity(intent);


    }

    public void geterstellerbewerberstatus(String erstellerID, ParseObject object) {
        try {
            if (erstellerID.equals(ParseUser.getCurrentUser().getObjectId())) {
                nutzerIstErsteller = true;
            } else {
                nutzerIstErsteller = false;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            nutzerIstErsteller = false;

        }
        JSONArray aktuelleBewerber = object.getJSONArray("Bewerber");
        if (aktuelleBewerber == null) {
            aktuelleBewerber = new JSONArray();
        }
        try {
            if (aktuelleBewerber.toString().contains(ParseUser.getCurrentUser().getObjectId())) {

                nutzerIstBewerber = true;
                btnAnfrage.setText("Anfrage zurückziehen");
            } else {
                nutzerIstBewerber = false;

            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            nutzerIstBewerber = false;
        }
    }

    public JSONArray createFavoriten() {
        JSONArray userFavoriten = null;
        try {
            userFavoriten = ParseUser.getCurrentUser().getJSONArray("userFavoriten");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return userFavoriten;
    }

    public void openLogin() {
        Intent intent1 = new Intent(this, LoginActivity.class);
        startActivity(intent1);
    }
}
