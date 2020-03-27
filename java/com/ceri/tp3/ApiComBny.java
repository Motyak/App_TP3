package com.ceri.tp3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import Mk.HttpCon;

//classes qui contient les scénarios avec l'API
public class ApiComBny {

    public static boolean updateTeam(Team team) throws IOException {
//                requete mise a jour infos generales
        URL searchTeamUrl = WebServiceUrl.buildSearchTeam(team.getName());
        String res = HttpCon.request(HttpCon.Type.GET, searchTeamUrl.toString(), null, null);
        InputStream is = new ByteArrayInputStream(res.getBytes("UTF-8"));
        JSONResponseHandlerTeam jsonTeam = new JSONResponseHandlerTeam(team);
        jsonTeam.readJsonStream(is);
//        si la team est introuvable dans l'API (par rapport à son nom)
        if(team.getIdLeague() == 0)
            return false;

        team = jsonTeam.getTeam();


//                requete mise a jour dernier match
        URL searchLastEventsUrl = WebServiceUrl.buildSearchLastEvents(team.getIdTeam());
        res = HttpCon.request(HttpCon.Type.GET, searchLastEventsUrl.toString(), null, null);
        is = new ByteArrayInputStream(res.getBytes("UTF-8"));
        JSONResponseHandlerLastEvents jsonLastEvents = new JSONResponseHandlerLastEvents(team);
        jsonLastEvents.readJsonStream(is);
        team = jsonLastEvents.getTeam();

//                requete mise a jour classement
        URL getRankingUrl = WebServiceUrl.buildGetRanking(team.getIdLeague());
        res = HttpCon.request(HttpCon.Type.GET, getRankingUrl.toString(), null, null);
        is = new ByteArrayInputStream(res.getBytes("UTF-8"));
        JSONResponseHandlerTeamRanking jsonRanking = new JSONResponseHandlerTeamRanking(team);
        jsonRanking.readJsonStream(is);
        team = jsonRanking.getTeam();

        return true;
    }

    public static Bitmap downloadTeamBadge(String url) {
        HttpURLConnection con = null;
        try {
            URL imageUrl = new URL(url);
            con = (HttpURLConnection) imageUrl.openConnection();
            if(con.getResponseCode() == 200) {
                InputStream is = new BufferedInputStream(con.getInputStream());
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                Bitmap img = BitmapFactory.decodeStream(is, null, options);
                return img;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(con != null)
                con.disconnect();
        }
        return null;    //en cas d'erreur
    }
}
