package com.example.assistantapplication;

import android.app.Activity;
import android.app.Application;

import org.aviran.cookiebar2.CookieBar;

public class ServerVariable extends Application {
    public String server;

    public String getSer(){
        return "https://mfam.site";
    }

    public void Cookie(Activity a){
        CookieBar.build(a)
                .setCookiePosition(CookieBar.BOTTOM)
                .setBackgroundColor(R.color.positiveColor)
                .setMessage("수정 되었습니다")
                .show();
    }

    public void LoginCookie(Activity a){
        CookieBar.build(a)
                .setCookiePosition(CookieBar.BOTTOM)
                .setBackgroundColor(R.color.welcomeColor)
                .setMessage("환영합니다!")
                .show();
    }

    public void LoginFail(Activity a){
        CookieBar.build(a)
                .setCookiePosition(CookieBar.BOTTOM)
                .setBackgroundColor(R.color.welcomeColor)
                .setMessage("아이디, 비밀번호를 확인하세요")
                .show();
    }

    public void insertSuccess(Activity a){
        CookieBar.build(a)
                .setCookiePosition(CookieBar.BOTTOM)
                .setBackgroundColor(R.color.welcomeColor)
                .setMessage("추가 되었습니다.")
                .show();
    }
}
