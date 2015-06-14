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
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class RezeptDetailsActivity extends ActionBarActivity {

    String id;
    String title;
    String kategorie;
    String zutaten;
    String zubereitungszeit;
    EditText tit;
    EditText kat;
    EditText zut;
    EditText zubereitung;
    TextView rezeptdetails;
    Button btnDelete;
    Button btnUpdate;
    ParseObject obj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rezept_details);
        ParseObject.registerSubclass(Rezept.class);

        Bundle i = getIntent().getExtras();
        id = i.getString("objectId");

        tit = (EditText) findViewById(R.id.titel_details);
        kat = (EditText) findViewById(R.id.kategorie_details);
        zut = (EditText) findViewById(R.id.zutaten_details);
        zubereitung = (EditText) findViewById(R.id.zubereitungszeit_details);
        rezeptdetails = (TextView) findViewById(R.id.details_rezept);
        final String details_text = rezeptdetails.getText().toString();
        btnDelete = (Button) findViewById(R.id.btn_delete);
        btnUpdate = (Button) findViewById(R.id.button_speichern_details);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Rezept");
        query.getInBackground(id, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    obj = object;
                    tit.setText(object.get("titel").toString());
                    kat.setText(object.get("kategorie").toString());
                    zut.setText(object.get("zutaten").toString());
                    zubereitung.setText(object.get("zubereitungszeit").toString());
                    rezeptdetails.setText(details_text + " " + object.get("titel").toString());
                } else {

                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {

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
                                                                     "Das Rezept wurde erfolgreich gelöscht", Toast.LENGTH_SHORT)
                                                                     .show();

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
                                         }
                                     }
        );

        btnUpdate.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             obj.put("titel", tit.getText().toString());
                                             obj.put("kategorie", kat.getText().toString());
                                             obj.put("zubereitungszeit", zubereitung.getText().toString());
                                             obj.put("zutaten", zut.getText().toString());
                                             obj.saveEventually();

                                             Toast.makeText(getApplicationContext(), "Das Rezept " + tit.getText().toString() + " wurde erfolgreich gespeichert", Toast.LENGTH_SHORT).show();

                                             Intent intent = new Intent(getApplicationContext(), RezeptActivity.class);
                                             startActivity(intent);
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


        KocheventAnbietenActivity.rezeptFuerKochevent(id, tit.getText().toString());


        Intent intent = new Intent(RezeptDetailsActivity.this, KocheventAnbietenActivity.class);
        startActivity(intent);


    }
}
