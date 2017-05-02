package com.example.suadahaji.todoapplication.views;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Created by suadahaji
 */

public class BaseActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences settings=getSharedPreferences("prefs",0);
        boolean firstRun=settings.getBoolean("firstRun",false);
        if(firstRun==false)
        // If running for the first time, Splash will load for first time
        {
            SharedPreferences.Editor editor=settings.edit();
            editor.putBoolean("firstRun",true);
            editor.commit();
            Intent i=new Intent(BaseActivity.this, SplashScreenActivity.class);
            startActivity(i);
            finish();
        }
        else
        {
            Intent a=new Intent(BaseActivity.this, MainActivity.class);
            startActivity(a);
            finish();
        }
    }

}
