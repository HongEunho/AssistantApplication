package com.example.assistantapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Date;

public class FAQActivity extends AppCompatActivity {
    //자주묻는질문 TOP3 액티비티 입니다.
    private Button fButton1;
    private Button fButton2;
    private Button fButton3;
    private EditText top1Edit;
    private EditText top2Edit;
    private EditText top3Edit;
    String top1;
    String top2;
    String top3;
    public long now;
    public Date mDate;
    public SimpleDateFormat simpleDate;
    public String formatDate;
    String curName;
    String myToken;
    int flag = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_a_q);

        final Activity a = FAQActivity.this;

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

        fButton1 = findViewById(R.id.fButton1);
        fButton2 = findViewById(R.id.fButton2);
        fButton3 = findViewById(R.id.fButton3);
        top1Edit = findViewById(R.id.top1Edit);
        top2Edit = findViewById(R.id.top2Edit);
        top3Edit = findViewById(R.id.top3Edit);

        final String ser = ((ServerVariable)getApplicationContext()).getSer();

        new JSONTask().execute(ser+"/bestqa");
        fButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=1;
                new JSONTask2().execute(ser+"/bestqa/1");
                ((ServerVariable)getApplicationContext()).Cookie(a);
            }
        });
        fButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=2;
                new JSONTask2().execute(ser+"/bestqa/2");
                ((ServerVariable)getApplicationContext()).Cookie(a);
            }
        });
        fButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=3;
                new JSONTask2().execute(ser+"/bestqa/3");
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

            String link01="";
            String link02="";
            String link03="";
            JSONArray ja = jsonParsing(result);
            try {
                JSONObject jo1 = ja.getJSONObject(0);
                JSONObject jo2 = ja.getJSONObject(1);
                JSONObject jo3 = ja.getJSONObject(2);

                link01 += jo1.getString("question");
                link02 += jo2.getString("question");
                link03 += jo3.getString("question");

                if(link01.equals(""))
                    link01="수정 중입니다.";
                if(link02.equals(""))
                    link02="수정 중입니다.";
                if(link03.equals(""))
                    link03="수정 중입니다.";

            } catch (JSONException e) {
                e.printStackTrace();
            }

            top1Edit.setText(link01);
            top2Edit.setText(link02);
            top3Edit.setText(link03);
        }

    }

    public class JSONTask2 extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... urls) {
            try {
                top1 = top1Edit.getText().toString();
                top2 = top2Edit.getText().toString();
                top3 = top3Edit.getText().toString();

                JSONObject jsonObject0 = new JSONObject();
                if(flag == 1)
                    jsonObject0.accumulate("question", top1);
                else if(flag == 2)
                    jsonObject0.accumulate("question", top2);
                else if(flag == 3)
                    jsonObject0.accumulate("question", top3);


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
            if(flag == 1)
                top1Edit.setText(""+top1);
            else if(flag == 2)
                top2Edit.setText(""+top2);
            else if(flag == 3)
                top3Edit.setText(""+top3);
        }
    }

    public JSONArray jsonParsing(String json)
    {
        JSONObject jo = null;
        JSONArray ja = null;
        try {
            jo = new JSONObject(json);
            ja = jo.getJSONArray("result");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ja;
    }

}