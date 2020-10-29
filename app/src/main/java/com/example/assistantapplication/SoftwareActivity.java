package com.example.assistantapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SoftwareActivity extends AppCompatActivity {

    private Button staBtn;
    private Button curriBtn;
    private Button roomBtn;
    private Button officeBtn;
    private Button FAQBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_software);
        setTitle("소프트웨어학과 조교관리 시스템");

        final Activity a = SoftwareActivity.this;

        staBtn = findViewById(R.id.staBtn);
        curriBtn = findViewById(R.id.curriBtn);
        roomBtn = findViewById(R.id.roomBtn);
        officeBtn = findViewById(R.id.officeBtn);
        FAQBtn = findViewById(R.id.FAQBtn);

        staBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SoftwareActivity.this,SoftwareStatusActivity.class);
                startActivity(intent);
            }
        });
        curriBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(SoftwareActivity.this,SoftwareCurriActivity.class);
                startActivity(intent2);
            }
        });
        roomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(SoftwareActivity.this,ComputerTimeActivity.class);
                startActivity(intent3);
            }
        });
        officeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(SoftwareActivity.this, ProfessorActivity.class);
                startActivity(intent4);
            }
        });
        FAQBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5 = new Intent(SoftwareActivity.this, FAQActivity.class);
                startActivity(intent5);
            }
        });
    }
}
