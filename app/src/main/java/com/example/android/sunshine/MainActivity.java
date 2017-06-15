/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.android.sunshine.utilities.*;
import com.example.android.sunshine.data.*;
import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "mainactivity";
    private ProgressBar progress;
    private EditText search;
    private TextView textView;

    private TextView mWeatherTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);


//        mWeatherTextView = (TextView) findViewById(R.id.tv_weather_data);

        // TODO X(4) Delete the dummy weather data. You will be getting REAL data from the Internet in this lesson.
        // TODO X(3) Delete the for loop that populates the TextView with dummy data

        textView=(TextView)findViewById(R.id.tv_weather_data);
        loadWeatherData();

        // TODO X(9) Call loadWeatherData to perform the network request to get the weather
    }

    // TODO X(8) Create a method that will get the user's preferred location and execute your new AsyncTask and call it loadWeatherData
    protected void loadWeatherData(){
        String preferredLocation = SunshinePreferences.getPreferredWeatherLocation(this);
        NetworkTask task = new NetworkTask(preferredLocation);
        task.execute();
    }
    // TODO X(5) Create a class that extends AsyncTask to perform network requests

    class NetworkTask extends AsyncTask<URL, Void, String[]> {
        String query;

        NetworkTask(String s){
            query = s;
        }

        @Override
        protected String[] doInBackground(URL... params) {

            String result=null;
            String[] resultJSON=null;
            URL url = NetworkUtils.buildUrl(query);
            try {
                result = NetworkUtils.getResponseFromHttpUrl(url);
                resultJSON = OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(MainActivity.this,result);
            }catch(Exception e){
                e.printStackTrace();
            }
            return resultJSON;
        }

        @Override
        protected void onPostExecute(String[] s) {
            super.onPostExecute(s);
            if(s == null){
                textView.setText("Sorry, no text was received");
            }
            else{
                for(String weatherString: s){
                    textView.append((weatherString)+"\n\n\n");
                }
            }
        }

    }
    // TODO X(6) Override the doInBackground method to perform your network requests
    // TODO X(7) Override the onPostExecute method to display the results of the network request
}