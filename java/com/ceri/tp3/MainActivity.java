package com.ceri.tp3;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.design.widget.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    static final public int ADD_TEAM_REQUEST = 1000;
    final private SportDbHelper dbHelper = new SportDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        registerForContextMenu(findViewById(R.id.lvTeams));

        dbHelper.populate();

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_2,
                dbHelper.fetchAllTeams(),
                new String[]{SportDbHelper.COLUMN_TEAM_NAME, SportDbHelper.COLUMN_LEAGUE_NAME},
                new int[]{android.R.id.text1, android.R.id.text2}
                );

        ListView listView = (ListView) findViewById(R.id.lvTeams);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Team team = dbHelper.cursorToTeam((Cursor) parent.getItemAtPosition(position));
//                System.out.println(team);

                Intent intent = new Intent(MainActivity.this, TeamActivity.class);
                intent.putExtra(Team.TAG, team);
                startActivity(intent);
            }
        });
//
//        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
//            @Override
//            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//                menu.add("Supprimer");
//
//                AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
//                Wine wine = WineDbHelper.cursorToWine((Cursor)listView.getItemAtPosition(acmi.position));
//
//                MainActivity.this.selectedWine = wine;
//            }
//        });
//
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewTeamActivity.class);
                startActivityForResult(intent, MainActivity.ADD_TEAM_REQUEST);
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
//        if(item.getTitle().toString().equals("Supprimer")) {
//            WineDbHelper wineDbHelper = new WineDbHelper(this);
//            wineDbHelper.getWritableDatabase();
//
//            wineDbHelper.deleteWine(MainActivity.this.selectedWine);
//
//            finish();
//            startActivity(getIntent());
//
//            return true;
//        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MainActivity.ADD_TEAM_REQUEST && resultCode == RESULT_OK) {
            if(data.hasExtra(Team.TAG)) {
                Team team = data.getParcelableExtra(Team.TAG);
                this.dbHelper.addTeam(team);
                recreate();
            }
        }

    }
}
