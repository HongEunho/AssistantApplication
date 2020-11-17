package com.example.assistantapplication;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ComputerStatusActivity extends AppCompatActivity {

    private EditText majorEdit;
    private EditText statusEdit;
    private EditText commentEdit;
    private RadioButton workBtn;
    private RadioButton notworkBtn;
    private RadioButton longnotBtn;
    private RadioGroup workingGroup;
    private Button button;
    int staint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_computer_status);

        final Activity a = ComputerStatusActivity.this;

        setTitle("컴퓨터공학과 조교관리 시스템");
        majorEdit = findViewById(R.id.majorEdit);
        statusEdit = findViewById(R.id.statusEdit);
        commentEdit = findViewById(R.id.commentEdit);
        workBtn = findViewById(R.id.workBtn);
        notworkBtn = findViewById(R.id.notworkBtn);
        longnotBtn = findViewById(R.id.longnotBtn);
        workingGroup = findViewById(R.id.workingGroup);
        button = findViewById(R.id.button);

        final String ser = ((ServerVariable)getApplicationContext()).getSer();

        new JSONTask().execute(ser+"/status/컴퓨터공학과");

        RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.workBtn)
                    staint = 0;
                else if(checkedId == R.id.notworkBtn)
                    staint = 1;
                else if(checkedId == R.id.longnotBtn)
                    staint = 2;
                else
                    staint = 0;
            }
        };
        workingGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JSONTask2().execute(ser+"/status/컴퓨터공학과");
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
            //String id = jsonParsing(result);
            JSONObject jo = jsonParsing2(result);
            //System.out.println("학과"+result);
            String dep="";
            String sta="";
            String comment = "";
            try {
                dep += jo.getString("department");
                sta += jo.getString("status");

                comment += jo.getString("comment");
                if(comment.equals("null"))
                    comment="없습니다";
            } catch (JSONException e) {
                e.printStackTrace();
            }
            majorEdit.setText(dep);
            //RadioButton으로 status나타내기
            staint = Integer.parseInt(sta);
            if(staint == 0) { workBtn.setChecked(true); notworkBtn.setChecked(false); longnotBtn.setChecked(false);      }
            else if(staint == 1) { workBtn.setChecked(false); notworkBtn.setChecked(true); longnotBtn.setChecked(false);      }
            else if(staint == 2) { workBtn.setChecked(false); notworkBtn.setChecked(false); longnotBtn.setChecked(true);      }
            else { workBtn.setChecked(true); notworkBtn.setChecked(false); longnotBtn.setChecked(false);      }

            //statusEdit.setText(sta);
            commentEdit.setText(comment);
        }

    }

    public class JSONTask2 extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... urls) {
            try {
                //staint = Integer.parseInt(statusEdit.getText().toString());
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("status", staint);
                jsonObject.accumulate("comment", commentEdit.getText().toString());

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

            //statusEdit.setText(""+staint);
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


}
