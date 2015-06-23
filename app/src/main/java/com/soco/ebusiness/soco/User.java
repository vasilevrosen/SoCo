package com.soco.ebusiness.soco;

/**
 * Created by benny on 09.06.15.
 */

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("User")
public class User  extends ParseUser{

    public User(){

    }

    public void setRadius(int titel){

        put("Radius", titel);
    }

    public int getRadius()

    {
        return getInt("Radius");

    }



}
