package com.example.assistantapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.text.method.Touch;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
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
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class KnowledgeActivity extends AppCompatActivity {

    private ArrayList<Knowledge> mArrayList;
    private KnowledgeAdapter mAdapter;
    Activity a;
    private int count = -1;
    private int size = 0;
    private JSONObject[] jo;
    private String[] question;
    private String[] answer;
    private String[] category1;
    private String[] category2;
    private String[] category3;
    private String[] category4;
    private String[] landingUrl;
    private String[] imageinfo;
    private int[] faqno;
    private int faqno2;
    private String questionE, answerE, category1E, category2E, category3E, category4E, landingUrlE, imageinfoE;
    public long now;
    public Date mDate;
    public SimpleDateFormat simpleDate;
    public String formatDate;
    String curName;
    String dep;
    String depKo;
    String myToken;
    String tmp_dep;

    // 질문 추가 및 수정 페이지 입니다.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge);

        //현재 시간
        now = System.currentTimeMillis() + 32400000;
        mDate = new Date(now);
        simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        formatDate = simpleDate.format(mDate);
        System.out.println("현재시간:"+formatDate);
        //현재 로그인 정보
        SharedPreferences preferences = getSharedPreferences("login",MODE_PRIVATE);
        String curID = preferences.getString("ID","0");
        curName = preferences.getString("Name","0");
        dep = preferences.getString("Department", "9999");
        myToken = preferences.getString("Token", null);

        Intent intent = getIntent();
        depKo = intent.getStringExtra("Department");

        a = KnowledgeActivity.this;
        final String ser = ((ServerVariable)getApplicationContext()).getSer();
        // 마찬가지로 여유롭게 10000개로 잡아두어 리사이클러뷰를 사용하였으나
        // 재구성할 필요가 있으면 페이지네이션을 사용하거나 리사이클러뷰 대신 페이징 방법으로 재구성 하시면 될 것 같습니다. (OnlyQuestionActivity)참고.
        jo = new JSONObject[10000];
        question = new String[10000]; answer = new String[10000]; category1 = new String[10000]; category2 = new String[10000]; category3 = new String[10000];
        category4 = new String[10000]; landingUrl = new String[10000]; imageinfo = new String[10000];
        faqno = new int[10000];

        new JSONTask().execute(ser+"/knowledgePlus/list/"+depKo);

        final RecyclerView mRecyclerView = findViewById(R.id.knowledge_recyclerview);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mArrayList = new ArrayList<>();

        mAdapter = new KnowledgeAdapter(mArrayList);

        mRecyclerView.setAdapter(mAdapter);
        //아이템 클릭 처리
        mAdapter.setOnItemClickListener(new KnowledgeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, final int position) {
                Knowledge item = mAdapter.getItem(position);
                final int pos = position;
                faqno2 = item.getFaqno();
                System.out.println("번호:"+faqno2);
                AlertDialog.Builder builder = new AlertDialog.Builder(KnowledgeActivity.this);
                View view = LayoutInflater.from(KnowledgeActivity.this).inflate(R.layout.modifydelete_knowledge, null, false);

                final Button btn_modify = view.findViewById(R.id.modify_btn);
                final Button btn_delete = view.findViewById(R.id.delete_btn);
                final AppCompatEditText questionEdit = view.findViewById(R.id.questionEdit2);
                final AppCompatEditText answerEdit = view.findViewById(R.id.answerEdit2);
                final AppCompatEditText category1Edit = view.findViewById(R.id.category1Edit2);
                final AppCompatEditText category2Edit = view.findViewById(R.id.category2Edit2);
                final AppCompatEditText category3Edit = view.findViewById(R.id.category3Edit2);
                final AppCompatEditText landingUrlEdit = view.findViewById(R.id.landingUrlEdit2);
                final AppCompatEditText imageInfoUrlEdit = view.findViewById(R.id.imageInfoEdit2);

                questionEdit.setText(item.getQuestion());
                answerEdit.setText(item.getAnswer());
                category1Edit.setText(item.getCategory1());
                category2Edit.setText(item.getCategory2());
                category3Edit.setText(item.getCategory3());
                landingUrlEdit.setText(item.getLandingUrl());
                imageInfoUrlEdit.setText(item.getImageInfo());


                builder.setView(view);

                final AlertDialog dialog = builder.create();

                btn_modify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        questionE = questionEdit.getText().toString();
                        answerE = answerEdit.getText().toString();
                        category1E = category1Edit.getText().toString();
                        category2E = category2Edit.getText().toString();
                        category3E = category3Edit.getText().toString();
                        landingUrlE = landingUrlEdit.getText().toString();
                        imageinfoE = imageInfoUrlEdit.getText().toString();

                        Knowledge data = new Knowledge(questionE,answerE,category1E,category2E,category3E,landingUrlE,imageinfoE);
                        mArrayList.set(pos,data);
                        mAdapter.notifyDataSetChanged();
                        new JSONTask3().execute(ser+"/knowledgePlus/"+Integer.toString(faqno2));
                        ((ServerVariable)getApplicationContext()).Cookie(a);
                        dialog.dismiss();
                    }
                });
                btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mArrayList.remove(pos);
                        mAdapter.notifyDataSetChanged();
                        new JSONTask4().execute(ser+"/knowledgePlus/"+Integer.toString(faqno2));
                        ((ServerVariable)getApplicationContext()).deleteSuccess(a);

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        //삽입버튼
        Button insertButton = findViewById(R.id.btn_insert);
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(KnowledgeActivity.this);
                View view = LayoutInflater.from(KnowledgeActivity.this).inflate(R.layout.edit_knowledge, null, false);
                builder.setView(view);

                final Button btn_modify = view.findViewById(R.id.modify_btn);
                final AppCompatEditText questionEdit = view.findViewById(R.id.questionEdit2);
                final AppCompatEditText answerEdit = view.findViewById(R.id.answerEdit2);
                final AppCompatEditText category1Edit = view.findViewById(R.id.category1Edit2);
                final AppCompatEditText category2Edit = view.findViewById(R.id.category2Edit2);
                final AppCompatEditText category3Edit = view.findViewById(R.id.category3Edit2);
                final AppCompatEditText landingUrlEdit = view.findViewById(R.id.landingUrlEdit2);
                final AppCompatEditText imageInfoUrlEdit = view.findViewById(R.id.imageInfoEdit2);

                final AlertDialog dialog = builder.create();
                //삽입확인
                btn_modify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        questionE = questionEdit.getText().toString();
                        answerE = answerEdit.getText().toString();
                        category1E = category1Edit.getText().toString();
                        category2E = category2Edit.getText().toString();
                        category3E = category3Edit.getText().toString();
                        landingUrlE = landingUrlEdit.getText().toString();
                        imageinfoE = imageInfoUrlEdit.getText().toString();
                        //없애고 다시 불러오기 ( 새 질문 삽입 후 초기화 과정 )
                        mArrayList.clear();
                        new JSONTask2().execute(ser+"/knowledgePlus");
                        new JSONTask().execute(ser+"/knowledgePlus");
                        ((ServerVariable)getApplicationContext()).insertSuccess(a);
                        dialog.dismiss();
                    }
                });
                dialog.show();
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
            JSONArray ja = jsonParsing(result);
            try {
                size = ja.length();
                for(int i=0; i<ja.length(); i++)
                {
                    jo[i] = ja.getJSONObject(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                for(int i=0; i<size;i++)
                {
                    faqno[i] = jo[i].getInt("faqno");
                    question[i] = jo[i].getString("question");
                    answer[i] = jo[i].getString("questionAnswer");
                    category1[i] = jo[i].getString("category1");
                    category2[i] = jo[i].getString("category2");
                    category3[i] = jo[i].getString("category3");
                    category4[i] = jo[i].getString("category4");
                    landingUrl[i] = jo[i].getString("landingUrl");
                    imageinfo[i] = jo[i].getString("imageinfo");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //recycler view에 탑재
            for(int i=0; i<size; i++)
            {
                Knowledge data = new Knowledge(faqno[i],question[i],answer[i],category1[i],category2[i], category3[i],landingUrl[i],imageinfo[i]);
                mArrayList.add(data);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    public class JSONTask2 extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... urls) {
            try {

                JSONObject jsonObject0 = new JSONObject();
                jsonObject0.accumulate("question", questionE);
                jsonObject0.accumulate("questionAnswer", answerE);
                jsonObject0.accumulate("category1", category1E);
                jsonObject0.accumulate("category2", category2E);
                jsonObject0.accumulate("category3", category3E);
                jsonObject0.accumulate("landingUrl", landingUrlE);
                jsonObject0.accumulate("imageinfo", imageinfoE);
                jsonObject0.accumulate("time",formatDate);
                jsonObject0.accumulate("modifier",curName);

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

        }
    }

    public class JSONTask3 extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... urls) {
            try {

                JSONObject jsonObject0 = new JSONObject();
                jsonObject0.accumulate("question", questionE);
                jsonObject0.accumulate("questionAnswer", answerE);
                jsonObject0.accumulate("category1", category1E);
                jsonObject0.accumulate("category2", category2E);
                jsonObject0.accumulate("category3", category3E);
                jsonObject0.accumulate("landingUrl", landingUrlE);
                jsonObject0.accumulate("imageinfo", imageinfoE);
                jsonObject0.accumulate("time",formatDate);
                jsonObject0.accumulate("modifier",curName);

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

        }
    }

    public class JSONTask4 extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject jsonObject0 = new JSONObject();
                jsonObject0.accumulate("faqno", faqno2);


                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("DELETE");
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

        }
    }

    public JSONArray jsonParsing(String json)
    {
        JSONObject jo = null;
        JSONObject resultjo = null;
        JSONArray rowja = null;
        try {
            jo = new JSONObject(json); //전체 반환문 {}
            resultjo = jo.getJSONObject("result");  //"result" jsonObject
            rowja = resultjo.getJSONArray("rows");
            System.out.println("확인용"+rowja);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rowja;
    }

    public interface OnItemClickListener{
        void OnItemClick(View v, int pos);
    }
}