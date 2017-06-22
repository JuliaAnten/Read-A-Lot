package com.example.julia.read_a_lot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }


    /**
     * Sends user to the game.
     */
    public void goToGame(View view) {
        Intent intent = new Intent(getApplicationContext(),GameActivity.class);
        intent.putExtra("onCreate", 0);
        intent.putExtra("genre", "default");
        startActivity(intent);
    }

    /**
     * Sends user to activity to pick genre.
     */
    public void goToGenre(View view) {
        Intent intent = new Intent(getApplicationContext(), GenreActivity.class);
        startActivity(intent);
    }
}
