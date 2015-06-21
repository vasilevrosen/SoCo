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

    public void meine_veranstaltungen(View view) {
        if(App.get_loginstate()) {
            Intent intent = new Intent(this, MeineVeranstaltungenListActivity.class);
            startActivity(intent);
        }
        else sendToast(getString(R.string.msg_loginmissing));
    }

    public void create_event(View view) {
        if(App.get_loginstate()) {
            Intent intent = new Intent(this, KocheventAnbietenActivity.class);
            startActivity(intent);
        }else sendToast(getString(R.string.msg_loginmissing));
    }
    public void event_map(View view) {
        // Intent intent = new Intent(this, KocheventsuchenActivity.class);
        // startActivity(intent);
    }
}