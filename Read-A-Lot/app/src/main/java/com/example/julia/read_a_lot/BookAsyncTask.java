package com.example.julia.read_a_lot;

import android.os.AsyncTask;

/**
 * Created by Julia on 12/06/2017.
 */

public class BookAsyncTask extends AsyncTask<String, Void, String> {

    String selectedBook;

    public BookAsyncTask(String main){
        this.selectedBook = main;
    }

    @Override
    protected String doInBackground(String... params) {
        return HttpRequestHelper.downloadFromServer(params);
    }
}
