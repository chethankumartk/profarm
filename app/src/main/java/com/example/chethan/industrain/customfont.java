package com.example.chethan.industrain;

import android.app.Application;

public class customfont extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "DEFAULT", "font/Raleway-Regular.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "font/Raleway-Regular.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "font/Raleway-Regular.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "font/Raleway-Regular.ttf");
    }
}