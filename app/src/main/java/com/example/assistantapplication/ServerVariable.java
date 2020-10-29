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
}
