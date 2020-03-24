package com.ceri.tp3;

import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

//import fr.uavignon.shuet.tp3.data.Team;

/**
 * Process the response to a GET request to the Web service
 * https://www.thesportsdb.com/api/v1/json/1/searchteams.php?t=R
 * Responses must be provided in JSON.
 *
 */


public class JSONResponseHandlerTeam {

    private static final String TAG = JSONResponseHandlerTeam.class.getSimpleName();

    private Team team;


    public JSONResponseHandlerTeam(Team team) {
        this.team = team;
    }

    public Team getTeam() { return this.team; }

    /**
     * @param response done by the Web service
     * @return A Team with attributes filled with the collected information if response was
     * successfully analyzed
     */
    public void readJsonStream(InputStream response) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(response, "UTF-8"));
        try {
            readTeams(reader);
        } finally {
            reader.close();
        }
    }

    public void readTeams(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("teams")) {
                readArrayTeams(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
    }


    private void readArrayTeams(JsonReader reader) throws IOException {
        reader.beginArray();
        int nb = 0; // only consider the first element of the array
        while (reader.hasNext() ) {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (nb==0) {
                    if (name.equals("idTeam")) {
                        team.setIdTeam(reader.nextLong());
                    } else if (name.equals("strTeam")) {
                        team.setName(reader.nextString());
                    } else if (name.equals("strLeague")) {
                        team.setLeague(reader.nextString());
                    } else if (name.equals("idLeague")) {
                        team.setIdLeague(reader.nextLong());
                    } else if (name.equals("strStadium")) {
                        team.setStadium(reader.nextString());
                    } else if (name.equals("strStadiumLocation")) {
                        team.setStadiumLocation(reader.nextString());
                    } else if (name.equals("strTeamBadge")) {
                        team.setTeamBadge(reader.nextString());
                    } else {
                        reader.skipValue();
                    }
                }  else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            nb++;
        }
        reader.endArray();
    }

}