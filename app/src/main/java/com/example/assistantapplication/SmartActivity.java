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

public class SmartActivity extends AppCompatActivity {
    //스마트기기공학전공 액티비티입니다.
    private Button staBtn;
    private Button onlyQaBtn;
    private Button modifyBtn;
    private Button officeBtn;
    private Button noticeBtn;
    private Button FAQBtn;
    private Button knowledgeBtn;
    private long backKeyPressedTime = 0;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_computer);

        ((ServerVariable)getApplicationContext()).unsubscribePush("smart");

        SharedPreferences preferences = getSharedPreferences("login",MODE_PRIVATE);
        String curID = preferences.getString("ID","0");
        String curName = preferences.getString("Name","0");

        FirebaseMessaging.getInstance().subscribeToTopic("smart")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }

                    }
                });

        System.out.println("확인함"+curID);
        staBtn = findViewById(R.id.staBtn);
        modifyBtn = findViewById(R.id.datamodifyBtn);
        officeBtn = findViewById(R.id.officeBtn);
        noticeBtn = findViewById(R.id.noticeBtn);
        FAQBtn = findViewById(R.id.FAQBtn);
        knowledgeBtn = findViewById(R.id.knowledgeBtn);
        onlyQaBtn = findViewById(R.id.onlyQaBtn);

        staBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SmartActivity.this, StatusActivity.class);
                startActivity(intent);
            }
        });

        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(SmartActivity.this,DatamodifyActivity.class);
                startActivity(intent3);
            }
        });
        officeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(SmartActivity.this, ProfessorActivity.class);
                startActivity(intent4);
            }
        });
        noticeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent6 = new Intent(SmartActivity.this, NoticeActivity.class);
                intent6.putExtra("department","7");
                startActivity(intent6);
            }
        });
        FAQBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5 = new Intent(SmartActivity.this, FAQActivity.class);
                startActivity(intent5);
            }
        });
        knowledgeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent7 = new Intent(SmartActivity.this, KnowledgeActivity.class);
                intent7.putExtra("department", "스마트기기공학전공");
                startActivity(intent7);
            }
        });
        onlyQaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(SmartActivity.this,OnlyQuestionActivity.class);
                startActivity(intent2);
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
