package com.soco.ebusiness.soco;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rezept_details);

        tit = (EditText) findViewById(R.id.titel_details);
        kat = (EditText) findViewById(R.id.kategorie_details);
        zut = (EditText) findViewById(R.id.zutaten_details);
        zubereitung = (EditText) findViewById(R.id.zubereitungszeit_details);
        rezeptdetails = (TextView) findViewById(R.id.details_rezept);
        final String details_text = rezeptdetails.getText().toString();

        Bundle i = getIntent().getExtras();
        id = i.getString("objectId");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Rezept");
        query.getInBackground(id, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    tit.setText(object.get("titel").toString());
                    kat.setText(object.get("kategorie").toString());
                    zut.setText(object.get("zutaten").toString());
                    zubereitung.setText(object.get("zubereitungszeit").toString());
                    rezeptdetails.setText(details_text + " " + object.get("titel").toString());
                } else {

                }
            }
        });
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
}
