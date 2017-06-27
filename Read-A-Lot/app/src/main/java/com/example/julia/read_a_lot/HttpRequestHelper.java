/*
 * Handles the request to the API.
 * Created by Julia Anten.
 * Open source
 */

package com.example.julia.read_a_lot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class HttpRequestHelper {
    static String downloadFromServer(String... params) {
        String APIKey = "AIzaSyBmD3yV5rFMTSB9gyOsB8qbrUd5InjWSM4";
        String result = "";
        String selectedBook = params[0];

        // make the url
        selectedBook = selectedBook.replace(" ", "+");
        String titleAndAuthor[] = selectedBook.split(",");
        URL url = null;

        try {
            url = new URL("https://www.googleapis.com/books/v1/volumes?q=intitle:\"" + titleAndAuthor[0] +
                    "\"&inauthor:" + titleAndAuthor[1] + "&printType=books&key=" + APIKey);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection connect;

        // connect to the API
        if (url != null) {
            try {
                connect = (HttpURLConnection) url.openConnection();

                connect.setRequestMethod("GET");
                Integer responseCode = connect.getResponseCode();

                if (responseCode == 200) {
                    BufferedReader bReader =
                            new BufferedReader(new InputStreamReader(connect.getInputStream()));
                    String line;

                    while ((line = bReader.readLine()) != null) {
                        result += line;
                    }
                } else {
                    result = "wrong";
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
