package com.ceri.tp3;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TeamActivity extends AppCompatActivity {

    private static final String TAG = TeamActivity.class.getSimpleName();
    static final public int ASK_WRITE_PERMISSIONS_REQUEST = 1002;
    private TextView textTeamName, textLeague, textManager, textStadium, textStadiumLocation,
            textTotalScore, textRanking, textLastMatch, textLastUpdate;


    private int totalPoints;
    private int ranking;
    private Match lastEvent;
    private String lastUpdate;

    private ImageView imageBadge;
    private Team team;

    public void setTeam(Team team) { this.team = team; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        team = (Team) getIntent().getParcelableExtra(Team.TAG);

        textTeamName = (TextView) findViewById(R.id.nameTeam);
        textLeague = (TextView) findViewById(R.id.league);
        textStadium = (TextView) findViewById(R.id.editStadium);
        textStadiumLocation = (TextView) findViewById(R.id.editStadiumLocation);
        textTotalScore = (TextView) findViewById(R.id.editTotalScore);
        textRanking = (TextView) findViewById(R.id.editRanking);
        textLastMatch = (TextView) findViewById(R.id.editLastMatch);
        textLastUpdate = (TextView) findViewById(R.id.editLastUpdate);
        imageBadge = (ImageView) findViewById(R.id.imageView);

        updateView();

        final Button but = (Button) findViewById(R.id.button);


        but.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new UpdateTeamTask(TeamActivity.this.team).execute();
            }
        });

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra(Team.TAG, this.team);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void updateView() {

        textTeamName.setText(team.getName());
        textLeague.setText(team.getLeague());
        textStadium.setText(team.getStadium());
        textStadiumLocation.setText(team.getStadiumLocation());
        textTotalScore.setText(Integer.toString(team.getTotalPoints()));
        textRanking.setText(Integer.toString(team.getRanking()));
        textLastMatch.setText(team.getLastEvent().toString());
        textLastUpdate.setText(team.getLastUpdate());

//        ajout du logo
        String dirPath = getApplicationContext().getExternalFilesDir(null).toString();
        File imageFile = new File(dirPath, this.team.getId() + ".png");
        if(imageFile.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap img = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
            imageBadge.setImageBitmap(img);
        }
    }

    class UpdateTeamTask extends AsyncTask {

        private Team team;

        UpdateTeamTask(Team team) {
            this.team = team;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
//                mise a jour infos de l'equipe dans l'activité
                ApiComBny.updateTeam(this.team);

//                recuperation du logo
                String path = TeamActivity.this.getApplicationContext().getExternalFilesDir(null).toString();
                File file = new File(path, this.team.getId() + ".png");
                if(!file.exists())
                {
                    Bitmap img = ApiComBny.downloadTeamBadge(this.team.getTeamBadge());

                    if(img != null && ContextCompat.checkSelfPermission(TeamActivity.this.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
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

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            TeamActivity.this.setTeam(this.team);
            TeamActivity.this.updateView();
        }
    }

}
