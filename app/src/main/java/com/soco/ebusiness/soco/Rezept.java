package com.soco.ebusiness.soco;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Rosen on 27.05.2015.
 */

@ParseClassName("Rezept")
public class Rezept extends ParseObject {
    public Rezept() {
    }

    public String getTitle() {
        return getString("titel");
    }

    public void setTitle(String title) {
        put("titel", title);
    }

    public String getKategorie() {
        return getString("kategorie");
    }

    public void setKategorie(String kategorie) {
        put("kategorie", kategorie);
    }

    public String getZutaten() {
        return getString("zutaten");
    }

    public void setZutaten(String zutaten) {
        put("zutaten", zutaten);
    }

    public String getZubereitungszeit() {
        return getString("zubereitungszeit");
    }

    public void setZubereitungszeit(String zubereitungszeit) {
        put("zubereitungszeit", zubereitungszeit);
    }

    public String getVorbereitung() {
        return getString("vorbereitung");
    }

    public void setVorbereitung(String vorbereitung) {
        put("vorbereitung", vorbereitung);
    }

    public String getUserId() {
        return getString("userId");
    }

    public void setUserId(String userId) {
        put("userId", userId);
    }

}
