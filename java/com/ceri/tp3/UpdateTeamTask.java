package com.ceri.tp3;

import android.os.AsyncTask;

public class UpdateTeamTask extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] objects) {
        //TODO : simplifier ma classe HttpCon et la réutiliser

//                try {
//                    URL searchTeamUrl = WebServiceUrl.buildSearchTeam();
//                    URL searchLastEventsUrl = WebServiceUrl.buildSearchLastEvents();
//                    URL getRankingUrl = WebServiceUrl.buildGetRanking();
//
//                    simplifier ma classe HttpCon...
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        //TODO : refraichir la vue (avec recreate() par ex..)
        //TODO : faire en sorte que la main activity soit egalement rechargée quand on revient dessus
    }
}
