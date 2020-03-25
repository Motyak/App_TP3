package com.ceri.tp3;

import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

/**
 * Process the response to a GET request to the Web service
 * https://www.thesportsdb.com/api/v1/json/1/lookuptable.php?l=4414&s=1920
 * Responses must be provided in JSON.
 *
 */


public class JSONResponseHandlerTeamRanking {

    private static final String TAG = JSONResponseHandlerTeamRanking.class.getSimpleName();

    private Team team;


    public JSONResponseHandlerTeamRanking(Team team) {
        this.team = team;
    }

    public Team getTeam() { return this.team; }

    /**
     * @param response done by the Web service
     *
     */
    public void readJsonStream(InputStream response) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(response, "UTF-8"));
        try {
            readTable(reader);
        } finally {
            reader.close();
        }
    }

    public void readTable(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("table")) {
                readArrayTable(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
    }


    private void readArrayTable(JsonReader reader) throws IOException {
        boolean foundTeam = false;
        boolean gotTeamRanking = false;
        reader.beginArray();
        int position = 1; // ranking position
        while (reader.hasNext() ) {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (!foundTeam) {
                    if (name.equals("name")) {
                        String s = reader.nextString();
                        if(s.equals(this.team.getName())) {
                            this.team.setRanking(position);
                            gotTeamRanking = true;
                        }
                    } else if (name.equals("total") && gotTeamRanking){
                        this.team.setTotalPoints(reader.nextInt());
                        foundTeam = true;
                    } else {
                        reader.skipValue();
                    }
                }  else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            position++;
        }
        reader.endArray();
    }

}