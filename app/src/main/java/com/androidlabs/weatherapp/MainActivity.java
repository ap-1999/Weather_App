package com.androidlabs.weatherapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
EditText edittext;
    String cityname;
    TextView t1,t2,t3,t4;

    public void findWeather(View view){
        edittext=(EditText)findViewById(R.id.editText);
        t1=(TextView)findViewById(R.id.textView);
        t2=(TextView)findViewById(R.id.textView2);
        t3=(TextView)findViewById(R.id.textView3);
        t4=(TextView)findViewById(R.id.textView4);
        cityname=edittext.getText().toString();
        InputMethodManager mgr=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(edittext.getWindowToken(),0);
        DownloadTask task=new DownloadTask();
        task.execute("http://api.openweathermap.org/data/2.5/weather?q="+cityname+"&APPID=84723c1082193092912c1ac0fe6ebe97");

    }
    public class DownloadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            URL url;
            HttpURLConnection connection=null;
            String result="";
            try {
                url=new URL(urls[0]);
                connection=(HttpURLConnection)url.openConnection();

                InputStream in=connection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while(data != -1)
                {
                    char current=(char)data;
                    result+=current;
                    data=reader.read();
                }
                return result;

            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            JSONObject jsonobject= null;
            try {
                jsonobject = new JSONObject(result);


                JSONArray arr=new JSONArray(jsonobject.getString("weather"));

                for(int i=0;i<arr.length();i++)
                {
                    JSONObject parsejson=arr.getJSONObject(i);
                    t3.setText(( parsejson.getString("main"))+":"+(parsejson.getString("description")));
                }
                jsonobject=new JSONObject(jsonobject.getString("main"));
                double j= ((Double.valueOf(jsonobject.getString("temp")))-273.15);
                double k= ((Double.valueOf(jsonobject.getString("temp_max")))-273.15);
                double l= ((Double.valueOf(jsonobject.getString("temp_min")))-273.15);
                t1.setText((Double.toString(j)).substring(0,4)+"°C");
                t4.setText("Humidity : "+jsonobject.getString("humidity") + "%");
                t2.setText((Double.toString(k)).substring(0, 4) + "°C" + "/" + (Double.toString(l)).substring(0, 4) + "°C");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edittext=(EditText)findViewById(R.id.editText);
       t1=(TextView)findViewById(R.id.textView);
        t2=(TextView)findViewById(R.id.textView2);
        t3=(TextView)findViewById(R.id.textView3);
        t4=(TextView)findViewById(R.id.textView4);
    }
}
