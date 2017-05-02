package com.example.suadahaji.todoapplication.views;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.suadahaji.todoapplication.R;
import com.example.suadahaji.todoapplication.utils.BaseApplication;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashScreenActivity extends AppCompatActivity {

    @BindView(R.id.tv_splash)
    TextView tvSplashText;
    @BindView(R.id.btn_splash)
    Button btnSplashBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ButterKnife.bind(this);

        tvSplashText.setTypeface(BaseApplication.ROBOTO_MEDIUM);
        btnSplashBtn.setTypeface(BaseApplication.ROBOTO_BOLD);
        btnSplashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStarted();
            }
        });

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    // Proceeds to the Main Activity
    public void getStarted() {
        Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
