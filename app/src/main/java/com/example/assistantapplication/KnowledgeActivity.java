package com.example.assistantapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

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
import java.util.ArrayList;

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
    private String questionE, answerE, category1E, category2E, category3E, category4E, landingUrlE, imageinfoE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge);
        a = KnowledgeActivity.this;
        final String ser = ((ServerVariable)getApplicationContext()).getSer();
        jo = new JSONObject[100];
        question = new String[100]; answer = new String[100]; category1 = new String[100]; category2 = new String[100]; category3 = new String[100];
        category4 = new String[100]; landingUrl = new String[100]; imageinfo = new String[100];

        final RecyclerView mRecyclerView = findViewById(R.id.knowledge_recyclerview);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mArrayList = new ArrayList<>();

        mAdapter = new KnowledgeAdapter(mArrayList);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        new JSONTask().execute(ser+"/knowledgePlus");

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
                final AppCompatEditText landingUrlEdit = view.findViewById(R.id.landingUrlEdit2);
                final AppCompatEditText imageInfoUrlEdit = view.findViewById(R.id.imageInfoEdit2);

                final AlertDialog dialog = builder.create();

                btn_modify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        questionE = questionEdit.getText().toString();
                        answerE = answerEdit.getText().toString();
                        category1E = category1Edit.getText().toString();
                        category2E = category2Edit.getText().toString();
                        landingUrlE = landingUrlEdit.getText().toString();
                        imageinfoE = imageInfoUrlEdit.getText().toString();
                        Knowledge data = new Knowledge(questionE,answerE,category1E,category2E,landingUrlE,imageinfoE);
                        mArrayList.add(data);
                        mAdapter.notifyDataSetChanged();
                        new JSONTask2().execute(ser+"/knowledgePlus");
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

            try {
                JSONArray ja = new JSONArray(result);
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
                Knowledge data = new Knowledge(question[i],answer[i],category1[i],category2[i],landingUrl[i],imageinfo[i]);
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
                jsonObject0.accumulate("landingUrl", landingUrlE);
                jsonObject0.accumulate("imageinfo", imageinfoE);

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
}