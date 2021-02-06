package com.example.assistantapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

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

public class NoticeActivity extends AppCompatActivity {

    private EditText linkEdit;
    private Button button;
    private TextInputLayout inputLayout;
    private EditText contentEdit;
    Activity a;

    String link2;
    String content2;
    String department;
    String dep;
    String myToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        //현재 로그인 정보
        SharedPreferences preferences = getSharedPreferences("login",MODE_PRIVATE);
        dep = preferences.getString("Department", "9999");
        myToken = preferences.getString("Token", null);

        a = NoticeActivity.this;
        final String ser = ((ServerVariable)getApplicationContext()).getSer();

        setTitle("학과공지 관리 시스템");
        linkEdit = findViewById(R.id.linkEdit2);
        button = findViewById(R.id.button);
        inputLayout = findViewById(R.id.contentEditLayout);

        inputLayout.setCounterEnabled(true);
        inputLayout.setCounterMaxLength(200);
        contentEdit = inputLayout.getEditText();
        contentEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 200)
                    inputLayout.setError("50글자 이상을 넘기면 안됩니다");
                else
                    inputLayout.setError(null);
            }
        });

        new JSONTask().execute(ser+"/notice/"+dep);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JSONTask2().execute(ser+"/notice/"+dep);
                //Toast.makeText(getApplicationContext(),"수정 되었습니다",Toast.LENGTH_SHORT).show();
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

                //System.out.println(buffer.toString());
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
            JSONObject jo = jsonParsing(result);
            String link="";
            String content="";
            String createdAt="";
            String updatedAt="";
            String department="";
            String username="";

            try {
                link += jo.getString("link");
                content += jo.getString("content");
                createdAt += jo.getString("createdAt");
                updatedAt += jo.getString("updatedAt");
                department += jo.getString("department");
                JSONObject userjo = jo.getJSONObject("User");
                username += userjo.getString("username");

                if(link.equals("null"))
                    link="수정 중입니다.";
            } catch (JSONException e) {
                e.printStackTrace();
            }
            linkEdit.setText(link);
            contentEdit.setText(content);
        }

    }
    public class JSONTask2 extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... urls) {
            try {
                link2 = linkEdit.getText().toString();
                content2 = contentEdit.getText().toString();

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("link", link2);
                jsonObject.accumulate("content", content2);

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
            linkEdit.setText(""+link2);
            contentEdit.setText(""+content2);
        }
    }

    public JSONObject jsonParsing(String json)
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