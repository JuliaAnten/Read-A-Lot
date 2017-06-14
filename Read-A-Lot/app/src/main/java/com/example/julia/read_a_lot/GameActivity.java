package com.example.julia.read_a_lot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    String titles[];
    JSONObject bookObject;
    String bookPlot = null;
    String bookTitle = null;
    TextView plotTextView;
    Button answer1Button;
    Button answer2Button;
    Button answer3Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        plotTextView = (TextView) findViewById(R.id.plotView);
        answer1Button = (Button) findViewById(R.id.answer1);
        answer2Button = (Button) findViewById(R.id.answer2);
        answer3Button = (Button) findViewById(R.id.answer3);

        titles = getResources().getText(R.string.booktitles).toString().split("=");
        bookSearch();
    }


    /**
     * Sends a request to the AsyncTask to request a book.
     */
    public void bookSearch(){
        String selectedBook = "";
        Random rand = new Random();

        // Get book bookTitle
        selectedBook = titles[rand.nextInt(103)];
        Log.d("log",selectedBook);
        BookAsyncTask asyncTask = new BookAsyncTask(this);
        asyncTask.execute(selectedBook);

    }


    /**
     * Filters the book info after getting it back from the AsyncTask.
     */
    public void handleBookInfo(String bookInfo) {

        JSONObject volumeObject = null;

        try {
            bookObject = new JSONObject(bookInfo);
            JSONArray itemsArray = bookObject.getJSONArray("items");

            for (int i = 0; i < itemsArray.length(); i++){

                try{
                    JSONObject object = itemsArray.getJSONObject(i);
                    volumeObject = object.getJSONObject("volumeInfo");
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }

            if (volumeObject != null) {
                bookPlot = volumeObject.getString("description");
                bookTitle = volumeObject.getString("title");
            }
Log.d("log",bookPlot);
            Log.d("log",bookTitle);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        setToViews();
    }


    public void setToViews(){

        plotTextView.setText(bookPlot);

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
