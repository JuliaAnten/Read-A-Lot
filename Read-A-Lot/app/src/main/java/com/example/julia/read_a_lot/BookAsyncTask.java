//TODO: moet dit hier of iets naar beneden
/*
 * Handles the AsyncTask to get the book information.
 * Created by Julia Anten.
 */
package com.example.julia.read_a_lot;

import android.app.AlertDialog;
import android.os.AsyncTask;

class BookAsyncTask extends AsyncTask<String, Void, String> {

    private GameActivity gameAct;
    private AlertDialog alertDialog;

    BookAsyncTask(GameActivity game){
        this.gameAct = game;
    }

    /**
     * Zou dit hier ook moeten?
     */
    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... params) {
        return HttpRequestHelper.downloadFromServer(params);
    }

    @Override
    protected void onPostExecute(String bookInfo) {
        super.onPostExecute(bookInfo);
        this.gameAct.handleBookInfo(bookInfo);
    }
}
