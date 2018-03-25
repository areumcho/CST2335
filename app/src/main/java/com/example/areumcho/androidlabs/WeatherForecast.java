package com.example.areumcho.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.lang.String;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class WeatherForecast extends Activity {

    protected static final String ACTIVITY_NAME = "WeatherForecast";

    TextView currentTempTV;
    TextView maxTempTV;
    TextView minTempTV;
    ImageView weatherImg;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        currentTempTV = (TextView) findViewById(R.id.currentTemp);
        maxTempTV = (TextView) findViewById(R.id.maxTemp);
        minTempTV = (TextView) findViewById(R.id.minTemp);
        weatherImg = (ImageView) findViewById(R.id.weatherImage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        // progressBar's visibility to View.Visible
       progressBar.setVisibility(View.VISIBLE);

        new ForecastQuery().execute();


    } // end of onCreae


    public class ForecastQuery extends AsyncTask<String, Integer, String> {


        String currentTemp;
        String maxTemp;
        String minTemp;
        String windSpeed;
        String currentWeather;
        private Bitmap bitmap;

//        private static final String OPEN_WEATHER_MAP_API =
//                "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";



        @Override
        protected String doInBackground(String... strings) {

            InputStream streamInput;


            try {

                //URL url = new URL(String.format(OPEN_WEATHER_MAP_API));


                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();

                streamInput = conn.getInputStream();

                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(streamInput, null);
                parser.nextTag();

                int eventTypeWeather = parser.getEventType();

               while (eventTypeWeather != XmlPullParser.END_DOCUMENT) {
                   //while(parser.next() != XmlPullParser.END_TAG) {

                   if (eventTypeWeather != XmlPullParser.START_TAG) {
                       eventTypeWeather = parser.next();
                       continue;
                   } else {
                       if (parser.getName().equals("temperature")) {
                           currentTemp = parser.getAttributeValue(null, "value");

                           publishProgress(25);
                           maxTemp = parser.getAttributeValue(null, "max");

                           publishProgress(50);
                           minTemp = parser.getAttributeValue(null, "min");

                           publishProgress(75);
                           windSpeed = parser.getAttributeValue(null, "speed");

                       } else if (parser.getName().equals("weather")) {

                           currentWeather = parser.getAttributeValue(null, "icon");
                       }
                       eventTypeWeather = parser.next();
                   }
               } // end of while

           conn.disconnect();

                //looking for a weather image and if it exists
                if (fileExistance(currentWeather + ".png")) {

                    Log.i(ACTIVITY_NAME, "Image exists, reading from file");

                    File file = getBaseContext().getFileStreamPath(currentWeather + ".png");
                    FileInputStream fis = new FileInputStream(file);
                    bitmap = BitmapFactory.decodeStream(fis);

                } else { //imagine doesn't exist.

                    Log.i(ACTIVITY_NAME, "Image doesn't exists");

                    URL imageUrl = new URL("http://openweathermap.org/img/w/" + currentWeather+ ".png");
                    conn = (HttpURLConnection) imageUrl.openConnection();
                    conn.connect();
                    streamInput = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(streamInput);

                    FileOutputStream outputStream = openFileOutput(currentWeather + ".png", Context.MODE_PRIVATE);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);

                    outputStream.flush();
                    outputStream.close();

                    conn.disconnect();
                }

                publishProgress(100);



            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }


            return null;
        } // end of doInBackground()


        public boolean fileExistance(String name) {
                File file = getBaseContext().getFileStreamPath(name);
                return file.exists();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

// update the GUI components with the min, max, and current temperature
            currentTempTV.setText("Current temperature is " + currentTemp + "celcius ");
            maxTempTV.setText("Max temperature is " + maxTemp + "celcius ");
            minTempTV.setText("Min temperature is " + minTemp + "celcius ");
            weatherImg.setImageBitmap(bitmap);
            progressBar.setVisibility(View.INVISIBLE);

        }




        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);

        }


    } // end of forecastQuery


        } // end of activity
