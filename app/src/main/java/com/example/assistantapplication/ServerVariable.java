package com.example.assistantapplication;

import android.app.Activity;
import android.app.Application;

import com.google.firebase.messaging.FirebaseMessaging;

import org.aviran.cookiebar2.CookieBar;

public class ServerVariable extends Application {
    public String server;
    public String alldep[] = {"public", "computer", "software", "datascience", "im", "information", "design", "cartoon", "smart", "imc", "ai"};
    public String getSer(){
        return "https://sjswbot.site";
    }

    public void Cookie(Activity a){
        CookieBar.build(a)
                .setCookiePosition(CookieBar.TOP)
                .setBackgroundColor(R.color.positiveColor)
                .setMessage("수정 되었습니다")
                .show();
    }

    public void LoginCookie(Activity a){
        CookieBar.build(a)
                .setCookiePosition(CookieBar.TOP)
                .setBackgroundColor(R.color.welcomeColor)
                .setMessage("환영합니다!")
                .show();
    }

    public void LoginFail(Activity a){
        CookieBar.build(a)
                .setCookiePosition(CookieBar.TOP)
                .setBackgroundColor(R.color.welcomeColor)
                .setMessage("아이디, 비밀번호를 확인하세요")
                .show();
    }

    public void insertSuccess(Activity a){
        CookieBar.build(a)
                .setCookiePosition(CookieBar.TOP)
                .setBackgroundColor(R.color.welcomeColor)
                .setMessage("추가 되었습니다.")
                .show();
    }

    public void deleteSuccess(Activity a){
        CookieBar.build(a)
                .setCookiePosition(CookieBar.TOP)
                .setBackgroundColor(R.color.welcomeColor)
                .setMessage("삭제 되었습니다.")
                .show();
    }

    public void unsubscribePush(String mydep){
        for(String s:alldep){
            if(!s.equals(mydep))
                FirebaseMessaging.getInstance().unsubscribeFromTopic(s);
        }
    }
}
