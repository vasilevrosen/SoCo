package com.soco.ebusiness.soco;

/**
 * Created by steffenteichmann on 22.06.15.
 * SlideMapActivity für die erstellung der Karte mit SlideListe
 */


    /**
     * Copyright 2014-present Amberfog
     * <p/>
     * Licensed under the Apache License, Version 2.0 (the "License");
     * you may not use this file except in compliance with the License.
     * You may obtain a copy of the License at
     * <p/>
     * http://www.apache.org/licenses/LICENSE-2.0
     * <p/>
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */


    import android.app.DialogFragment;
    import android.content.Context;
    import android.content.Intent;
    import android.location.Location;
    import android.location.LocationManager;
    import android.os.Bundle;
    import android.support.v4.app.FragmentTransaction;
    import android.support.v7.app.AppCompatActivity;
    import android.view.View;
    import android.widget.ProgressBar;

    import com.google.android.gms.maps.model.LatLng;
    import com.parse.ParseException;
    import com.parse.ParseGeoPoint;
    import com.parse.ParseObject;
    import com.parse.ParseQuery;
    import com.parse.ParseUser;

    import org.json.JSONArray;
    import org.json.JSONException;


public class SlideMapActivity  extends AppCompatActivity implements NOGPSDialogFragment.NoticeDialogListener {

    private boolean locationstatus;
    private ParseGeoPoint lastEventGPS = new ParseGeoPoint(49.0017191, 8.4183314);

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Bundle i = getIntent().getExtras();
            if(i!=null) {
                locationstatus = i.getBoolean("locationstatus");
            }
           Location location = MainActivity.getlastlocation();
            if(location == null && !locationstatus){
                showNoticeDialog();
            } else if (location != null && !locationstatus){
                locationstatus =true;
            }
            setContentView(R.layout.map_slide);
            if (savedInstanceState == null) {
                FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
                trans.add(R.id.fragment, MainFragment.newInstance(null));
                trans.commit();
            }

        }
    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        NOGPSDialogFragment dialog = new NOGPSDialogFragment();
        dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
    }
    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface


    @Override
    public void onDialogPositiveClick(android.support.v4.app.DialogFragment dialog) {
        locationstatus = true;
    }

    @Override
    public void onDialogNegativeClick(android.support.v4.app.DialogFragment dialog) {
        locationstatus = false;
        //Display alert message when back button has been pressed
        Intent intent = new Intent(this, FirstActivity.class);
        startActivity(intent);

    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {


    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
    public boolean getlocationstatus(){
        return locationstatus;
    }


}
