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
import android.util.Log;
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
    String wrong1;
    String wrong2;
    String genre;
    String rightAnswer;
    String[] titleAndAuthor = new String[0];

    int onCreate;
    int first;
    int second;
    int third;

    JSONObject bookObject;
    JSONObject[] volumeObject;
    JSONArray itemsArray;
    Random rand = new Random();
    int streak;

    TextView plotTextView;
    TextView streakTextView;
    Button answer1Button;
    Button answer2Button;
    Button answer3Button;
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
        loadStreakFromSharedPrefs();
        bookSearch();
    }


    /**
     * Initiate
     * Checks if there is a connection to the internet.
     */
    public void checkConnection(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if(cm.getActiveNetworkInfo() == null){
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("No internet connection");
            alertDialog.setMessage("Please connect to internet");
            alertDialog.show();

            answer1Button.setEnabled(false);
            answer2Button.setEnabled(false);
            answer3Button.setEnabled(false);
        }
    }

    /**
     * Initiate
     * Sets up the text views and buttons.
     */
    public void setUpViews(){
        plotTextView = (TextView) findViewById(R.id.plotView);
        plotTextView.setMovementMethod(new ScrollingMovementMethod());

        streakTextView = (TextView) findViewById(R.id.streakView);
        streakTextView.setText(String.valueOf(0));

        answer1Button = (Button) findViewById(R.id.answer1);
        answer2Button = (Button) findViewById(R.id.answer2);
        answer3Button = (Button) findViewById(R.id.answer3);
        nextButton = (ImageButton) findViewById(R.id.nextButton);
    }


    /**
     * Initiate
     * Selects the genre.
     */
    public void selectList(){
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
    public void bookSearch(){
        selectedBook = titles[rand.nextInt(titles.length)];
        BookAsyncTask asyncTask = new BookAsyncTask(this);
        asyncTask.execute(selectedBook);
    }


    /**
     * Book info
     * Starts the filtering of the book info after getting it back from the AsyncTask.
     */
    public void handleBookInfo(String bookInfo) {
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
     * Find the right book from the responses from the API.
     */
    public void getBookPlot() {
        for (int i = 0; i < itemsArray.length(); i++){
            try{
                JSONObject object = itemsArray.getJSONObject(i);
                volumeObject[i] = object.getJSONObject("volumeInfo");
            } catch (JSONException e){
                e.printStackTrace();
            }
        }

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
                            equals(titleAndAuthor[0].substring(1).toLowerCase()) & authors.toString().
                            contains(titleAndAuthor[1].substring(titleAndAuthor[1].lastIndexOf(" ")))) {
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

        if (bookPlot == null){
            bookSearch();
        }
    }


    /**
     * Book info
     * Filters title information and author information from descriptions.
     */
    public void filterBookPlot() {
        if (bookPlot != null){
            String lastName = titleAndAuthor[1].substring(titleAndAuthor[1].lastIndexOf(" "));
            String firstName = titleAndAuthor[1].substring(titleAndAuthor[1].lastIndexOf(" ", 0));

            bookPlot = bookPlot.replaceAll("(?i)" + titleAndAuthor[0].substring(1), " ... ");
            bookPlot = bookPlot.replaceAll("(?i)" + titleAndAuthor[1].substring(1), " ... ");
            bookPlot = bookPlot.replaceAll("(?i)" + lastName.substring(1), " ... ");
            bookPlot = bookPlot.replaceAll("(?i)" + firstName, " ... ");

            if (onCreate == 0){
                setToViews();
                onCreate = 1;
            } else {
                nextButton.setVisibility(View.VISIBLE);
                if (chosenAnswer == null){
                    nextButton.setEnabled(false);
                }
            }
        }
    }


    /**
     * Answers
     * Checks which answer button is clicked.
     */
    public void onAnswerClicked(View view) {
        switch (view.getId()) {
            case R.id.answer1:
                chosenAnswer = answer1Button.getText().toString();
                break;
            case R.id.answer2:
                chosenAnswer = answer2Button.getText().toString();
                break;
            case R.id.answer3:
                chosenAnswer = answer3Button.getText().toString();
                break;
            default:
                throw new RuntimeException("Unknown button ID");
        }

        checkAnswers();
        changeButtons();

        if (nextButton.getVisibility() == View.VISIBLE){
            nextButton.setEnabled(true);
        }
    }


    /**
     * Answers
     * Checks if the answer the user has given is correct.
     */
    public void checkAnswers(){
        if (chosenAnswer.equals(rightAnswer)){
            streak +=1;
        } else {
            checkHighScores();
            streak = 0;
        }

        SharedPreferences prefs = this.getSharedPreferences("streaks", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("streak", streak);
        editor.apply();
        streakTextView.setText(String.valueOf(streak));
    }


    /**
     * Answers
     * Checks the streak against the current high scores and save these to shared preferences.
     */
    public void checkHighScores() {
        loadHighScoreFromSharedPrefs();

        SharedPreferences prefs = this.getSharedPreferences("highScores", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if (first < streak){
            editor.putInt("second", first);
            editor.putInt("third", second);
            editor.putInt("first", streak);
        }
        else if (second < streak){
            editor.putInt("third", second);
            editor.putInt("second", streak);
        }
        else if (third < streak){
            editor.putInt("third",streak);
        }

        editor.apply();
    }


    /**
     * Answers
     * Changes background color of the button with the right answer.
     */
    public void changeButtons(){
        int rightColor = ContextCompat.getColor(this, R.color.rightButton);

        // change background color of button with right answer.
        if (answer1Button.getText().equals(rightAnswer)){
            answer1Button.setBackgroundColor(rightColor);
        } else if (answer2Button.getText().equals(rightAnswer)){
            answer2Button.setBackgroundColor(rightColor);
        } else {
            answer3Button.setBackgroundColor(rightColor);
        }

        answer1Button.setEnabled(false);
        answer2Button.setEnabled(false);
        answer3Button.setEnabled(false);
    }


    /**
     * Next
     * Takes care of a new question.
     */
    public void onNextClicked(View view){
        setToViews();
        checkConnection();

    }


    /**
     * Next
     * Sets the plot and answers to their views.
     */
    public void setToViews(){
        restoreBeginSettings();

        plotTextView.setText(bookPlot);
        wrong1 = titles[rand.nextInt(titles.length)];
        wrong2 = titles[rand.nextInt(titles.length)];

        while (wrong1.equals(selectedBook)){
            Log.d("log","loop");
            wrong1 = titles[rand.nextInt(titles.length)];
        }

        while (wrong2.equals(selectedBook)){
            Log.d("log","loop2");

            wrong2 = titles[rand.nextInt(titles.length)];
        }

        int n = rand.nextInt(3);

        if (n == 0){
            answer1Button.setText(wrong1);
            answer2Button.setText(wrong2);
            answer3Button.setText(selectedBook);
        } else if(n == 1) {
            answer1Button.setText(wrong1);
            answer2Button.setText(selectedBook);
            answer3Button.setText(wrong2);
        } else {
            answer1Button.setText(selectedBook);
            answer2Button.setText(wrong2);
            answer3Button.setText(wrong1);
        }

        rightAnswer = selectedBook;
        chosenAnswer = null;
        Log.d("log",rightAnswer);
        bookPlot = null;
        bookSearch();
    }


    /**
     * Next
     * Restore begin settings after next button is clicked.
     */
    private void restoreBeginSettings() {
        int backgroundColor = ContextCompat.getColor(this, R.color.backgroundButton);

        nextButton.setVisibility(View.INVISIBLE);
        answer1Button.setBackgroundColor(backgroundColor);
        answer2Button.setBackgroundColor(backgroundColor);
        answer3Button.setBackgroundColor(backgroundColor);

        plotTextView.scrollTo(0, 0);
        answer1Button.setEnabled(true);
        answer2Button.setEnabled(true);
        answer3Button.setEnabled(true);
    }


    /**
     * Shared Preferences
     * Loads the high scores from shared preferences.
     */
    public void loadHighScoreFromSharedPrefs() {
        SharedPreferences prefs = this.getSharedPreferences("highScores", MODE_PRIVATE);

        first = prefs.getInt("first", 0);
        second = prefs.getInt("second", 0);
        third = prefs.getInt("third",0);
    }


    /**
     * Shared preferences
     * Loads streak from shared preferences.
     */
    public void loadStreakFromSharedPrefs(){
        SharedPreferences prefs = this.getSharedPreferences("streaks", MODE_PRIVATE);

        streak = prefs.getInt("streak", 0);

        streakTextView.setText(String.valueOf(streak));
    }


    /**
     * Menu
     * Creates high score button.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.highscore_menu, menu);
        return true;
    }


    /**
     * Menu
     * Implements functionality of high score  button.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        loadHighScoreFromSharedPrefs();
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("High Score");
        alertDialog.setMessage("1.\t" + first + "\n2.\t" + second + "\n3.\t" + third);
        alertDialog.setButton("Back", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
            }
        });
        alertDialog.show();

        return true;
    }
}
