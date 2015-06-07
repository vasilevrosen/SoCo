package com.soco.ebusiness.soco;

        import android.content.Intent;
        import android.content.res.TypedArray;
        import android.os.Bundle;
        import android.view.View;

public class FirstActivity extends MainActivity {
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load
        // titles
        // from
        // strings.xml

        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);// load icons from
        // strings.xml

        set(navMenuTitles, navMenuIcons);
    }

    public void meine_events(View view) {
        if(App.login) {
            Intent intent = new Intent(this, MeineEventsActivity.class);
            startActivity(intent);
        }
    }

    public void create_event(View view) {
        if(App.login) {
            Intent intent = new Intent(this, KocheventAnbietenActivity.class);
            startActivity(intent);
        }
    }
    public void event_map(View view) {
        // Intent intent = new Intent(this, KocheventsuchenActivity.class);
        // startActivity(intent);
    }
}