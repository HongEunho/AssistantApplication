package com.example.assistantapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    private Button btn_register;
    private EditText idEdit;
    private EditText passEdit;


    String id;
    String password;

    String dep;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);

        idEdit = findViewById(R.id.idEdit);
        passEdit = findViewById(R.id.passEdit);

        final String ser = ((ServerVariable)getApplicationContext()).getSer();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                

                new JSONTask2().execute(ser+"/auth/login");
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent2);
                finish();
            }
        });

    }
    public class JSONTask2 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                id = idEdit.getText().toString();
                password = passEdit.getText().toString();

                Log.v("TAG","백그라운드 작업   user_id=" + id + " user_pwd=" + password);

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("userid", id);
                jsonObject.accumulate("password", password);


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
                    Log.v("TAG", buffer.toString());
                    //System.out.println("버퍼"+buffer.toString());
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
            System.out.println("확"+result);
            try {
                JSONObject jo = jsonParsing2(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //System.out.println("확"+jo.toString());
            //JSONObject jo2 = jo.optJSONObject("user");
            //System.out.println("확인"+jo2.toString());
            if(result != null)
            {
                if(dep.equals("0")){
                    Intent intent = new Intent(LoginActivity.this, ComputerActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(dep.equals("1")){
                    Intent intent = new Intent(LoginActivity.this, SoftwareActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(dep.equals("2")){
                    Intent intent = new Intent(LoginActivity.this, SecurityActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(dep.equals("3")){
                    Intent intent = new Intent(LoginActivity.this, DataScienceActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(dep.equals("4")){
                    Intent intent = new Intent(LoginActivity.this, IntellActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(dep.equals("5")){
                    Intent intent = new Intent(LoginActivity.this, IdeaActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "아이디 비밀번호를 다시 확인하세요", Toast.LENGTH_SHORT).show();
            }
            //offEdit.setText(""+officelink);
            //phoneEdit.setText(""+phonelink);
        }
    }


    public JSONObject jsonParsing2(String json) throws JSONException {
        JSONObject jo = new JSONObject(json);
        JSONObject userjo = (JSONObject) jo.get("user");
        dep = userjo.get("department").toString();
        System.out.println("칼"+dep);
        return jo;
    }

}