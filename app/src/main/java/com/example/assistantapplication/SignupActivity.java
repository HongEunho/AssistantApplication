package com.example.assistantapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
    private EditText idEdit;
    private EditText nameEdit;
    private EditText passEdit;
    private EditText passChkEdit;
    private EditText phoneNumEdit;
    private EditText emailEdit;

    int dep;
    Boolean check = false;

    String id;
    String name;
    String password;
    String phoneNum;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("회원가입");

        valButton = findViewById(R.id.valButton);
        regButton = findViewById(R.id.regButton);
        depButton = findViewById(R.id.depButton);

        idEdit = findViewById(R.id.userId);
        nameEdit = findViewById(R.id.username);
        passEdit = findViewById(R.id.password);
        passChkEdit = findViewById(R.id.passCheck);
        phoneNumEdit = findViewById(R.id.phoneNum);
        emailEdit = findViewById(R.id.email);

        final String ser = ((ServerVariable)getApplicationContext()).getSer();
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = emailEdit.getText().toString();

                if(check == false)
                {
                    Toast.makeText(getApplicationContext(), "이미 존재하는 아이디 입니다", Toast.LENGTH_SHORT).show();
                }

                else if(!passEdit.getText().toString().equals(passChkEdit.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(),"비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                }
                else if(!str.substring(str.lastIndexOf("@")+1).equals("sejong.ac.kr"))
                {
                    Toast.makeText(getApplicationContext(),"세종대학교 이메일이 아닙니다", Toast.LENGTH_SHORT).show();
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

        depButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(SignupActivity.this);
                dlg.setTitle("학과를 선택하세요");
                final String[] versionArray = new String[] {"컴퓨터공학과", "소프트웨어학과", "정보보호학과", "데이터사이언스학과", "지능기전공학부", "창의소프트학부"};
                //아이콘 만들면 여기에 생성
                dlg.setSingleChoiceItems(versionArray, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        depButton.setText(versionArray[which]);
                        dep = which;
                    }
                });
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
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
                if(result == null)
                {
                    AlertDialog.Builder dia = new AlertDialog.Builder(SignupActivity.this);
                    dia.setTitle("중복 확인");
                    dia.setMessage("이미 존재하는 아이디 입니다");
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
                    dia.setTitle("중복 확인");
                    dia.setMessage("사용 가능한 아이디 입니다");
                    dia.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dia.show();
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
}