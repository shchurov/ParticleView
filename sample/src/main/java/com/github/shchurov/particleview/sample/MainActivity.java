package com.github.shchurov.particleview.sample;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.shchurov.particleview.sample.burst.BurstActivity;
import com.github.shchurov.particleview.sample.getting_started.GettingStartedActivity;
import com.github.shchurov.particleview.sample.rain.RainActivity;
import com.github.shchurov.particleview.sample.spinner.SpinnerActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnGettingStarted).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GettingStartedActivity.start(MainActivity.this);
            }
        });
        findViewById(R.id.btnBurst).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BurstActivity.start(MainActivity.this);
            }
        });
        findViewById(R.id.btnSpinner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpinnerActivity.start(MainActivity.this);
            }
        });
        findViewById(R.id.btnRain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RainActivity.start(MainActivity.this);
            }
        });
    }
}
