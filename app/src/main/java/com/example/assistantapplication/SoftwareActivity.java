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
    private Button roomBtn;
    private Button officeBtn;
    private Button FAQBtn;
    private Button noticeBtn;
    private Button knowledgeBtn;
    private Switch pushSwitch;
    private long backKeyPressedTime = 0;
    private Toast toast;
    ImageButton menu_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_computer);
        setTitle("소프트웨어학과 조교관리 시스템");

        final Activity a = SoftwareActivity.this;

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
        roomBtn = findViewById(R.id.roomBtn);
        officeBtn = findViewById(R.id.officeBtn);
        FAQBtn = findViewById(R.id.FAQBtn);
        noticeBtn = findViewById(R.id.noticeBtn);
        knowledgeBtn = findViewById(R.id.knowledgeBtn);
        pushSwitch = findViewById(R.id.pushSwitch);
        pushSwitch.setChecked(true);
        menu_btn = findViewById(R.id.menu_btn);

        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("클릭");
                PopupMenu popup = new PopupMenu(getApplicationContext(), v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.popupmenu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().toString().equals("로그아웃"))
                        {
                            AlertDialog.Builder dlg = new AlertDialog.Builder(SoftwareActivity.this);
                            dlg.setMessage("로그아웃 하시겠습니까?");
                            dlg.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences preferences = getSharedPreferences("login",MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("ID", null);
                                    editor.putString("Password", null);
                                    editor.putString("Department","9999");
                                    editor.putString("Name",null);
                                    editor.commit();
                                    Intent intent = new Intent(SoftwareActivity.this, LoginActivity.class);
                                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            });
                            dlg.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            dlg.show();
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
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
                startActivity(intent7);
            }
        });

        pushSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    FirebaseMessaging.getInstance().subscribeToTopic("software");
                }
                else{
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("software");
                }
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
