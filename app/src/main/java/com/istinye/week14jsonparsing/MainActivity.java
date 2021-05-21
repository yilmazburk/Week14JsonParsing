package com.istinye.week14jsonparsing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private String url = "https://jsonplaceholder.typicode.com/users";
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.myTextView);

        new JsonDataFetcherTask().execute(url);
    }


    public class JsonDataFetcherTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            URL url;
            BufferedReader bufferedReader = null;

            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                inputStream = new BufferedInputStream(urlConnection.getInputStream());

                StringBuffer buffer = new StringBuffer();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    buffer.append(line);
                }

                if (buffer.length() == 0) {
                    return null;
                }
                return buffer.toString();
            } catch (Exception ex) {
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        return null;
                    }
                }
            }


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String jsonData) {

            if (jsonData != null) {
                try {
                    JSONArray jsonArray = new JSONArray(jsonData);

                    if (jsonArray.length() != 0) {
                        JSONObject user = (JSONObject) jsonArray.get(0);
                        if (user != null) {
                            JSONObject address = user.getJSONObject("address");
                            if (address != null) {
                                JSONObject geo = address.getJSONObject("geo");
                                if (geo != null) {
                                    String latitude = geo.getString("lat");
                                    String longitude = geo.getString("lng");
                                    textView.setText("Lat: " + latitude + "\n" + "Lng: " + longitude);
                                }
                            }
                        }
                    }

                    Log.d("JSON", jsonArray.toString());
                } catch (JSONException e) {

                }

            }

        }
    }

}