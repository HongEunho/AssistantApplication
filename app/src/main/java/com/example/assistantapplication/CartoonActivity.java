package com.example.assistantapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

public class CartoonActivity extends AppCompatActivity {
    //만화 애니메이션텍 액티비티입니다.
    public static Context mContext;
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
        mContext = this;


        ((ServerVariable)getApplicationContext()).unsubscribePush("cartoon");

        staBtn = findViewById(R.id.staBtn);
        onlyQaBtn = findViewById(R.id.onlyQaBtn);
        modifyBtn = findViewById(R.id.datamodifyBtn);
        officeBtn = findViewById(R.id.officeBtn);
        noticeBtn = findViewById(R.id.noticeBtn);
        FAQBtn = findViewById(R.id.FAQBtn);
        knowledgeBtn = findViewById(R.id.knowledgeBtn);

        staBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartoonActivity.this, StatusActivity.class);
                startActivity(intent);
            }
        });

        onlyQaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(CartoonActivity.this,OnlyQuestionActivity.class);
                startActivity(intent2);
            }
        });
        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(CartoonActivity.this,DatamodifyActivity.class);
                startActivity(intent3);
            }
        });
        officeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(CartoonActivity.this, ProfessorActivity.class);
                startActivity(intent4);
            }
        });
        noticeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent6 = new Intent(CartoonActivity.this, NoticeActivity.class);
                intent6.putExtra("department","만화애니메이션텍");
                startActivity(intent6);
            }
        });
        FAQBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5 = new Intent(CartoonActivity.this, FAQActivity.class);
                startActivity(intent5);
            }
        });
        knowledgeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent7 = new Intent(CartoonActivity.this, KnowledgeActivity.class);
                intent7.putExtra("department", "만화애니메이션텍");
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
