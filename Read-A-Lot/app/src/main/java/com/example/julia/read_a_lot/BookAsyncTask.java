package com.example.julia.read_a_lot;

import android.app.AlertDialog;
import android.os.AsyncTask;

/**
 * Helps to get book information.
 * Created by Julia on 12/06/2017.
 */

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
    protected void onPreExecute(){
        alertDialog = new AlertDialog.Builder(gameAct).create();
        alertDialog.setMessage("Get ready!");
        alertDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        return HttpRequestHelper.downloadFromServer(params);
    }

    @Override
    protected void onPostExecute(String bookInfo){
        super.onPostExecute(bookInfo);
        alertDialog.dismiss();
        this.gameAct.handleBookInfo(bookInfo);
    }
}
