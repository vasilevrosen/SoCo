package com.soco.ebusiness.soco;


import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;


import java.util.Date;


/**
 * Created by benny on 05.06.15.
 */

@ParseClassName("Event")
public class Event extends ParseObject{

    public Event(){

        addNeuerTeilnehmer();
    }

    public void setTitel(String titel){

        put("Titel", titel);
    }

    public String getTitel()

    {
        return getString("Titel");

    }

    public void setMaxTeilnehmer(int maxTeilnehmer) {

        put("MaxTeilnehmer", maxTeilnehmer);
    }


    public int getMaxTeilnehmer() {

        return getInt("MaxTeilnehmer");
    }

    public void addNeuerTeilnehmer(){


        JSONArray aktuelleTeilnehmer = getAktuelleTeilnehmer();

        if(aktuelleTeilnehmer == null){
            aktuelleTeilnehmer = new JSONArray();
        }

        String aktuelleUserId = ParseUser.getCurrentUser().getObjectId();

        aktuelleTeilnehmer.put(aktuelleUserId);

        put("aktuelleTeilnehmer", aktuelleTeilnehmer);
    }

    public JSONArray getAktuelleTeilnehmer(){

        return getJSONArray("aktuelleTeilnehmer");
    }

    public void setPLZ(int plz){

        put("PLZ", plz);
    }

    public int getPLZ(){

        return  getInt("PLZ");
    }


    public void setOrt(String ort){

        put("Ort", ort);
    }

    public String getOrt(){

        return getString("Ort");
    }

    public void setStrasse(String strasse){

        put("Strasse", strasse);
    }

    public String getStrasse(){

        return getString("Strasse");
    }

    public void setHausnummer(int hausnummer){

        put("Hausnummer", hausnummer);
    }

    public int getHausnummer(){

        return getInt("Hausnummer");
    }

    public void setDatum(String datum){

        put("Datum", datum);
    }

    public String getDatum(){

        return getString("Datum");
    }

    public void setUhrzeit(String uhrzeit){

        put("Uhrzeit", uhrzeit);
    }

    public String getUhrzeit(){

        return getString("Uhrzeit");
    }

    public String getRezeptID(){

        return getString("Rezept_ID");
    }

    public void setRezeptID(String rezeptID){

        put("Rezept_ID", rezeptID);
    }

}