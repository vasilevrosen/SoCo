package com.soco.ebusiness.soco;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class KocheventAnbietenActivity extends FragmentActivity {


    private static EditText gesetztesDatum;
    private static EditText gesetzteZeit;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kochevent_anbieten);
        gesetztesDatum = (EditText) findViewById(R.id.editTextEventDatum);
        gesetzteZeit = (EditText) findViewById(R.id.editTextEventUhrzeit);

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


        String neuesDatum = String.format("%02d", tag) + "." + String.format("%02d", monat) + "." + String.format("%02d", jahr);

        gesetztesDatum.setText(neuesDatum);


    }


    public static void setTime(int stunden, int minuten){



        String neueZeit = String.format("%02d", stunden) + ":" + String.format("%02d", minuten);
        gesetzteZeit.setText(neueZeit);
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
