package com.ceri.tp3;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import Mk.HttpCon;

public class ApiComBny {

    public static Team updateTeam(Team team) throws IOException {
//                requete mise a jour infos generales
        URL searchTeamUrl = WebServiceUrl.buildSearchTeam(team.getName());
        String res = HttpCon.request(HttpCon.Type.GET, searchTeamUrl.toString(), null, null);
        InputStream is = new ByteArrayInputStream(res.getBytes("UTF-8"));
        JSONResponseHandlerTeam jsonTeam = new JSONResponseHandlerTeam(team);
        jsonTeam.readJsonStream(is);
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

        return team;
    }

}
