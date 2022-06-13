package com.example.instagram;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("hw7v1KCSBfAG4odQLphhh4JcIEI1F3JKennRcj0z")
                .clientKey("JLILOGlf2ncnmSvwpRaYmwxAUBnNzqL3LG7pQv9b")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }

}
