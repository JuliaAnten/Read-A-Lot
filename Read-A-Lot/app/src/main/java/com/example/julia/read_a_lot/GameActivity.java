package com.example.julia.read_a_lot;

import android.app.AlertDialog;
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

    String titles[];
    String selectedBook = "";
    String bookPlot;
    String chosenAnswer;
    String wrong1;
    String wrong2;

    JSONObject bookObject;
    Random rand = new Random();
    int streak = 0;

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

        plotTextView = (TextView) findViewById(R.id.plotView);
        plotTextView.setMovementMethod(new ScrollingMovementMethod());

        streakTextView = (TextView) findViewById(R.id.streakView);
        streakTextView.setText(String.valueOf(0));

        answer1Button = (Button) findViewById(R.id.answer1);
        answer2Button = (Button) findViewById(R.id.answer2);
        answer3Button = (Button) findViewById(R.id.answer3);
        nextButton = (ImageButton) findViewById(R.id.nextButton);

        titles = getResources().getText(R.string.booktitles).toString().split("=");
        bookSearch();
    }



    /**
     * Sends a request to the AsyncTask to request a book.
     */
    public void bookSearch(){

        int backgroundColor = ContextCompat.getColor(this, R.color.backgroundButton);

        // restore begin settings after next button is used
        nextButton.setVisibility(View.INVISIBLE);
        answer1Button.setBackgroundColor(backgroundColor);
        answer2Button.setBackgroundColor(backgroundColor);
        answer3Button.setBackgroundColor(backgroundColor);
        bookPlot = null;

        // Get book bookTitle
        selectedBook = titles[rand.nextInt(titles.length)];
        BookAsyncTask asyncTask = new BookAsyncTask(this);
        asyncTask.execute(selectedBook);
    }


    /**
     * Filters the book info after getting it back from the AsyncTask.
     * first search for the title in the request than get the info
     */
    public void handleBookInfo(String bookInfo) {


        try {
            bookObject = new JSONObject(bookInfo);
            JSONArray itemsArray = bookObject.getJSONArray("items");
            JSONObject[] volumeObject = new JSONObject[itemsArray.length()];

            // get volumeInfo from itemsArray
            for (int i = 0; i < itemsArray.length(); i++){
                try{
                    JSONObject object = itemsArray.getJSONObject(i);
                    volumeObject[i] = object.getJSONObject("volumeInfo");
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }

            for (JSONObject object : volumeObject){
                String titleAndAuthor[] = selectedBook.split(",");


                JSONArray authors = null;
                
                Log.d("log",object.getString("title"));
                Log.d("log",titleAndAuthor[0].substring(1));
                try{
                    authors = object.getJSONArray("authors");
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                

                Log.d("log",titleAndAuthor[1].substring(titleAndAuthor[1].lastIndexOf(" ")));


                if (authors != null){
                    Log.d("log","nee");
                    if (object.getString("title").toLowerCase().equals(titleAndAuthor[0].substring(1).toLowerCase()) & authors.toString().contains(titleAndAuthor[1].substring(titleAndAuthor[1].lastIndexOf(" "))) ){
                        Log.d("log","ja");
                        try{
                            Log.d("log","whoop");
                            bookPlot = object.getString("description");
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }

                        if (bookPlot == null){
                            bookSearch();
                        }
                        break;
                    }
                }

            }



//          Log.d("logselected",selectedBook.substring(0, selectedBook.indexOf(",")));
            //Log.d("logtitle", volumeObject.("title"));
            // get book plot for volumeObject

//                JSONObject = volumeObject.getString("description");
//                Log.d("log", volumeObject[1].toString());
//                for (int i = 0; i < bookObject.getInt("totalItems"); i++){
//                    if (volumeObject.getString("title").equals(selectedBook.substring(0, selectedBook.indexOf(","))))
//                        {
//                            Log.d("log","8");
//                            bookPlot = volumeObject.getString("description");
//                            Log.d("log",bookPlot);
//                        }
//
//                }
//                while (!volumeObject.getString("title").equals(selectedBook.substring(0, selectedBook.indexOf(",")))){
//                    Log.d("logtitle", volumeObject.getString("title"));
//
//                }

//                if (volumeObject.getString("title").equals(selectedBook.substring(0, selectedBook.indexOf(","))))
//                {
//                    Log.d("log","8");
//                    bookPlot = volumeObject.getString("description");
//                }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        setToViews();
    }


    /**
     * Sets the plot and answer to their views.
     */
    public void setToViews(){

        plotTextView.setText(bookPlot);

        wrong1 = titles[rand.nextInt(titles.length)];
        wrong2 = titles[rand.nextInt(titles.length)];

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

    }

    /**
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
    }


    /**
     *  Checks if the answer the user has given is correct.
     *  Should also add this to shared preferences.
     */
    public void checkAnswers(){

        if (chosenAnswer.equals(selectedBook)){
            streak +=1;
        } else {
            streak = 0;
        }

        streakTextView.setText(String.valueOf(streak));

    }


    /**
     * Checks what is the right answer and makes next button visible.
     */
    public void changeButtons(){

        int rightColor = ContextCompat.getColor(this, R.color.rightButton);

        // change background color of button with right answer.
        if (answer1Button.getText().equals(selectedBook)){
            answer1Button.setBackgroundColor(rightColor);
        } else if (answer2Button.getText().equals(selectedBook)){
            answer2Button.setBackgroundColor(rightColor);
        } else {
            answer3Button.setBackgroundColor(rightColor);
        }

        nextButton.setVisibility(View.VISIBLE);
    }


    /**
     * Takes care of a new question.
     */
    public void onNextClicked(View view){
        bookSearch();
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
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage("highscore");
        alertDialog.show();

        return true;
    }
}
