package com.soco.ebusiness.soco;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    Bitmap img_qrcode;
    Bitmap bmp_qrcode;

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


        btnAnfrage = (Button) findViewById(R.id.btnAnfrage);

        Bundle i = getIntent().getExtras();
        String id = i.getString("objectId");
        try {
             bmp_qrcode = App.generateQrCode("Event-"+id);
            img_qrcode.setImageBitmap(bmp_qrcode);
        } catch (WriterException e) {
            e.printStackTrace();
        }

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
    public void onclicksaveqr(View view){
        App.saveqrcode(this, bmp_qrcode);
    }
}
