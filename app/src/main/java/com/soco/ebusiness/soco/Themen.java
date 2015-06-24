package com.soco.ebusiness.soco;

import android.net.Uri;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Rosen on 27.05.2015.
 * Object Thema für die WissensDB
 */

@ParseClassName("Themen")
public class Themen extends ParseObject {
    public Themen() {
    }

    public String getTitle() {
        return getString("titel");
    }

    public void setTitle(String title) {
        put("titel", title);
    }

    public String getURI(){
        return getString("URI");
    }
    public void setURI(String URI ) {
        put("URI",URI);

    }

}
