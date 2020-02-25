package com.ceri.tp3;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        registerForContextMenu(findViewById(R.id.lvWines));

//        WineDbHelper wineDbHelper = new WineDbHelper(this);
//        wineDbHelper.getWritableDatabase(); //calls onCreate method
//        if(wineDbHelper.getOnCreateCalled())
//            wineDbHelper.populate();
//
//
//        Cursor c = wineDbHelper.fetchAllWines();
//
//        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, c,
//                new String[]{WineDbHelper.COLUMN_NAME,WineDbHelper.COLUMN_WINE_REGION}, new int[]{android.R.id.text1,android.R.id.text2});
//
//        final ListView listView = (ListView) findViewById(R.id.lvWines);
//        listView.setAdapter(adapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView parent, View view, int position, long id) {
//                Wine wine = WineDbHelper.cursorToWine((Cursor)parent.getItemAtPosition(position));
//
//                System.out.println("nom:"+wine.getTitle()+" ; id:"+wine.getId());//
//
//                Intent intent = new Intent(MainActivity.this, WineActivity.class);
//                intent.putExtra("Wine", wine);
//                startActivity(intent);
//            }
//        });
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
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, WineActivity.class);
//                intent.putExtra("Wine", new Wine(0, "","","","",""));
//                intent.putExtra("Add", true);
//                startActivity(intent);
//            }
//        });
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
}
