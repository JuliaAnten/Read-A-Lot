package com.example.julia.read_a_lot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    String titles[];
    JSONObject bookObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        titles = getResources().getText(R.string.booktitles).toString().split("=");
        bookSearch();
    }


    /**
     * Sends a request to the AsyncTask to request a book.
     */
    public void bookSearch(){
        String selectedBook = "";
        Random rand = new Random();

        // Get book title
        selectedBook = titles[rand.nextInt(103)];
        Log.d("log",selectedBook);
        BookAsyncTask asyncTask = new BookAsyncTask(this);
        asyncTask.execute(selectedBook);

    }


    public void handleBookInfo(String bookInfo) {

        String description = "";

        JSONArray items = null;
        JSONArray volumeInfo = null;
        try {
            bookObject = new JSONObject(bookInfo);
            items = bookObject.getJSONArray("items");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("log", String.valueOf(items));
        Log.d("log", "4");

    }

    /**
     * Takes care of a new question.
     */
    public void onNextClicked(View view){

    }
    /**
     * Creates high score button.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.highscore_menu, menu);
        return true;
    }


    /**
     * Implements functionality of high score  button.
     * Will come later
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }
}
