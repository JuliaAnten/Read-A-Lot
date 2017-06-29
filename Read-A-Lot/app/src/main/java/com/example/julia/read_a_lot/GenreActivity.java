/*
 * Handles the possibility to choose a genre.
 * Copyright © 2017 Julia Anten. All rights reserved.
 */

package com.example.julia.read_a_lot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class GenreActivity extends AppCompatActivity {

    String genre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);
    }


    /**
     * Checks which genre button is clicked.
     * @param view The four genre buttons.
     */
    public void onGenreClicked(View view) {
        switch (view.getId()) {
            case R.id.Horror:
                genre = "horror";
                break;
            case R.id.Mystery:
                genre = "mystery";
                break;
            case R.id.Romance:
                genre = "romance";
                break;
            case R.id.SciFi:
                genre = "scifi";
                break;
            default:
                throw new RuntimeException("Unknown button ID");
        }

        goToGame();
    }


    /**
     * Sends user to the game.
     */
    public void goToGame() {
        Intent intent = new Intent(getApplicationContext(),GameActivity.class);
        intent.putExtra("onCreate", 0);
        intent.putExtra("genre", genre);
        startActivity(intent);
        finish();
    }
}
