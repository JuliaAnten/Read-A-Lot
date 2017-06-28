/*
 * Handles the AsyncTask to get the book information.
 * Copyright © 2017 Julia Anten. All rights reserved.
 */
package com.example.julia.read_a_lot;

import android.os.AsyncTask;

class BookAsyncTask extends AsyncTask<String, Void, String> {

    private GameActivity gameAct;

    BookAsyncTask(GameActivity game){
        this.gameAct = game;
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
