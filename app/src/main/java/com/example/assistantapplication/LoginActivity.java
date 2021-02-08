package com.example.assistantapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
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

public class LoginActivity extends AppCompatActivity {

    private ButtonRectangle btn_login;
    private ButtonRectangle btn_register;

    private long backKeyPressedTime = 0;
    private Toast toast;

    ProgressBar loading;

    TextInputLayout idE;

    private AppCompatEditText idEdit;
    private AppCompatEditText passEdit;
    Activity a;
    String id;
    String password;
    String dep;
    String pushdep;
    String name;
    String mytoken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Sejong HelpBot");
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        loading = findViewById(R.id.indeterminateBar);
        loading.setVisibility(View.INVISIBLE);
        a = LoginActivity.this;

        idEdit = (AppCompatEditText)findViewById(R.id.idEdit2);
        passEdit = (AppCompatEditText)findViewById(R.id.passEdit2);

        SharedPreferences auto = getSharedPreferences("login",MODE_PRIVATE);
        String autoID = auto.getString("ID",null);
        String autoPw = auto.getString("Password", null);
        String autodep = auto.getString("Department", "9999");
        String autotok = auto.getString("Token", null);
        System.out.println("autodep 확인" + autodep);
        if(autoID != null && autoPw != null)
        {
            System.out.println("진입");
            if (autodep.equals("1")) {      //컴퓨터공학과 (Computer)
                Intent intent = new Intent(LoginActivity.this, ComputerActivity.class);
                startActivity(intent);
                finish();
            } else if (autodep.equals("2")) {   //소프트웨어학과 (Software)
                Intent intent = new Intent(LoginActivity.this, SoftwareActivity.class);
                startActivity(intent);
                finish();
            } else if (autodep.equals("3")) {   //정보보호학과(Security)
                Intent intent = new Intent(LoginActivity.this, SecurityActivity.class);
                startActivity(intent);
                finish();
            } else if (autodep.equals("4")) {   //데이터사이언스학과(DataScience)
                Intent intent = new Intent(LoginActivity.this, DataScienceActivity.class);
                startActivity(intent);
                finish();
            } else if (autodep.equals("5")) {   //디자인이노베이션(Design)
                Intent intent = new Intent(LoginActivity.this, DesignActivity.class);
                startActivity(intent);
                finish();
            } else if (autodep.equals("6")) {   //만화애니메이션텍(Cartoon)
                Intent intent = new Intent(LoginActivity.this, CartoonActivity.class);
                startActivity(intent);
                finish();
            }else if (autodep.equals("7")) {    //스마트기기공학전공(Smart)
                Intent intent = new Intent(LoginActivity.this, SmartActivity.class);
                startActivity(intent);
                finish();
            }
            else if (autodep.equals("8")){      //무인이동체공학전공(Unmanned)
                Intent intent = new Intent(LoginActivity.this, UnmannedActivity.class);
                startActivity(intent);
                finish();
            }
            else if (autodep.equals("9")){      //인공지능학과(AI)
                Intent intent = new Intent(LoginActivity.this, AiActivity.class);
                startActivity(intent);
                finish();
            }
            else if(autodep.equals("10")){      //관리자페이지(Manager)
                Intent intent = new Intent(LoginActivity.this, ManagerActivity.class);
                startActivity(intent);
                finish();
            }
        }


