package com.example.julia.read_a_lot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    String titles[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        titles = getResources().getText(R.string.booktitles).toString().split(",");
        bookSearch();
    }


    public String bookSearch(){
        String selected = "";
        Random rand = new Random();

        // Get three book titles
        for(int i = 0; i < 3; i++){
            selected += titles[rand.nextInt(105)];
            selected += ",";
        }
        Log.d("log",selected);
        return selected;


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
