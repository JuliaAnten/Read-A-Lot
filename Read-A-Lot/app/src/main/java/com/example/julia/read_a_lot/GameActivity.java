/*
 * Handles the core functionality of the game.
 * Copyright © 2017 Julia Anten. All rights reserved.
 */

package com.example.julia.read_a_lot;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    String[] titles;
    String selectedBook = "";
    String bookPlot;
    String chosenAnswer;
    String wrongAnswerOne;
    String wrongAnswerTwo;
    String genre;
    String rightAnswer;
    String[] titleAndAuthor = new String[0];

    int onCreate;
    int firstScore;
    int secondScore;
    int thirdScore;
    int countNonResponse = 0;

    JSONObject bookObject;
    JSONObject[] volumeObject;
    JSONArray itemsArray;
    Random rand = new Random();
    int streak;

    TextView plotTextView;
    TextView streakTextView;
    Button firstAnswerButton;
    Button secondAnswerButton;
    Button thirdAnswerButton;
    ImageButton nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        onCreate = getIntent().getIntExtra("onCreate", 1);
        genre = getIntent().getStringExtra("genre");

        setUpViews();
        checkConnection();
        selectList();
        loadStreakFromSharedPreferences();
        bookSearch();
    }


    /**
     * Initiate & Next
     * Controls if there is network connection. Needed to play the game.
     * @return  Returns true, if there is network connection, else false.
     */
    public boolean checkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // checks if there is network connection
        if (cm.getActiveNetworkInfo() == null) {

            // if there is no connection disable answer button and show dialog
            firstAnswerButton.setEnabled(false);
            secondAnswerButton.setEnabled(false);
            thirdAnswerButton.setEnabled(false);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No network connection");
            builder.setMessage("Please connect to the internet");
            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    boolean connection = checkConnection();
                    if (connection) {
                        onCreate = 0;
                        bookSearch();
                    }
                }
            });
            builder.create().show();
            return false;
        }
        return true;
    }

    /**
     * Initiate
     * Sets up the text views and buttons.
     */
    public void setUpViews() {
        plotTextView = (TextView) findViewById(R.id.plotView);
        plotTextView.setMovementMethod(new ScrollingMovementMethod());

        streakTextView = (TextView) findViewById(R.id.streakView);
        streakTextView.setText(String.valueOf(0));

        firstAnswerButton = (Button) findViewById(R.id.answerOne);
        firstAnswerButton.setEnabled(false);
        secondAnswerButton = (Button) findViewById(R.id.answerTwo);
        secondAnswerButton.setEnabled(false);
        thirdAnswerButton = (Button) findViewById(R.id.answerThree);
        thirdAnswerButton.setEnabled(false);
        nextButton = (ImageButton) findViewById(R.id.nextButton);
    }


    /**
     * Initiate
     * Selects the genre and selects the right string to use.
     */
    public void selectList() {
        switch (genre) {
            case "horror":
                titles = getResources().getText(R.string.booktitlesHorror).toString().split("=");
                break;
            case "mystery":
                titles = getResources().getText(R.string.booktitlesMystery).toString().split("=");
                break;
            case "romance":
                titles = getResources().getText(R.string.booktitlesRomance).toString().split("=");
                break;
            case "scifi":
                titles = getResources().getText(R.string.booktitlesSciFi).toString().split("=");
                break;
            default:
                titles = getResources().getText(R.string.booktitles).toString().split("=");
                break;
        }
    }


    /**
     * Book info
     * Sends a request to the AsyncTask to find a specific book.
     */
    public void bookSearch() {
        final int maxAmountNonResponse = 3;

        // if there is no correct response from the API three times, a dialog is shown
        if (countNonResponse < maxAmountNonResponse) {
            selectedBook = titles[rand.nextInt(titles.length)];
            BookAsyncTask asyncTask = new BookAsyncTask(this);
            asyncTask.execute(selectedBook);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("No book plots available. Please try again later.");
            builder.create().show();
        }

    }


    /**
     * Book info
     * Starts the filtering of the book info after getting it back from the AsyncTask.
     * @param bookInfo  Information from the API about a book.
     */
    public void handleBookInfo(String bookInfo) {
        checkInfo(bookInfo);

        try {
            bookObject = new JSONObject(bookInfo);
            itemsArray = bookObject.getJSONArray("items");
            volumeObject = new JSONObject[itemsArray.length()];

            getBookPlot();
            filterBookPlot();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Book info
     * Checks if the info from the API is correct
     * @param bookInfo  Information from the API about a book.
     */
    private void checkInfo(String bookInfo) {
        if (bookInfo.equals("wrong")) {
            countNonResponse++;
            bookSearch();
        } else {
            countNonResponse = 0;
        }
    }


    /**
     * Book info
     * Find the right book from the responses from the API.
     */
    public void getBookPlot() {

        // get all volumeInfo objects from itemsArray
        for (int i = 0; i < itemsArray.length(); i++){
            try {
                JSONObject object = itemsArray.getJSONObject(i);
                volumeObject[i] = object.getJSONObject("volumeInfo");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // find the right volume from the volume array.
        for (JSONObject object : volumeObject) {

            titleAndAuthor = selectedBook.split(",");
            JSONArray authors = null;

            try {
                authors = object.getJSONArray("authors");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // checks the title and author to find the right volume
            if (authors != null) {
                try {
                    if (object.getString("title").toLowerCase().
                            equals(titleAndAuthor[0].substring(1).toLowerCase()) &
                            authors.toString().contains(titleAndAuthor[1].
                                    substring(titleAndAuthor[1].lastIndexOf(" ")))) {
                        try {
                            bookPlot = object.getString("description");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            bookSearch();
                            return;
                        }
                        break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        if (bookPlot == null) {
            bookSearch();
        }
    }


    /**
     * Book info
     * Filters title information and author information from descriptions.
     */
    public void filterBookPlot() {
        if (bookPlot != null) {
            String lastName = titleAndAuthor[1].substring(titleAndAuthor[1].lastIndexOf(" "));
            String firstName = titleAndAuthor[1].substring(titleAndAuthor[1].lastIndexOf(" ", 0));

            bookPlot = bookPlot.replaceAll("(?i)" + titleAndAuthor[0].substring(1), " ... ");
            bookPlot = bookPlot.replaceAll("(?i)" + titleAndAuthor[1].substring(1), " ... ");
            bookPlot = bookPlot.replaceAll("(?i)" + lastName.substring(1), " ... ");
            bookPlot = bookPlot.replaceAll("(?i)" + firstName, " ... ");

            setupNext();
        }
    }

    /**
     * Book info & Next
     * Sets up the next question.
     */
    public void setupNext() {

        // if activity is in onCreate, show items, else make next button visible.
        if (onCreate == 0) {
            setToViews();
            onCreate = 1;
        } else {
            nextButton.setVisibility(View.VISIBLE);
            if (chosenAnswer == null) {
                nextButton.setEnabled(false);
            }
        }
    }

    /**
     * Answers
     * Checks which answer button is clicked.
     * @param view  The three answer buttons.
     */
    public void onAnswerClicked(View view) {
        switch (view.getId()) {
            case R.id.answerOne:
                chosenAnswer = firstAnswerButton.getText().toString();
                break;
            case R.id.answerTwo:
                chosenAnswer = secondAnswerButton.getText().toString();
                break;
            case R.id.answerThree:
                chosenAnswer = thirdAnswerButton.getText().toString();
                break;
            default:
                throw new RuntimeException("Unknown button ID");
        }

        checkAnswers();
        changeButtons();
    }


    /**
     * Answers
     * Checks if the answer the user has given is correct.
     */
    public void checkAnswers() {
        if (chosenAnswer.equals(rightAnswer)) {
            streak +=1;
        } else {
            checkHighScores();
            streak = 0;
        }

        saveStreakToSharedPreferences();
    }


    /**
     * Answers
     * Checks the streak against the current high scores and save these to shared preferences.
     */
    public void checkHighScores() {
        loadHighScoreFromSharedPreferences();

        SharedPreferences preferences = this.getSharedPreferences("highScores", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // changes high scores in shared preferences if needed
        if (firstScore < streak) {
            editor.putInt("secondScore", firstScore);
            editor.putInt("thirdScore", secondScore);
            editor.putInt("firstScore", streak);
        } else if (secondScore < streak) {
            editor.putInt("thirdScore", secondScore);
            editor.putInt("secondScore", streak);
        } else if (thirdScore < streak) {
            editor.putInt("thirdScore",streak);
        }

        editor.apply();
    }


    /**
     * Answers
     * Changes background color of the button with the right answer.
     */
    public void changeButtons() {
        int rightColor = ContextCompat.getColor(this, R.color.rightButton);

        // change background color of button with right answer
        if (firstAnswerButton.getText().equals(rightAnswer)) {
            firstAnswerButton.setBackgroundColor(rightColor);
        } else if (secondAnswerButton.getText().equals(rightAnswer)) {
            secondAnswerButton.setBackgroundColor(rightColor);
        } else {
            thirdAnswerButton.setBackgroundColor(rightColor);
        }

        // disable buttons after an answer is chosen
        firstAnswerButton.setEnabled(false);
        secondAnswerButton.setEnabled(false);
        thirdAnswerButton.setEnabled(false);

        // enable next button if answer is chosen and available
        if (nextButton.getVisibility() == View.VISIBLE) {
            nextButton.setEnabled(true);
        }
    }


    /**
     * Next
     * Takes care of a new question.
     * @param view The next button.
     */
    public void onNextClicked(View view) {
        setToViews();
        checkConnection();
    }


    /**
     * Next
     * Sets the plot and answers to their views.
     */
    public void setToViews() {
        restoreBeginSettings();
        assertWrong();
        plotTextView.setText(bookPlot);

        int n = rand.nextInt(3);

        // assigns the right answer to a random button
        if (n == 0) {
            firstAnswerButton.setText(wrongAnswerOne);
            secondAnswerButton.setText(wrongAnswerTwo);
            thirdAnswerButton.setText(selectedBook);
        } else if (n == 1) {
            firstAnswerButton.setText(wrongAnswerOne);
            secondAnswerButton.setText(selectedBook);
            thirdAnswerButton.setText(wrongAnswerTwo);
        } else {
            firstAnswerButton.setText(selectedBook);
            secondAnswerButton.setText(wrongAnswerTwo);
            thirdAnswerButton.setText(wrongAnswerOne);
        }

        nextSearch();
    }


    /**
     * Next
     * Restore begin settings after next button is clicked.
     */
    private void restoreBeginSettings() {
        int backgroundColor = ContextCompat.getColor(this, R.color.backgroundButton);

        nextButton.setVisibility(View.INVISIBLE);
        firstAnswerButton.setBackgroundColor(backgroundColor);
        secondAnswerButton.setBackgroundColor(backgroundColor);
        thirdAnswerButton.setBackgroundColor(backgroundColor);

        plotTextView.scrollTo(0, 0);
        firstAnswerButton.setEnabled(true);
        secondAnswerButton.setEnabled(true);
        thirdAnswerButton.setEnabled(true);
    }


    /**
     * Next
     * Assert wrong titles.
     */
    public void assertWrong() {
        wrongAnswerOne = titles[rand.nextInt(titles.length)];
        wrongAnswerTwo = titles[rand.nextInt(titles.length)];

        // while a wrong item equals the right answer, replace it
        while (wrongAnswerOne.equals(selectedBook)) {
            wrongAnswerOne = titles[rand.nextInt(titles.length)];
        }

        while (wrongAnswerTwo.equals(selectedBook)) {
            wrongAnswerTwo = titles[rand.nextInt(titles.length)];
        }
    }


    /**
     * Next
     * Initiates next search as soon as next button is clicked.
     */
    public void nextSearch() {
        rightAnswer = selectedBook;
        chosenAnswer = null;
        bookPlot = null;
        bookSearch();
    }


    /**
     * Shared preferences
     * Saves the current streak to the shared preferences after an answer is chosen.
     */
    private void saveStreakToSharedPreferences() {
        SharedPreferences preferences = this.getSharedPreferences("streaks", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("streak", streak);
        editor.apply();
        streakTextView.setText(String.valueOf(streak));
    }


    /**
     * Shared preferences
     * Loads all the high scores from shared preferences, if there is none, default is zero.
     */
    public void loadHighScoreFromSharedPreferences() {
        SharedPreferences prefs = this.getSharedPreferences("highScores", MODE_PRIVATE);

        firstScore = prefs.getInt("firstScore", 0);
        secondScore = prefs.getInt("secondScore", 0);
        thirdScore = prefs.getInt("thirdScore",0);
    }


    /**
     * Shared preferences
     * Loads current streak from the shared preferences, default is zero.
     */
    public void loadStreakFromSharedPreferences() {
        SharedPreferences prefs = this.getSharedPreferences("streaks", MODE_PRIVATE);

        streak = prefs.getInt("streak", 0);

        streakTextView.setText(String.valueOf(streak));
    }


    /**
     * Menu
     * Creates high score button.
     * @param menu  The menu resource with the appearance of the menu.
     * @return  Returns true when finished.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.highscore_menu, menu);
        return true;
    }


    /**
     * Menu
     * Implements functionality of high score button.
     * @param item  Item in the menu resource with the high score button.
     * @return  Returns true when finished.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        loadHighScoreFromSharedPreferences();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("High Score");
        builder.setMessage("1.\t" + firstScore + "\n2.\t" + secondScore + "\n3.\t" + thirdScore);
        builder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        builder.create().show();

        return true;
    }
}