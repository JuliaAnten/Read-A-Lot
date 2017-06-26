/*
 * Handles the request to the API.
 * Created by Julia Anten.
 */

package com.example.julia.read_a_lot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Requests the book information from the API.
 * Created by Julia on 13/06/2017.
 */

class HttpRequestHelper {
    public static String downloadFromServer(String... params) {
        String result = "";
        String selectedBook = params[0];

        selectedBook = selectedBook.replace(" ", "+");
        String titleAndAuthor[] = selectedBook.split(",");


        URL url = null;
        try {
            url = new URL("https://www.googleapis.com/books/v1/volumes?q=intitle:\"" + titleAndAuthor[0] + "\"&inauthor:" + titleAndAuthor[1] + "&printType=books&key=AIzaSyBmD3yV5rFMTSB9gyOsB8qbrUd5InjWSM4");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection connect;


        if (url != null) {

            try {
                connect = (HttpURLConnection) url.openConnection();

                connect.setRequestMethod("GET");
                Integer responseCode = connect.getResponseCode();

                if (responseCode == 200){

                    BufferedReader bReader = new BufferedReader(new InputStreamReader(connect.getInputStream()));
                    String line;

                    while ((line = bReader.readLine()) != null){
                        result += line;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
