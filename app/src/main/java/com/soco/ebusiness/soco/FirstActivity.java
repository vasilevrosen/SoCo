package com.soco.ebusiness.soco;

        import android.content.Intent;
        import android.content.res.TypedArray;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.EditText;

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

    public void meine_teilnahmen(View view) {
        if(App.get_loginstate()) {
            Intent intent = new Intent(this, MeineTeilnahmenListActivity.class);
            startActivity(intent);
        }
        else sendToast(getString(R.string.msg_loginmissing));
    }

    public void meine_favoriten(View view) {
        if(App.get_loginstate()) {
            Intent intent = new Intent(this, MeineFavoritenListActivity.class);
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
         Intent intent = new Intent(this, SlideMapActivity.class);
         startActivity(intent);
    }

    public void navigateToEventSuche(View view){

        String gesuchterOrt = ((EditText) findViewById(R.id.OrtText)).getText().toString();


        Intent intent = new Intent(FirstActivity.this, EventListe.class);

        intent.putExtra("Ort", gesuchterOrt);
        startActivity(intent);


    }
}