package com.example.assistantapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProfessorActivity extends AppCompatActivity {

    private Button button1;
    private Button button2;
    private Button button3;
    private EditText profEdit;
    private EditText offEdit;
    private EditText phoneEdit;
    private EditText emailEdit;
    String name;
    String officelink;
    String phonelink;
    String emaillink;
    public long now;
    public Date mDate;
    public SimpleDateFormat simpleDate;
    public String formatDate;
    String curName;
    String myToken;
    Activity a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor);
        setTitle("연구실 관리 시스템");
        a = ProfessorActivity.this;
        //현재 시간
        now = System.currentTimeMillis() + 32400000;
        mDate = new Date(now);
        simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        formatDate = simpleDate.format(mDate);
        //현재 로그인 정보
        SharedPreferences preferences = getSharedPreferences("login",MODE_PRIVATE);
        String curID = preferences.getString("ID","0");
        curName = preferences.getString("Name","0");
        myToken = preferences.getString("Token",null);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        profEdit = findViewById(R.id.profEdit);
        offEdit = findViewById(R.id.offEdit);
        phoneEdit = findViewById(R.id.phoneEdit);
        emailEdit = findViewById(R.id.emailEdit);

        final String ser = ((ServerVariable)getApplicationContext()).getSer();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = profEdit.getText().toString();
                new JSONTask().execute(ser+"/professor/"+name);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JSONTask2().execute(ser+"/professor/"+name);
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
            JSONObject jo = jsonParsing2(result);
            String pname="";
            String office="";
            String phone="";
            String email="";

            try {
                pname += jo.getString("name");
                office += jo.getString("classPosition");
                phone += jo.getString("phoneNumber");
                email += jo.getString("email");
            } catch (JSONException e) {
                e.printStackTrace();
                
            }

            profEdit.setText(pname);
            offEdit.setText(office);
            phoneEdit.setText(phone);
            emailEdit.setText(email);
        }

    }
    public class JSONTask2 extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... urls) {
            try {
                officelink = offEdit.getText().toString();
                phonelink = phoneEdit.getText().toString();
                emaillink = emailEdit.getText().toString();

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("classPosition", officelink);
                jsonObject.accumulate("phoneNumber", phonelink);
                jsonObject.accumulate("email",emaillink);

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
                    con.setRequestProperty("Authorization", myToken);
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.connect();

                    OutputStream outStream = con.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
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
            offEdit.setText(""+officelink);
            phoneEdit.setText(""+phonelink);
            emailEdit.setText(""+emaillink);
        }
    }


    public JSONObject jsonParsing2(String json)
    {
        JSONObject jo = null;
        JSONObject resultjo = null;
        try {
            jo = new JSONObject(json);
            resultjo = jo.getJSONObject("result");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultjo;
    }
}