package com.example.assistantapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ComputerTimeActivity extends AppCompatActivity {

    private EditText linkEdit2;
    private EditText linkEdit3;
    private EditText linkEdit4;
    private EditText linkEdit5;
    private EditText linkEdit11;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    String link2="";
    String link3="";
    String link4="";
    String link5="";
    String link11="";
    String allresult="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_computer_time);

        final Activity a = ComputerTimeActivity.this;

        setTitle("강의실 조교관리 시스템");
        linkEdit2 = findViewById(R.id.linkEdit2);
        linkEdit3 = findViewById(R.id.linkEdit3);
        linkEdit4 = findViewById(R.id.linkEdit4);
        linkEdit5 = findViewById(R.id.linkEdit5);
        linkEdit11 = findViewById(R.id.linkEdit11);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);

        final String ser = ((ServerVariable)getApplicationContext()).getSer();

        new JSONTask().execute(ser+"/timetable");
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JSONTask1().execute(ser+"/timetable/센B102");
                ((ServerVariable)getApplicationContext()).Cookie(a);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JSONTask2().execute(ser+"/timetable/센B103");
                ((ServerVariable)getApplicationContext()).Cookie(a);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JSONTask3().execute(ser+"/timetable/센B104");
                ((ServerVariable)getApplicationContext()).Cookie(a);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JSONTask4().execute(ser+"/timetable/센B105");
                ((ServerVariable)getApplicationContext()).Cookie(a);
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JSONTask5().execute(ser+"/timetable/센B111");
                ((ServerVariable)getApplicationContext()).Cookie(a);
            }
        });
    }

    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection con = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(urls[0]);

                con = (HttpURLConnection)url.openConnection();
                con.connect();

                InputStream stream = con.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";

                while ((line = reader.readLine()) != null) buffer.append(line);

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                con.disconnect();
                try {
                    if(reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            allresult += result;
            JSONObject jo = jsonParsing2(result);
            JSONObject jo2 = jsonParsing3(result);
            JSONObject jo3 = jsonParsing4(result);
            JSONObject jo4 = jsonParsing5(result);
            JSONObject jo5 = jsonParsing11(result);

            String link01="";
            String link02="";
            String link03="";
            String link04="";
            String link05="";

            try {
                link01 += jo.getString("link");
                link02 += jo2.getString("link");
                link03 += jo3.getString("link");
                link04 += jo4.getString("link");
                link05 += jo5.getString("link");

                if(link01.equals(""))
                    link01="수정 중입니다.";
                if(link02.equals(""))
                    link02="수정 중입니다.";
                if(link03.equals(""))
                    link03="수정 중입니다.";
                if(link04.equals(""))
                    link04="수정 중입니다.";
                if(link05.equals(""))
                    link05="수정 중입니다.";

            } catch (JSONException e) {
                e.printStackTrace();
            }
            linkEdit2.setText(link01);
            linkEdit3.setText(link02);
            linkEdit4.setText(link03);
            linkEdit5.setText(link04);
            linkEdit11.setText(link05);
        }

    }
    public class JSONTask1 extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... urls) {
            try {
                link2 = linkEdit2.getText().toString();

                JSONObject jsonObject0 = new JSONObject();
                jsonObject0.accumulate("link", link2);


                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("PUT");
                    con.setRequestProperty("Cache-Control", "no-cache");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Accept", "text/html");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.connect();

                    OutputStream outStream = con.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject0.toString());
                    writer.flush();
                    writer.close();

                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }

                    return buffer.toString();

                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        if(reader != null){
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            linkEdit2.setText(""+link2);

        }
    }

    public class JSONTask2 extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... urls) {
            try {
                link3 = linkEdit3.getText().toString();

                JSONObject jsonObject0 = new JSONObject();
                jsonObject0.accumulate("link", link3);


                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("PUT");
                    con.setRequestProperty("Cache-Control", "no-cache");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Accept", "text/html");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.connect();

                    OutputStream outStream = con.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject0.toString());
                    writer.flush();
                    writer.close();

                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }

                    return buffer.toString();

                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        if(reader != null){
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            linkEdit3.setText(""+link3);

        }
    }
    public class JSONTask3 extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... urls) {
            try {
                link4 = linkEdit4.getText().toString();

                JSONObject jsonObject0 = new JSONObject();
                jsonObject0.accumulate("link", link4);


                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("PUT");
                    con.setRequestProperty("Cache-Control", "no-cache");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Accept", "text/html");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.connect();

                    OutputStream outStream = con.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject0.toString());
                    writer.flush();
                    writer.close();

                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }

                    return buffer.toString();

                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        if(reader != null){
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            linkEdit4.setText(""+link4);

        }
    }
    public class JSONTask4 extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... urls) {
            try {
                link5 = linkEdit5.getText().toString();

                JSONObject jsonObject0 = new JSONObject();
                jsonObject0.accumulate("link", link5);


                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("PUT");
                    con.setRequestProperty("Cache-Control", "no-cache");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Accept", "text/html");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.connect();

                    OutputStream outStream = con.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject0.toString());
                    writer.flush();
                    writer.close();

                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }

                    return buffer.toString();

                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        if(reader != null){
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            linkEdit5.setText(""+link5);

        }
    }
    public class JSONTask5 extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... urls) {
            try {
                link11 = linkEdit11.getText().toString();

                JSONObject jsonObject0 = new JSONObject();
                jsonObject0.accumulate("link", link11);


                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("PUT");
                    con.setRequestProperty("Cache-Control", "no-cache");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Accept", "text/html");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.connect();

                    OutputStream outStream = con.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject0.toString());
                    writer.flush();
                    writer.close();

                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }

                    return buffer.toString();

                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        if(reader != null){
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            linkEdit11.setText(""+link11);

        }
    }

    public JSONObject jsonParsing2(String json)
    {
        JSONObject jo = null;
        try {
            JSONArray ja = new JSONArray(json);
            jo = ja.getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo;
    }
    public JSONObject jsonParsing3(String json)
    {
        JSONObject jo = null;
        try {
            JSONArray ja = new JSONArray(json);
            jo = ja.getJSONObject(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo;
    }
    public JSONObject jsonParsing4(String json)
    {
        JSONObject jo = null;
        try {
            JSONArray ja = new JSONArray(json);
            jo = ja.getJSONObject(2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo;
    }
    public JSONObject jsonParsing5(String json)
    {
        JSONObject jo = null;
        try {
            JSONArray ja = new JSONArray(json);
            jo = ja.getJSONObject(3);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo;
    }
    public JSONObject jsonParsing11(String json)
    {
        JSONObject jo = null;
        try {
            JSONArray ja = new JSONArray(json);
            jo = ja.getJSONObject(4);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo;
    }
}
