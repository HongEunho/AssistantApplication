package com.example.assistantapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class ComputerActivity extends AppCompatActivity {
    //컴퓨터공학과 액티비티입니다.
    private Button staBtn;
    private Button onlyQaBtn;
    private Button modifyBtn;
    private Button officeBtn;
    private Button noticeBtn;
    private Button FAQBtn;
    private Button knowledgeBtn;
    private Button setBtn;
    private long backKeyPressedTime = 0;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_computer);

        ((ServerVariable)getApplicationContext()).unsubscribePush("computer");

        SharedPreferences preferences = getSharedPreferences("login",MODE_PRIVATE);
        String curID = preferences.getString("ID","0");
        String curName = preferences.getString("Name","0");


        System.out.println("확인함"+curID);
        staBtn = findViewById(R.id.staBtn);
        setBtn = findViewById(R.id.set_btn);
        officeBtn = findViewById(R.id.officeBtn);
        noticeBtn = findViewById(R.id.noticeBtn);
        FAQBtn = findViewById(R.id.FAQBtn);
        knowledgeBtn = findViewById(R.id.knowledgeBtn);
        onlyQaBtn = findViewById(R.id.onlyQaBtn);
        modifyBtn = findViewById(R.id.datamodifyBtn);

        staBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ComputerActivity.this, StatusActivity.class);
                intent.putExtra("department","컴퓨터공학과");
                startActivity(intent);
            }
        });

        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(ComputerActivity.this,DatamodifyActivity.class);
                startActivity(intent3);
            }
        });
        officeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(ComputerActivity.this, ProfessorActivity.class);
                startActivity(intent4);
            }
        });
        noticeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent6 = new Intent(ComputerActivity.this, NoticeActivity.class);
                intent6.putExtra("department","컴퓨터공학과");
                startActivity(intent6);
            }
        });
        FAQBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5 = new Intent(ComputerActivity.this, FAQActivity.class);
                startActivity(intent5);
            }
        });
        knowledgeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent7 = new Intent(ComputerActivity.this, KnowledgeActivity.class);
                intent7.putExtra("department", "컴퓨터공학과");
                startActivity(intent7);
            }
        });
        onlyQaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ComputerActivity.this,OnlyQuestionActivity.class);
                startActivity(intent2);
            }
        });
        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ComputerActivity.this, SettingActivity.class);
                startActivity(intent);
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



}
