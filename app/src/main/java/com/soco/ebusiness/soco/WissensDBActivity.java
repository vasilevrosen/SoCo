package com.soco.ebusiness.soco;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

import java.util.ArrayList;
import java.util.List;

public class WissensDBActivity extends ListActivity {

    public ArrayList<String> listOfSharedWord = new ArrayList<>();
    String x;
    String query;
    ListView lv;
    List<Themen> objectsl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_rezept_liste);

        ParseObject.registerSubclass(Themen.class);
        lv = getListView();


        ParseQuery<Themen> query = new ParseQuery<>("Themen");
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Themen>() {
            @Override
            public void done(List<Themen> objects, ParseException e) {
                if (e == null) {
                    objectsl = objects;
                    for (int i = 0; i < objects.size(); i++) {
                        x = objects.get(i).getString("titel");
                        listOfSharedWord.add(x);
                    }
                    Toast.makeText(getApplicationContext(), "Es wurde(n) " + listOfSharedWord.size() + " Themen gefunden", Toast.LENGTH_SHORT).show();

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(WissensDBActivity.this, android.R.layout.simple_list_item_1, listOfSharedWord);
                    lv.setAdapter(arrayAdapter);
                } else {
                    e.getMessage();
                    Toast.makeText(getApplicationContext(), "failed!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wissens_db, menu);
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

        String objectId = objectsl.get(position).getObjectId();
        String uri = objectsl.get(position).getURI();

        Intent intent = new Intent(WissensDBActivity.this, activity_wiki_article.class);
        intent.putExtra("URI",uri);
        startActivity(intent);

    }
    @Override
    public void onBackPressed() {
        //Display alert message when back button has been pressed
        Intent intent = new Intent(this, FirstActivity.class);
        startActivity(intent);
    }

}
