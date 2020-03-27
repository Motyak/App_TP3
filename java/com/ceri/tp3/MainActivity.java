package com.ceri.tp3;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleCursorAdapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.design.widget.FloatingActionButton;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import Mk.HttpCon;

public class MainActivity extends AppCompatActivity {

    static final public int ADD_TEAM_REQUEST = 1000;
    static final public int UPDATE_TEAM_REQUEST = 1001;

    final private SportDbHelper dbHelper = new SportDbHelper(this);

    private SwipeRefreshLayout refresh;
//    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        registerForContextMenu(findViewById(R.id.lvTeams));
        registerForContextMenu(findViewById(R.id.rvTeams)); //a modifier?

        if(dbHelper.getAllTeams().isEmpty())
            dbHelper.populate();    //seulement lorsque la table est vide

//        this.adapter = new SimpleCursorAdapter(
//                this,
//                android.R.layout.simple_list_item_2,
//                dbHelper.fetchAllTeams(),
//                new String[]{SportDbHelper.COLUMN_TEAM_NAME, SportDbHelper.COLUMN_LEAGUE_NAME},
//                new int[]{android.R.id.text1, android.R.id.text2}
//                );

        RecyclerView rvTeams = findViewById(R.id.rvTeams);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, this.dbHelper.getAllTeams());
        rvTeams.setAdapter(adapter);
        rvTeams.setLayoutManager(new LinearLayoutManager(this));
        rvTeams.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
//
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewTeamActivity.class);
                startActivityForResult(intent, MainActivity.ADD_TEAM_REQUEST);
            }
        });

        this.refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        this.refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new UpdateAllTeamTask().execute();
            }
        });

//        ask for writing permissions if not already granted
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, TeamActivity.ASK_WRITE_PERMISSIONS_REQUEST);
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
//                this.adapter.changeCursor(this.dbHelper.fetchAllTeams());
//                this.adapter.notifyDataSetChanged();
            }

        }
        else if(requestCode == MainActivity.UPDATE_TEAM_REQUEST && resultCode == RESULT_OK) {
            if(data.hasExtra(Team.TAG)) {
                Team team = data.getParcelableExtra(Team.TAG);
                Log.d("wouloulou", "onActivityResult: id = " + team.getId() + "team = " + team);//debug
                this.dbHelper.updateTeam(team);
                recreate();
//                this.adapter.changeCursor(this.dbHelper.fetchAllTeams());
//                this.adapter.notifyDataSetChanged();
            }
        }

    }

    class UpdateAllTeamTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
//            on recupere l'ensemble des teams de la BDD et on les store dans un array
            List<Team> teams = MainActivity.this.dbHelper.getAllTeams();

//            pour chaque Team, on update ses valeurs
            for(Team t : teams) {
                try {
                    ApiComBny.updateTeam(t);
                    MainActivity.this.dbHelper.updateTeam(t);

//                    badge
                    String path = MainActivity.this.getApplicationContext().getExternalFilesDir(null).toString();
                    File file = new File(path, t.getId() + ".png");
                    if(!file.exists())
                    {
                        Bitmap img = ApiComBny.downloadTeamBadge(t.getTeamBadge());

                        if(img != null && ContextCompat.checkSelfPermission(MainActivity.this.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                        {
                            OutputStream os = null;
                            os = new FileOutputStream(file);
                            img.compress(Bitmap.CompressFormat.PNG, 85, os);
                            os.close();

                            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            MainActivity.this.refresh.setRefreshing(false);
            MainActivity.this.recreate();   //ca ou updateView
//            MainActivity.this.adapter.changeCursor(MainActivity.this.dbHelper.fetchAllTeams());
//            MainActivity.this.adapter.notifyDataSetChanged();
        }
    }
}
