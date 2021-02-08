package com.example.assistantapplication;

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
import android.widget.PopupMenu;
import android.widget.Switch;

import com.google.firebase.messaging.FirebaseMessaging;

public class SettingActivity extends AppCompatActivity {
    public Switch pushSwitch;
    public Switch pushSwitch2;
    private Button logoutBtn;
    String dep;
    String mydep_flag;
    String alldep_flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        pushSwitch = findViewById(R.id.pushSwitch);
        pushSwitch2 = findViewById(R.id.pushSwitch2);
        logoutBtn = findViewById(R.id.logoutBtn);

        final SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        dep = preferences.getString("pushdep","1");
        mydep_flag = preferences.getString("mydep_push", "true");
        alldep_flag = preferences.getString("alldep_push", "true");

        if(mydep_flag.equals("true")){
            pushSwitch.setChecked(true);
            mypushOk(dep);
        }
        else {
            pushSwitch.setChecked(false);
            mypushNo(dep);
        }

        if(alldep_flag.equals("true")) {
            pushSwitch2.setChecked(true);
            mypushOk("public");
        }
        else {
            pushSwitch2.setChecked(false);
            mypushNo("public");
        }

        pushSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mypushOk(dep);
                }
                else{
                    mypushNo(dep);
                }
            }
        });

        pushSwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mypushOk("public");
                }
                else{
                    mypushNo("public");
                }
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(SettingActivity.this);
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
                        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
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
        });
    }

    public void mypushOk(String dep){
        FirebaseMessaging.getInstance().subscribeToTopic(dep);
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if(dep.equals("public"))
            editor.putString("alldep_push","true");

        else
            editor.putString("mydep_push", "true");

        editor.commit();
    }

    public void mypushNo(String dep){
        FirebaseMessaging.getInstance().unsubscribeFromTopic(dep);
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if(dep.equals("public"))
            editor.putString("alldep_push", "false");
        else
            editor.putString("mydep_push", "false");

        editor.commit();
    }
}