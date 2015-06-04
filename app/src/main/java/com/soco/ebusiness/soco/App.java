package com.soco.ebusiness.soco;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.SaveCallback;

/**
 * Created by Rosen on 27.05.2015.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Rezept.class);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "r5kFDsKD0dBiQpdiSxSxpQkiIAy1ytBUV1olpd0U", "C8XCXf6LQp2GBYfedTlaDnLFjQWctB7yc0DzZ2L5");

        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });

    }

}