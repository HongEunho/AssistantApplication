package com.example.assistantapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.concurrent.ExecutionException;

public class SignupActivity extends AppCompatActivity {

    private Button valButton;
    private Button regButton;
    private Button depButton;
    private Button evalButton;
    private AppCompatEditText idEdit;
    private AppCompatEditText nameEdit;
    private AppCompatEditText passEdit;
    private AppCompatEditText passChkEdit;
    private AppCompatEditText phoneNumEdit;
    private AppCompatEditText emailEdit;
    private ProgressDialog pd;

    int dep;
    Boolean check = false;
    Boolean echeck = false;
    String id;
    String name;
    String password;
    String phoneNum;
    String email;
    String emailCode;
    String emailstr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("회원가입");

        valButton = findViewById(R.id.valButton);
        regButton = findViewById(R.id.regButton);
        depButton = findViewById(R.id.depButton);
        evalButton = findViewById(R.id.evalButton);

        idEdit = (AppCompatEditText)findViewById(R.id.userId2);
        nameEdit = findViewById(R.id.username2);
        passEdit = findViewById(R.id.password2);
        passChkEdit = findViewById(R.id.passCheck2);
        phoneNumEdit = findViewById(R.id.phoneNum2);
        emailEdit = findViewById(R.id.email2);

        final String ser = ((ServerVariable)getApplicationContext()).getSer();
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = emailEdit.getText().toString();

                if(!check)
                {
                    Toast.makeText(getApplicationContext(), "아이디를 확인해주세요", Toast.LENGTH_SHORT).show();
                }

                else if(!passEdit.getText().toString().equals(passChkEdit.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(),"비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                }
                else if(phoneNumEdit.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"휴대폰 번호를 입력하세요",Toast.LENGTH_SHORT).show();
                }
                else if(!echeck)
                {
                    Toast.makeText(getApplicationContext(), "이메일 인증을 진행해주세요", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    new JSONTask2().execute(ser+"/auth/signup");
                    Toast.makeText(getApplicationContext(),"가입이 완료 되었습니다",Toast.LENGTH_SHORT).show();
                    Intent signIntent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(signIntent);
                    finish();
                }
            }
        });

        dep = 1;
        depButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(SignupActivity.this);
                dlg.setTitle("학과를 선택하세요");
                final String[] versionArray = new String[] {"컴퓨터공학과", "소프트웨어학과", "정보보호학과", "데이터사이언스학과", "디자인이노베이션","만화애니메이션텍", "스마트기기공학전공", "무인이동체공학전공", "인공지능학과"};
                //아이콘 만들면 여기에 생성
                dlg.setSingleChoiceItems(versionArray, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        depButton.setText(versionArray[which]);
                        dep = which+1;
                    }
                });
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                System.out.println("학과확인"+dep);
                dlg.show();
            }
        });

        valButton.setOnClickListener(new View.OnClickListener() {
            String result;
            @Override
            public void onClick(View v) {
                try {
                    result = new JSONTask3().execute(ser+"/auth/idCheck").get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(result == null) // 중복되는 아이디 = null을 반환
                {
                    AlertDialog.Builder dia = new AlertDialog.Builder(SignupActivity.this);
                    dia.setTitle("중복 확인");
                    dia.setMessage("이미 존재하는 아이디 입니다");
                    valButton.setTextColor(Color.parseColor("#FF0000"));
                    dia.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dia.show();
                }
                else
                {
                    check = true;
                    AlertDialog.Builder dia = new AlertDialog.Builder(SignupActivity.this);
                    dia.setTitle("사용가능");
                    dia.setMessage("사용 가능한 아이디 입니다");
                    valButton.setTextColor(Color.parseColor("#47C83E"));

                    dia.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dia.show();
                }

            }
        });

        evalButton.setOnClickListener(new View.OnClickListener() {
            String result;
            String codeResult;
            @Override
            public void onClick(View view) {
                /*pd = new ProgressDialog(SignupActivity.this);
                pd.setIndeterminate(true);
                pd.setCancelable(false);
                pd.show();
                pd.setContentView(R.layout.custom_progress);*/
                emailstr = emailEdit.getText().toString();
                if (!emailstr.substring(emailstr.lastIndexOf("@")+1).equals("sejong.ac.kr")) {
                    if(emailstr.equals("")) Toast.makeText(getApplicationContext(),"이메일을 입력하세요",Toast.LENGTH_SHORT).show();
                    else Toast.makeText(getApplicationContext(),"세종대학교 이메일이 아닙니다", Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        result = new JSONTask4().execute(ser+"/mail/send").get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(result != null){
                        final Dialog builder = new Dialog(SignupActivity.this);
                        builder.setContentView(R.layout.emailcheck);
                        builder.setTitle("입력하신 이메일에서 인증번호를 확인 해주세요");
                        builder.show();

                        final Button yes_btn = (Button)builder.findViewById(R.id.yesbtn);
                        final Button no_btn = (Button)builder.findViewById(R.id.nobtn);
                        final EditText codeEdit = (EditText)builder.findViewById(R.id.codeEdit);

                        yes_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                emailCode = codeEdit.getText().toString();
                                try {
                                    codeResult = new JSONTask5().execute(ser+"/mail/verify").get();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                if (codeResult == null){
                                    Toast.makeText(getApplicationContext(), "인증번호가 올바르지 않습니다.",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    evalButton.setTextColor(Color.parseColor("#47C83E"));
                                    Toast.makeText(getApplicationContext(), "인증 되었습니다", Toast.LENGTH_SHORT).show();
                                    echeck = true;
                                }
                                builder.dismiss();
                            }
                        });
                        no_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                builder.dismiss();
                            }
                        });
                    }
                }
            }
        });

    }

    public class JSONTask2 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                id = idEdit.getText().toString();
                name = nameEdit.getText().toString();
                password = passEdit.getText().toString();
                phoneNum = phoneNumEdit.getText().toString();
                email = emailEdit.getText().toString();

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("userid", id);
                jsonObject.accumulate("username", name);
                jsonObject.accumulate("password", password);
                jsonObject.accumulate("phoneNumber", phoneNum);
                jsonObject.accumulate("email", email);
                jsonObject.accumulate("department", dep);

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
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
            System.out.println("확인용"+result);
            //offEdit.setText(""+officelink);
            //phoneEdit.setText(""+phonelink);
        }
    }

    public class JSONTask3 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                id = idEdit.getText().toString();

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("userid", id);

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
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
            System.out.println("체크"+result);

            //offEdit.setText(""+officelink);
            //phoneEdit.setText(""+phonelink);
        }
    }

    public class JSONTask4 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("to", emailstr);

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
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

        }

    }

    public class JSONTask5 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                id = idEdit.getText().toString();

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("email", emailstr);
                jsonObject.accumulate("authNumber", emailCode);

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
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
            System.out.println("체크"+result);

            //offEdit.setText(""+officelink);
            //phoneEdit.setText(""+phonelink);
        }
    }
}