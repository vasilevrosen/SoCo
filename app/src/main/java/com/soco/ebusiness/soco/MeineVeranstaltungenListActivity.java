package com.soco.ebusiness.soco;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MeineVeranstaltungenListActivity extends ListActivity {

    ListView lv;
    List<ParseObject> objectlist;

    List<String> alleUserEventIdStr;
    List<String> alleUserEventsTitel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meine_veranstaltungen_list);

        ParseObject.registerSubclass(Event.class);
        alleUserEventIdStr = new ArrayList<String>();
        alleUserEventsTitel = new ArrayList<String>();

        objectlist = new ArrayList<ParseObject>();


        lv = getListView();



        JSONArray alleUserEventId = ParseUser.getCurrentUser().getJSONArray("userEvents");

        if(alleUserEventId != null) {
            for (int i = 0; i < alleUserEventId.length(); i++) {

                alleUserEventIdStr.add(alleUserEventId.optString(i));

            }
        }


        for(String s: alleUserEventIdStr){

            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Event");

            query.whereEqualTo("objectId", s);

            ParseObject po = null;

            try {
                po = query.getFirst();

            } catch (ParseException e) {
                Toast.makeText(MeineVeranstaltungenListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            objectlist.add(po);
            alleUserEventsTitel.add(po.getString("Titel"));






        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MeineVeranstaltungenListActivity.this, android.R.layout.simple_list_item_1, alleUserEventsTitel);

        lv.setAdapter(arrayAdapter);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_meine_veranstaltungen_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String objectId = objectlist.get(position).getObjectId();

        Intent intent = new Intent(MeineVeranstaltungenListActivity.this, EventActivity.class);
        intent.putExtra("objectId",objectId);
        startActivity(intent);



    }


}