        final String ser = ((ServerVariable)getApplicationContext()).getSer();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                new JSONTask2().execute(ser+"/auth/login");
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent2);
                //finish();
            }
        });

    }

    @Override
    public void onBackPressed(){
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            finish();
            toast.cancel();

        }
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
            if(result == null)
            {
                ((ServerVariable)getApplicationContext()).LoginFail(a);
                loading.setVisibility(View.INVISIBLE);
            }
            else {
                try {
                    JSONObject jo = jsonParsing2(result);
                } catch (JSONException e) {
                    System.out.println("콱" + e);
                    e.printStackTrace();
                }

                //System.out.println("확"+jo.toString());
                //JSONObject jo2 = jo.optJSONObject("user");
                //System.out.println("확인"+jo2.toString());
                if (result != null) {
                    if (dep.equals("1")) {          //컴퓨터공학과 (Computer)
                        Intent intent = new Intent(LoginActivity.this, ComputerActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (dep.equals("2")) {   //소프트웨어학과 (Software)
                        Intent intent = new Intent(LoginActivity.this, SoftwareActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (dep.equals("3")) {   //정보보호학과(Security)
                        Intent intent = new Intent(LoginActivity.this, SecurityActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (dep.equals("4")) {   //데이터사이언스학과(DataScience)
                        Intent intent = new Intent(LoginActivity.this, DataScienceActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (dep.equals("5")) {   //디자인이노베이션(Design)
                        Intent intent = new Intent(LoginActivity.this, DesignActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (dep.equals("6")) {   //만화애니메이션텍(Cartoon)
                        Intent intent = new Intent(LoginActivity.this, CartoonActivity.class);
                        startActivity(intent);
                        finish();
                    }else if (dep.equals("7")) {    //스마트기기공학전공(Smart)
                        Intent intent = new Intent(LoginActivity.this, SmartActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if (dep.equals("8")){      //무인이동체공학전공(Unmanned)
                        Intent intent = new Intent(LoginActivity.this, UnmannedActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if (dep.equals("9")){      //인공지능학과(Ai)
                        Intent intent = new Intent(LoginActivity.this, AiActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if (dep.equals("10")){     //관리자페이지(Manager)
                        Intent intent = new Intent(LoginActivity.this, ManagerActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "아이디 비밀번호를 다시 확인하세요", Toast.LENGTH_SHORT).show();
                }
            }
            //offEdit.setText(""+officelink);
            //phoneEdit.setText(""+phonelink);
        }
    }


    public JSONObject jsonParsing2(String json) throws JSONException {
        JSONObject jo = new JSONObject(json);
        JSONArray ja = jo.getJSONArray("result");
        System.out.println("제이슨"+ja);
        JSONObject resultjo = ja.getJSONObject(0);
        dep = resultjo.get("department").toString();
        name = resultjo.get("user").toString();
        mytoken = resultjo.get("token").toString();

        System.out.println("제이슨 이름"+dep);
        //JSONObject userjo = (JSONObject) jo.get("result");
        //dep = userjo.get("department").toString();
        //name = userjo.get("user").toString();
        //현재 id와 password는 edittext 그대로 가져오고 dep는 서버결과값에서 가져옴
        System.out.println("칼"+dep);
        SharedPreferences preferences = getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ID",id);
        editor.putString("Password",password);
        editor.putString("Department",dep);
        editor.putString("Name",name);
        editor.putString("Token",mytoken);

        switch(dep){
            case "1":
                editor.putString("depKo","컴퓨터공학과");
                editor.putString("pushdep","computer");
                break;
            case "2":
                editor.putString("depKo","소프트웨어학과");
                editor.putString("pushdep","software");
                break;
            case "3":
                editor.putString("depKo","정보보호학과");
                editor.putString("pushdep","information");
                break;
            case "4":
                editor.putString("depKo","데이터사이언스학과");
                editor.putString("pushdep","datascience");
                break;
            case "5":
                editor.putString("depKo","디자인이노베이션");
                editor.putString("pushdep","design");
                break;
            case "6":
                editor.putString("depKo","만화애니메이션텍");
                editor.putString("pushdep","cartoon");
                break;
            case "7":
                editor.putString("depKo","스마트기기공학전공");
                editor.putString("pushdep","smart");
                break;
            case "8":
                editor.putString("depKo","무인이동체공학전공");
                editor.putString("pushdep","imc");
                break;
            case "9":
                editor.putString("depKo","인공지능학과");
                editor.putString("pushdep","ai");
                break;
            case "10":
                editor.putString("depKo","관리자");
                editor.putString("pushdep","computer");
            default:
                break;
        }
        editor.commit();
        return resultjo;
    }

}