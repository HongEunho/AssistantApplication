package com.example.assistantapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

public class SoftwareActivity extends AppCompatActivity {

    private Button staBtn;
    private Button onlyQaBtn;
    private Button modifyBtn;
    private Button officeBtn;
    private Button FAQBtn;
    private Button noticeBtn;
    private Button knowledgeBtn;
    private long backKeyPressedTime = 0;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_computer);
        setTitle("소프트웨어학과 조교관리 시스템");

        final Activity a = SoftwareActivity.this;

        ((ServerVariable)getApplicationContext()).unsubscribePush("software");

        FirebaseMessaging.getInstance().subscribeToTopic("software")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }

                    }
                });
        staBtn = findViewById(R.id.staBtn);
        onlyQaBtn = findViewById(R.id.onlyQaBtn);
        modifyBtn = findViewById(R.id.datamodifyBtn);
        officeBtn = findViewById(R.id.officeBtn);
        FAQBtn = findViewById(R.id.FAQBtn);
        noticeBtn = findViewById(R.id.noticeBtn);
        knowledgeBtn = findViewById(R.id.knowledgeBtn);

        staBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SoftwareActivity.this, StatusActivity.class);
                startActivity(intent);
            }
        });
        onlyQaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(SoftwareActivity.this,OnlyQuestionActivity.class);
                startActivity(intent2);
            }
        });
        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(SoftwareActivity.this,DatamodifyActivity.class);
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
        noticeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent6 = new Intent(SoftwareActivity.this, NoticeActivity.class);
                intent6.putExtra("department","소프트웨어학과");
                startActivity(intent6);
            }
        });
        knowledgeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent7 = new Intent(SoftwareActivity.this, KnowledgeActivity.class);
                intent7.putExtra("department", "소프트웨어학과");
                startActivity(intent7);
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
