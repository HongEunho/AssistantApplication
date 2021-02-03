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
    private Button roomBtn;
    private Button officeBtn;
    private Button noticeBtn;
    private Button FAQBtn;
    private Button knowledgeBtn;
    private long backKeyPressedTime = 0;
    private Toast toast;
    private Switch pushSwitch;
    ImageButton menu_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_computer);

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
        roomBtn = findViewById(R.id.roomBtn);
        officeBtn = findViewById(R.id.officeBtn);
        noticeBtn = findViewById(R.id.noticeBtn);
        FAQBtn = findViewById(R.id.FAQBtn);
        knowledgeBtn = findViewById(R.id.knowledgeBtn);
        onlyQaBtn = findViewById(R.id.onlyQaBtn);
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
                            AlertDialog.Builder dlg = new AlertDialog.Builder(SmartActivity.this);
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
                                    Intent intent = new Intent(SmartActivity.this, LoginActivity.class);
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
                Intent intent = new Intent(SmartActivity.this, StatusActivity.class);
                startActivity(intent);
            }
        });

        roomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(SmartActivity.this,ComputerTimeActivity.class);
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

        pushSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    FirebaseMessaging.getInstance().subscribeToTopic("Smart");
                }
                else{
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Smart");
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
