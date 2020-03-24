package com.ceri.tp3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import Mk.HttpCon;

public class TeamActivity extends AppCompatActivity {

    private static final String TAG = TeamActivity.class.getSimpleName();
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
                //TODO : appeler la tache permettant d'update l'équipe en question
                new UpdateTeamTask(TeamActivity.this.team).execute();
            }
        });

    }

    @Override
    public void onBackPressed() {
        //TODO : prepare result for the main activity
        super.onBackPressed();
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

	//TODO : update imageBadge
    }

    class UpdateTeamTask extends AsyncTask {

        private Team team;

        UpdateTeamTask(Team team) {
            this.team = team;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            //TODO : simplifier ma classe HttpCon et la réutiliser

            try {
//                requete mise a jour infos generales
                URL searchTeamUrl = WebServiceUrl.buildSearchTeam(this.team.getName());
                System.out.println(searchTeamUrl.toString());
                String res = HttpCon.request(HttpCon.Type.GET, searchTeamUrl.toString(), null, null);
                InputStream is = new ByteArrayInputStream(res.getBytes("UTF-8"));
                JSONResponseHandlerTeam jsonTeam = new JSONResponseHandlerTeam(this.team);
                jsonTeam.readJsonStream(is);
                this.team = jsonTeam.getTeam();

////                requete mise a jour dernier match
//                URL searchLastEventsUrl = WebServiceUrl.buildSearchLastEvents(this.team.getIdTeam());
//                res = HttpCon.request(HttpCon.Type.GET, searchLastEventsUrl.toString(), null, null);
//                is = new ByteArrayInputStream(res.getBytes("UTF-8"));
//                JSONResponseHandlerLastEvents jsonLastEvents = new JSONResponseHandlerLastEvents(this.team);
//                jsonLastEvents.readJsonStream(is);
//                this.team = jsonLastEvents.getTeam();

////                    prend en param l'id de la ligue
//                    URL getRankingUrl = WebServiceUrl.buildGetRanking();

//                    simplifier ma classe HttpCon...
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            //TODO : refraichir la vue (avec recreate() par ex..)
            //TODO : faire en sorte que la main activity soit egalement rechargée quand on revient dessus
            //TODO : faire en sorte que la bdd soit mise à jour lorsqu'on revient dans la MainActivity


            TeamActivity.this.setTeam(this.team);
            TeamActivity.this.updateView();
        }
    }

}
