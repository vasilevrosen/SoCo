package com.soco.ebusiness.soco;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity {

    public ArrayList<String> listOfSharedWord = new ArrayList<String>();
    String x;
    String query;
    ListView lv;
    List<ParseObject> objectsl;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ParseGeoPoint lastEventGPS = new ParseGeoPoint(49.0017191, 8.4183314);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        try{
            setupMarkerListener();
            setupWindowListener();
            ParseUser userObject = ParseUser.getCurrentUser();
            ParseGeoPoint userLocation = (ParseGeoPoint) userObject.get("location");
            if (userLocation == null) {
                try {
                    userLocation = getLastEventGPS();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
//             if(){
//                 query.whereWithinKilometers();
//             }

        query.whereNear("geoPoint", userLocation);
       query.setLimit(10);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    objectsl = objects;
                    for (int i = 0; i < objects.size(); i++) {
                        x = objects.get(i).getString("titel");
                        listOfSharedWord.add(x);
                    }
                    Toast.makeText(getApplicationContext(), "Es wurde(n) " + listOfSharedWord.size() + " Event(s) gefunden", Toast.LENGTH_SHORT).show();

                    // ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MapsActivity.this, android.R.layout.simple_list_item_1, listOfSharedWord);
                    // lv.setAdapter(arrayAdapter);
                    createEventMarker(objects);
                } else {
                    e.getMessage();
                    Toast.makeText(getApplicationContext(), "failed!", Toast.LENGTH_LONG).show();
                }
            }
        });
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
      Location location =  MainActivity.getlastlocation();
        if(location!=null){
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                    .title(getString(R.string.currentPosition)));
        } else{
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(49.0017191, 8.4183314))
                    .title(getString(R.string.currentPosition)));
            mMap.setMyLocationEnabled(true);
           mMap.getMaxZoomLevel();
            mMap.getMyLocation();
            mMap.getUiSettings();
        }

    }
    private void createEventMarker(List<ParseObject> objects){
            for (int i = 0; i < objects.size(); i++) {
                String objectId = objects.get(i).getObjectId();
                double lat = objects.get(i).getParseGeoPoint("geoPoint").getLatitude();
                double lng = objects.get(i).getParseGeoPoint("geoPoint").getLongitude();
                String titel = objects.get(i).getString("Titel");
                String date = objects.get(i).getString("Datum");
                String time = objects.get(i).getString("Uhrzeit");
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat, lng))
                        .title(titel)
                        .snippet(getString(R.string.datetime)+ " "+date+" "+time));

                //    Intent intent = new Intent(MapsActivity.this, EventActivity.class);
                //    intent.putExtra("objectId",objectId);
                //    startActivity(intent);
        }
    }

    public ParseGeoPoint getLastEventGPS() throws ParseException, JSONException {
        try {
        JSONArray UserlastEventId = ParseUser.getCurrentUser().getJSONArray("userEvents");
       String objectid = UserlastEventId.getString((UserlastEventId.length() - 1));
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        query.whereEqualTo("objectid", objectid);
        ParseGeoPoint gpslastevent = query.getFirst().getParseGeoPoint("geoPoint");
            lastEventGPS.setLatitude(gpslastevent.getLatitude());
            lastEventGPS.setLongitude(gpslastevent.getLongitude());
        } catch (ParseException e) {
            Toast.makeText(MapsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.getMessage();
            Toast.makeText(getApplicationContext(), "failed!", Toast.LENGTH_LONG).show();
        }
        return lastEventGPS;
    }
    public boolean onMarkerClick(Marker marker) {
        Log.i("GoogleMapActivity", "onMarkerClick");
        Toast.makeText(getApplicationContext(),
                "Marker Clicked: " + marker.getTitle(), Toast.LENGTH_LONG)
                .show();
        return false;
    }
    private void setupMarkerListener() {
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                moveCamera();
                if (marker.isInfoWindowShown()) {
                    marker.hideInfoWindow();
                } else {
                    marker.showInfoWindow();
                }
                return true;
            }
        });
    }

    private void setupWindowListener() {
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {

                Log.d("", marker.getTitle());
            }
        });
    }

    public boolean onInfoWindowClick(Marker marker) {
        Log.i("GoogleMapActivity", "onMarkerClick");
        Toast.makeText(getApplicationContext(),
                "Marker Clicked: " + marker.getTitle(), Toast.LENGTH_LONG)
                .show();
        return false;
    }

    private void moveCamera() {
        final LatLng NODES = new LatLng(lastEventGPS.getLatitude(), lastEventGPS.getLongitude());
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(NODES)      // Sets the center of the map to Mountain View
                .zoom(17)           // Sets the zoom
                .bearing(90)        // Sets the orientation of the camera to east
                .tilt(30)           // Sets the tilt of the camera to 30 degrees
                .build();           // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
