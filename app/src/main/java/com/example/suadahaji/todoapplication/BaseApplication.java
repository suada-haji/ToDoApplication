package com.example.suadahaji.todoapplication;

import android.app.Application;
import android.graphics.Typeface;

/**
 * Created by suadahaji
 */

public class BaseApplication extends Application {
    public static Typeface ROBOTO_BLACK;
    public static Typeface ROBOTO_LIGHT;
    public static Typeface ROBOTO_MEDIUM;
    public static Typeface ROBOTO_REGULAR;

    @Override
    public void onCreate() {
        super.onCreate();
        initFonts();
    }

    private void initFonts() {
        ROBOTO_BLACK = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Black.ttf");
        ROBOTO_LIGHT = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        ROBOTO_MEDIUM = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
        ROBOTO_REGULAR = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
    }
}
