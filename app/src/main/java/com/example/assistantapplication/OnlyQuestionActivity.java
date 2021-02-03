package com.example.assistantapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class OnlyQuestionActivity extends AppCompatActivity {

    private ArrayList<OnlyQuestion> mArrayList;
    private OnlyQuestionAdapter mAdapter;
    Activity a;
    private int size = 0;
    private JSONObject[] jo;
    private String[] department;
    private String[] content;
    private String[] time;
    private int[] idx;
    private int idxno;
    private String departmentE, contentE;
    public long now;
    public Date mDate;
    public SimpleDateFormat simpleDate;
    public String formatDate;
    String curName;
    String dep;
    String myToken;
    String tmp_dep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_question);

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

        a = OnlyQuestionActivity.this;
        final String ser = ((ServerVariable)getApplicationContext()).getSer();

        //현재는 여유롭게 게시글 수 10000개를 최대로 제한해 놓았습니다.
        //더 많은 데이터가 필요하다면, 페이지네이션 기법을 통해 불러오는 개수를 조정하시면 됩니다.
        //리사이클러뷰로 제작했으니, 밑의 서버 코드에서 페이지 네이션을 통해 ser+/question/+dep+?page=0&size=5 이런 형식으로 구성하시면 됩니다.
        //자세한 부분은 sjswbot.site/docs에 설명되어 있습니다.
        jo = new JSONObject[10000];
        department = new String[10000]; content = new String[10000]; time = new String[10000];
        idx = new int[10000];

        new JSONTask().execute(ser+"/question/"+dep);

        final RecyclerView mRecyclerView = findViewById(R.id.onlyquestion_recyclerview);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mArrayList = new ArrayList<>();

        mAdapter = new OnlyQuestionAdapter(mArrayList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnDeleteClickListener(new OnlyQuestionAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(View v, int position) {
                OnlyQuestion item = mAdapter.getItem(position);
                final int pos = position;
                idxno = item.getIdx();

                AlertDialog.Builder builder = new AlertDialog.Builder(OnlyQuestionActivity.this);
                builder.setMessage("삭제 하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mArrayList.remove(pos);
                        mAdapter.notifyDataSetChanged();
                        new JSONTask2().execute(ser+"/question/"+Integer.toString(idxno));
                        ((ServerVariable)getApplicationContext()).deleteSuccess(a);

                    }
                });

                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
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
                con.setRequestProperty("Authorization", myToken);
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
                    idx[i] = jo[i].getInt("idx");
                    tmp_dep = jo[i].getString("department");
                    if(tmp_dep.equals("11")){
                        department[i] = "공통 질문";
                    }
                    else{
                        department[i] = "우리학과 질문";
                    }
                    content[i] = jo[i].getString("content");
                    time[i] = jo[i].getString("updatedAt");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //recycler view에 탑재
            for(int i=0; i<size; i++)
            {
                OnlyQuestion data = new OnlyQuestion(idx[i],department[i],content[i],time[i]);
                mArrayList.add(data);

            }
            Collections.sort(mArrayList);
            mAdapter.notifyDataSetChanged();
        }
    }

    public class JSONTask2 extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject jsonObject0 = new JSONObject();
                jsonObject0.accumulate("idx", idxno);


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
}