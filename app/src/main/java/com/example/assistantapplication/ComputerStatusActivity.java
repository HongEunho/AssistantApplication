package com.example.assistantapplication;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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
import java.text.SimpleDateFormat;
import java.util.Date;

public class ComputerStatusActivity extends AppCompatActivity {

    private TextView comment;
    private EditText commentEdit;
    private EditText majorEdit;
    private EditText positionEdit;
    private EditText phoneNumEdit;
    private RadioButton workBtn;
    private RadioButton notworkBtn;
    private RadioButton longnotBtn;
    private RadioGroup workingGroup;
    private Button button;
    int staint;

    public long now;
    public Date mDate;
    public SimpleDateFormat simpleDate;
    public String formatDate;
    String curName;
    String myToken;
    String myDep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_computer_status);
        final Activity a = ComputerStatusActivity.this;

        //setTitle("컴퓨터공학과 조교관리 시스템");
        // 원래는 컴퓨터공학과 전용 페이지였지만 변수설정으로 모든 학과의 설정 페이지로 변경하였음.

        //현재 시간
        now = System.currentTimeMillis() + 32400000;
        mDate = new Date(now);
        simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        formatDate = simpleDate.format(mDate);
        //현재 로그인 정보
        SharedPreferences preferences = getSharedPreferences("login",MODE_PRIVATE);
        String curID = preferences.getString("ID","0");
        curName = preferences.getString("Name","0");
        myDep = preferences.getString("Department", "9999");
        myToken = preferences.getString("Token",null);
        System.out.println("현재 내 토큰"+myToken);

        comment = findViewById(R.id.comment);
        commentEdit = findViewById(R.id.commentEdit);
        majorEdit = findViewById(R.id.majorEdit);
        positionEdit = findViewById(R.id.positionEdit);
        phoneNumEdit = findViewById(R.id.officeNumEdit);
        commentEdit = findViewById(R.id.commentEdit);
        workBtn = findViewById(R.id.workBtn);
        notworkBtn = findViewById(R.id.notworkBtn);
        longnotBtn = findViewById(R.id.longnotBtn);
        workingGroup = findViewById(R.id.workingGroup);
        button = findViewById(R.id.button);

        //기본적으로 comment는 숨기기
        comment.setVisibility(View.GONE);
        commentEdit.setVisibility(View.GONE);

        final String ser = ((ServerVariable)getApplicationContext()).getSer();

        new JSONTask().execute(ser+"/status/"+myDep);

        RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.workBtn)
                {   staint = 0; comment.setVisibility(View.GONE); commentEdit.setVisibility(View.GONE);}
                else if(checkedId == R.id.notworkBtn)
                {   staint = 1; comment.setVisibility(View.GONE); commentEdit.setVisibility(View.GONE);}
                else if(checkedId == R.id.longnotBtn)
                {   staint = 2; comment.setVisibility(View.VISIBLE);  commentEdit.setVisibility(View.VISIBLE); }
                else
                {   staint = 0; comment.setVisibility(View.GONE); commentEdit.setVisibility(View.GONE);}
            }
        };
        workingGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JSONTask2().execute(ser+"/status/"+myDep);
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
            System.out.println("학과"+result);
            String position="";
            String dep="";
            String sta="";
            String comment = "";
            String phoneNum = "";
            try {
                dep += jo.getString("department");
                sta += jo.getString("status");
                position += jo.getString("position");
                phoneNum += jo.getString("phoneNumber");
                comment += jo.getString("comment");
                if(comment.equals("null"))
                    comment="없습니다";
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //RadioButton으로 status나타내기
            staint = Integer.parseInt(sta);
            if(staint == 0) { workBtn.setChecked(true); notworkBtn.setChecked(false); longnotBtn.setChecked(false);      }
            else if(staint == 1) { workBtn.setChecked(false); notworkBtn.setChecked(true); longnotBtn.setChecked(false);      }
            else if(staint == 2) { workBtn.setChecked(false); notworkBtn.setChecked(false); longnotBtn.setChecked(true);      }
            else { workBtn.setChecked(true); notworkBtn.setChecked(false); longnotBtn.setChecked(false);      }

            majorEdit.setText(dep);
            commentEdit.setText(comment);
            positionEdit.setText(position);
            phoneNumEdit.setText(phoneNum);

        }

    }

    public class JSONTask2 extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("status", staint);
                jsonObject.accumulate("comment", commentEdit.getText().toString());
                jsonObject.accumulate("phoneNumber",phoneNumEdit.getText().toString());
                jsonObject.accumulate("position",positionEdit.getText().toString());
                jsonObject.accumulate("time",formatDate);
                jsonObject.accumulate("modifier",curName);

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
