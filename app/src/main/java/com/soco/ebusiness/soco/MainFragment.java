/**
 * Copyright 2015-present Amberfog
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

package com.soco.ebusiness.soco;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import com.google.android.gms.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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

public class MainFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        SlidingUpPanelLayout.PanelSlideListener, LocationListener {


    private static final String ARG_LOCATION = "arg.location";

    private LockableListView mListView;
    private SlidingUpPanelLayout mSlidingUpPanelLayout;

    private View mTransparentHeaderView;
    private View mTransparentView;
    private View mSpaceView;

    private LatLng mLocation;
    private Marker mLocationMarker;

    private SupportMapFragment mMapFragment;

    private boolean mIsNeedLocationUpdate = true;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    public ArrayList<String> listOfSharedWord = new ArrayList<String>();
    String x;
    String y;
    List<ParseObject> objectsl;
    public ArrayList<String> eventids = new ArrayList<String>();

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ParseGeoPoint lastEventGPS = new ParseGeoPoint(49.0017191, 8.4183314);
    ParseQuery<ParseObject> eventquery;
    public MainFragment() {
    }

    public static MainFragment newInstance(LatLng location) {
        MainFragment f = new MainFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_LOCATION, location);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_slide, container, false);

        mListView = (LockableListView) rootView.findViewById(android.R.id.list);
        mListView.setOverScrollMode(ListView.OVER_SCROLL_NEVER);

        mSlidingUpPanelLayout = (SlidingUpPanelLayout) rootView.findViewById(R.id.slidingLayout);
        mSlidingUpPanelLayout.setEnableDragViewTouchEvents(true);

        int mapHeight = getResources().getDimensionPixelSize(R.dimen.map_height);
        mSlidingUpPanelLayout.setPanelHeight(mapHeight); // you can use different height here
        mSlidingUpPanelLayout.setScrollableView(mListView, mapHeight);

        mSlidingUpPanelLayout.setPanelSlideListener(this);

        // transparent view at the top of ListView
        mTransparentView = rootView.findViewById(R.id.transparentView);

        // init header view for ListView
        mTransparentHeaderView = inflater.inflate(R.layout.transparent_header_view, mListView, false);
        mSpaceView = mTransparentHeaderView.findViewById(R.id.space);

        collapseMap();

        mSlidingUpPanelLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mSlidingUpPanelLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mSlidingUpPanelLayout.onPanelDragged(0);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        mLocation = getArguments().getParcelable(ARG_LOCATION);
        if (mLocation == null) {
            mLocation = getLastKnownLocation(false);
        }

        mMapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mapContainer, mMapFragment, "map");
        fragmentTransaction.commit();


        //CurrentLocation
        ParseUser userObject = ParseUser.getCurrentUser();
        ParseGeoPoint userLocation = (ParseGeoPoint) userObject.get("location");
        try {
            setUpMapIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (userLocation == null) {
            try {
                userLocation = getLastEventGPS();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //EVENT MARKER
             eventquery = ParseQuery.getQuery("Event");
            // if(){
            //     query.whereWithinKilometers();
            // }

            eventquery.whereNear("geoPoint", userLocation);
            eventquery.setLimit(10);
            eventquery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        objectsl = objects;
                        listOfSharedWord.clear();
                        for (int i = 0; i < objects.size(); i++) {
                            x = objects.get(i).getString("Titel");
                            y = objects.get(i).getObjectId();
                            eventids.add(y);
                            listOfSharedWord.add(x);
                        }
                        Toast.makeText(App.getAppContext(), "Es wurde(n) " + listOfSharedWord.size() + " Event(s) gefunden", Toast.LENGTH_SHORT).show();

                        createEventMarker(objects);
                    } else {
                        e.getMessage();
                        Toast.makeText(App.getAppContext(), "failed!", Toast.LENGTH_LONG).show();
                    }
                }
            });
            mListView.addHeaderView(mTransparentHeaderView);
            mListView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.simple_list_item, listOfSharedWord));
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ParseGeoPoint eventgps = new ParseGeoPoint(0, 0);
                    mSlidingUpPanelLayout.collapsePane();
                    if (id >= 0) {
                        int ids = (int) id;
                        String objectId = eventids.get(ids);
                        try {
                            eventgps = eventquery.get(objectId).getParseGeoPoint("geoPoint");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        CameraUpdate center =
                                CameraUpdateFactory.newLatLng(new LatLng(eventgps.getLatitude(),
                                        eventgps.getLongitude()));
                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(5);

                        mMap.moveCamera(center);
                        mMap.animateCamera(zoom);
                        setupWindowListener();
                    }
                }
            });

            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();

        }
    }

    private void setUpMapIfNeeded() throws ParseException, JSONException {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = mMapFragment.getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setCompassEnabled(false);
                mMap.getUiSettings().setZoomControlsEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                LatLng update = getLastKnownLocation();
                if (update != null) {
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(update, 11.0f)));
                }
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        mIsNeedLocationUpdate = false;
                        moveToLocation(latLng, false);
                    }
                });
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // In case Google Play services has since become available.
        try {
            setUpMapIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Connect the client.
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        // Disconnecting the client invalidates it.
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private LatLng getLastKnownLocation() {
        return getLastKnownLocation(true);
    }

    private LatLng getLastKnownLocation(boolean isMoveMarker) {
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
            if (isMoveMarker) {
                moveMarker(latLng);
            }
            return latLng;
        }
        return null;
    }

    private void moveMarker(LatLng latLng) {
        if (mLocationMarker != null) {
            mLocationMarker.remove();
        }
        mLocationMarker = mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_mylocation))
                .position(latLng).anchor(0.5f, 0.5f));
    }

    private void moveToLocation(Location location) {
        if (location == null) {
            return;
        }
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        moveToLocation(latLng);
    }

    private void moveToLocation(LatLng latLng) {
        moveToLocation(latLng, true);
    }

    private void moveToLocation(LatLng latLng, final boolean moveCamera) {
        if (latLng == null) {
            return;
        }
        moveMarker(latLng);
        mLocation = latLng;
        mListView.post(new Runnable() {
            @Override
            public void run() {
                if (mMap != null && moveCamera) {
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(mLocation, 11.0f)));
                }
            }
        });
    }

    private void collapseMap() {
        mSpaceView.setVisibility(View.VISIBLE);
        mTransparentView.setVisibility(View.GONE);
        if (mMap != null && mLocation != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLocation, 11f), 1000, null);
        }
        mListView.setScrollingEnabled(true);
    }

    private void expandMap() {
        mSpaceView.setVisibility(View.GONE);
        mTransparentView.setVisibility(View.INVISIBLE);
        if (mMap != null) {
            mMap.animateCamera(CameraUpdateFactory.zoomTo(14f), 1000, null);
        }
        mListView.setScrollingEnabled(false);
    }

    @Override
    public void onPanelSlide(View view, float v) {
    }

    @Override
    public void onPanelCollapsed(View view) {
        expandMap();
    }

    @Override
    public void onPanelExpanded(View view) {
        collapseMap();
    }

    @Override
    public void onPanelAnchored(View view) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (mIsNeedLocationUpdate) {
            moveToLocation(location);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setNumUpdates(1);

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

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
            e.getMessage();
        } catch (JSONException e) {
            e.getMessage();

        }
        return lastEventGPS;
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
                    .snippet(getString(R.string.datetime) + " " + date + " " + time + "::" + objectId));

            //    Intent intent = new Intent(MapsActivity.this, EventActivity.class);
            //    intent.putExtra("objectId",objectId);
            //    startActivity(intent);
        }
    }
    private void setupWindowListener() {
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {
                String snippet = marker.getSnippet();
                String[] snippets = snippet.split("::");
                String eventid = snippets[1];
                Intent intent = new Intent(App.getAppContext(), EventActivity.class);
                intent.putExtra("objectId", eventid);
                startActivity(intent);
            }
        });
    }
}