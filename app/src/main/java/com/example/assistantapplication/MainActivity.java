package com.example.assistantapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // 해커톤 초기에 만들어놓은 페이지로 현재는 사용하지 않습니다.
    // 지우셔도 상관 없습니다.
    public static String Server = "https://mfam.site";

    private Button computerbtn;
    private Button softwarebtn;
    private Button securitybtn;
    private Button databtn;
    private Button intellbtn;
    private Button ideabtn;
    private TextView selTV;


    public String getDate(){
        return Server;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setTitle("소융봇 조교관리 시스템");
        selTV = findViewById(R.id.selTV);
        computerbtn = findViewById(R.id.computerbtn);
        softwarebtn = findViewById(R.id.softwarebtn);
        securitybtn = findViewById(R.id.securitybtn);
        databtn = findViewById(R.id.databtn);
        intellbtn = findViewById(R.id.intellbtn);
        ideabtn = findViewById(R.id.ideabtn);

        computerbtn.setOnClickListener(myClickListener);
        softwarebtn.setOnClickListener(myClickListener);
        databtn.setOnClickListener(myClickListener);
        securitybtn.setOnClickListener(myClickListener);
        intellbtn.setOnClickListener(myClickListener);
        ideabtn.setOnClickListener(myClickListener);

    }
    View.OnClickListener myClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            switch(v.getId())
            {
                case R.id.computerbtn:
                    Intent comintent = new Intent(MainActivity.this,ComputerActivity.class);
                    startActivity(comintent);
                    break;
                case R.id.softwarebtn:
                    Intent sofintent = new Intent(MainActivity.this,SoftwareActivity.class);
                    startActivity(sofintent);
                    break;
                case R.id.databtn:
                    Intent dataintent = new Intent(MainActivity.this,DataScienceActivity.class);
                    startActivity(dataintent);
                    break;
                case R.id.securitybtn:
                    Intent secuintent = new Intent(MainActivity.this,SecurityActivity.class);
                    startActivity(secuintent);
                    break;
                case R.id.intellbtn:
                    Intent intellintent = new Intent(MainActivity.this, UnmannedActivity.class);
                    startActivity(intellintent);
                    break;
                case R.id.ideabtn:
                    Intent ideaintent = new Intent(MainActivity.this, DesignActivity.class);
                    startActivity(ideaintent);
                    break;
            }
        }
    };

}

