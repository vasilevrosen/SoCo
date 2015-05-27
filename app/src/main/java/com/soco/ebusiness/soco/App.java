package com.soco.ebusiness.soco;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

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
    }

}