package com.example.julia.read_a_lot;

import android.os.Bundle;
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

    String titles[];
    String selectedBook = "";
    String bookPlot = null;
    String chosenAnswer;

    JSONObject bookObject;
    Random rand = new Random();

    Answer wrong1;
    Answer wrong2;
    Answer right;

    TextView plotTextView;
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

        // restore begin settings
        nextButton.setVisibility(View.INVISIBLE);
        answer1Button.setBackgroundColor(this.getResources().getColor(R.color.backgroundButton));
        answer2Button.setBackgroundColor(this.getResources().getColor(R.color.backgroundButton));
        answer3Button.setBackgroundColor(this.getResources().getColor(R.color.backgroundButton));

        // Get book bookTitle
        selectedBook = titles[rand.nextInt(103)];
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

            // get volumeInfo from itemsArray
            for (int i = 0; i < itemsArray.length(); i++){
                try{
                    JSONObject object = itemsArray.getJSONObject(i);
                    volumeObject = object.getJSONObject("volumeInfo");
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }

            // get book plot for volumeObject
            if (volumeObject != null) {
                bookPlot = volumeObject.getString("description");
            }

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

        wrong1 = new Answer(titles[rand.nextInt(103)], 1);
        wrong2 = new Answer(titles[rand.nextInt(103)], 1);
        right = new Answer(selectedBook, 0);

        int n = rand.nextInt(2);

        if (n == 0){
            answer1Button.setText(wrong1.getAnswer());
            answer2Button.setText(wrong2.getAnswer());
            answer3Button.setText(right.getAnswer());
        } else if(n == 1) {
            answer1Button.setText(wrong1.getAnswer());
            answer2Button.setText(right.getAnswer());
            answer3Button.setText(wrong2.getAnswer());
        } else {
            answer1Button.setText(right.getAnswer());
            answer2Button.setText(wrong2.getAnswer());
            answer3Button.setText(wrong1.getAnswer());
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
        checkAnswer();
    }

    public void checkAnswer(){
        if (answer1Button.getText().toString().equals(right.getAnswer())){
            answer1Button.setBackgroundColor(this.getResources().getColor(R.color.rightButton));
        } else if (answer2Button.getText().toString().equals(right.getAnswer())){
            answer2Button.setBackgroundColor(this.getResources().getColor(R.color.rightButton));
        } else {
            answer3Button.setBackgroundColor(this.getResources().getColor(R.color.rightButton));
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
        return true;
    }
}
