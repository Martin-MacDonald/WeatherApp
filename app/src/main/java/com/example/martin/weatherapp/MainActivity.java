package com.example.martin.weatherapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText weatherInput;
    TextView conditionTextView;
    TextView tempTextView;
    final String API_KEY = "&APPID=add13a121c43de5cb536920dfac5ad50";
    final String URL = "http://api.openweathermap.org/data/2.5/weather?q=";

    public void getWeather(View view){

        String city = weatherInput.getText().toString();

        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(weatherInput.getWindowToken(), 0);

        if (city.isEmpty()){
            Toast.makeText(this, "Please enter a city", Toast.LENGTH_SHORT).show();
            tempTextView.setVisibility(View.INVISIBLE);
            conditionTextView.setVisibility(View.INVISIBLE);
        } else{
            WeatherData task = new WeatherData();
            task.execute(URL + city + API_KEY);
        }


    }

    private class WeatherData extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection;

            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1){

                    char content = (char) data;
                    result += content;
                    data = reader.read();
                }

                return result;
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Could not find weather!", Toast.LENGTH_SHORT).show();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject weatherData = new JSONObject(s);
                String weatherType = weatherData.getJSONArray("weather").getJSONObject(0).getString("main");
                int temp = weatherData.getJSONObject("main").getInt("temp") - 273;

                conditionTextView.setText(weatherType);
                tempTextView.setText(Integer.toString(temp) + "°C");

                conditionTextView.setVisibility(View.VISIBLE);
                tempTextView.setVisibility(View.VISIBLE);


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherInput = (EditText) findViewById(R.id.weatherInput);
        conditionTextView = (TextView) findViewById(R.id.conditionTextView);
        tempTextView = (TextView) findViewById(R.id.tempTextView);


    }
}
