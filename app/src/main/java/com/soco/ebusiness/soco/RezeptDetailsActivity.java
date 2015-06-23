package com.soco.ebusiness.soco;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class RezeptDetailsActivity extends ActionBarActivity {

    String id;
    String title;
    String kategorie;
    String zutaten;
    String zubereitungszeit;
    String vorbereitung;
    EditText et_title;
    EditText et_kategorie;
    EditText et_zutaten;
    EditText et_zubereitung;
    EditText et_vorbereitung;
    TextView tv_rezeptdetails;
    TextView tv_title;
    TextView tv_kategorie;
    TextView tv_zutaten;
    TextView tv_zubereitungszeit;
    TextView tv_vorbereitung;
    Button btnDelete;
    Button btnUpdate;
    ParseObject obj;
    String authorId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rezept_details);
        ParseObject.registerSubclass(Rezept.class);
        final ParseUser currentUser = ParseUser.getCurrentUser();
        Bundle i = getIntent().getExtras();
        id = i.getString("objectId");

        et_title = (EditText) findViewById(R.id.titel_details);
        et_kategorie = (EditText) findViewById(R.id.kategorie_details);
        et_zutaten = (EditText) findViewById(R.id.zutaten_details);
        et_zubereitung = (EditText) findViewById(R.id.zubereitungszeit_details);
        tv_rezeptdetails = (TextView) findViewById(R.id.details_rezept);

        final String details_text = tv_rezeptdetails.getText().toString();
        btnDelete = (Button) findViewById(R.id.btn_delete);
        btnUpdate = (Button) findViewById(R.id.button_speichern_details);
        tv_title = (TextView) findViewById(R.id.details_titel);
        tv_kategorie = (TextView) findViewById(R.id.details_rezeptkategorie);
        tv_vorbereitung = (TextView) findViewById(R.id.details_vorbereitung);
        tv_zubereitungszeit = (TextView) findViewById(R.id.details_zubereitungszeit);
        tv_zutaten = (TextView) findViewById(R.id.details_zutaten);
        et_vorbereitung = (EditText) findViewById(R.id.et_details_vorbereitung);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Rezept");
        query.getInBackground(id, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    obj = object;
                    et_title.setText(object.get("titel").toString());
                    kategorie = object.get("kategorie").toString();
                    et_kategorie.setText(kategorie);
                    et_zutaten.setText(object.get("zutaten").toString());
                    et_vorbereitung.setText(object.get("vorbereitung").toString());
                    et_zubereitung.setText(object.get("zubereitungszeit").toString());
                    tv_rezeptdetails.setText(details_text + " " + object.get("titel").toString());
                    authorId = object.get("userId").toString();
                } else {

                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(final View view) {
                                             if (authorId.matches(currentUser.getObjectId().toString())) {

                                                 AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                                                         RezeptDetailsActivity.this);

                                                 alertDialog2.setTitle("Confirm delete");
                                                 alertDialog2.setMessage("Rezept delete?");

                                                 //          alertDialog2.setIcon(R.drawable.delete);

                                                 alertDialog2.setPositiveButton("Delete",
                                                         new DialogInterface.OnClickListener() {
                                                             public void onClick(DialogInterface dialog, int which) {

                                                                 obj.deleteInBackground();
                                                                 Toast.makeText(getApplicationContext(),
                                                                         "Das Rezept wurde erfolgreich geloescht", Toast.LENGTH_SHORT)
                                                                         .show();
                                                                 obj.put("zubereitungszeit", et_zubereitung.getText().toString());

                                                                 Intent intent = new Intent(getApplicationContext(), RezeptActivity.class);

                                                                 startActivity(intent);
                                                             }
                                                         });
                                                 alertDialog2.setNegativeButton("Abbrechen",
                                                         new DialogInterface.OnClickListener() {
                                                             public void onClick(DialogInterface dialog, int which) {
                                                                 dialog.cancel();
                                                             }
                                                         });

                                                 alertDialog2.show();
                                             } else {
                                                 Toast.makeText(getApplicationContext(), "Sie duerfen nur Ihre Rezepte loeschen", Toast.LENGTH_LONG).show();
                                             }
                                         }
                                     }
        );

        btnUpdate.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             if (authorId.matches(currentUser.getObjectId().toString())) {
                                                 obj.put("titel", et_title.getText().toString());
                                                 obj.put("kategorie", et_kategorie.getText().toString());
                                                 obj.put("zutaten", et_zutaten.getText().toString());
                                                 obj.put("zubereitungszeit", et_zubereitung.getText().toString());
                                                 obj.put("vorbereitung", et_vorbereitung.getText().toString());
                                                 obj.saveEventually();

                                                 Toast.makeText(getApplicationContext(), "Das Rezept " + et_title.getText().toString() + " wurde erfolgreich gespeichert", Toast.LENGTH_SHORT).show();

                                                 Intent intent = new Intent(getApplicationContext(), RezeptActivity.class);
                                                 startActivity(intent);
                                             }
                                             else {
                                                 Toast.makeText(getApplicationContext(), "Sie duerfen nur Ihre Rezepte aendern.", Toast.LENGTH_LONG).show();
                                             }
                                         }
                                     }
        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rezept_details, menu);
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


    public void rezeptAuswahlKochevent(View view) {


        KocheventAnbietenActivity.rezeptFuerKochevent(id, et_title.getText().toString(),kategorie);


        Intent intent = new Intent(RezeptDetailsActivity.this, KocheventAnbietenActivity.class);
        startActivity(intent);


    }
    public void pushSubscribe(View view){
        String channel = kategorie;
            ParsePush.subscribeInBackground(channel);
        }
}
