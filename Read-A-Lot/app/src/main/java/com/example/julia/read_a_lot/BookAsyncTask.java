package com.example.julia.read_a_lot;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Julia on 12/06/2017.
 */

public class BookAsyncTask extends AsyncTask<String, Void, String> {

    Context context;
    GameActivity gameAct;


    public BookAsyncTask(GameActivity game){
        this.gameAct = game;
        this.context = this.gameAct.getApplicationContext();

    }

    @Override
    protected String doInBackground(String... params) {
        return HttpRequestHelper.downloadFromServer(params);
    }

    @Override
    protected void onPostExecute(String bookInfo){
        super.onPostExecute(bookInfo);

        this.gameAct.handleBookInfo(bookInfo);
    }
}
