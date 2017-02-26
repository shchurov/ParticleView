package com.github.shchurov.particleview.sample.spinner;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.shchurov.particleview.ParticleView;
import com.github.shchurov.particleview.sample.R;
import com.github.shchurov.particleview.sample.SampleTextureAtlasFactory;

public class SpinnerActivity extends AppCompatActivity {

    private static final int SPINNER_RADIUS_DP = 100;

    private ParticleView particleView;
    private Button btnStart;
    private TextView tvLoading;
    private SpinnerParticleSystem particleSystem;
    private boolean spinnerVisible;

    public static void start(Context context) {
        Intent i = new Intent(context, SpinnerActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner);
        particleView = (ParticleView) findViewById(R.id.particleView);
        tvLoading = (TextView) findViewById(R.id.tvLoading);
        btnStart = (Button) findViewById(R.id.btnStart);
        int spinnerRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, SPINNER_RADIUS_DP,
                getResources().getDisplayMetrics());
        particleSystem = new SpinnerParticleSystem(spinnerRadius);
        particleView.setTextureAtlasFactory(new SampleTextureAtlasFactory(getResources()));
        particleView.setParticleSystem(particleSystem);
        btnStart.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showLoading();
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideLoading();
                }
            }, 4500);
        }
    };

    private void showLoading() {
        spinnerVisible = true;
        particleSystem.show(particleView.getWidth() / 2, particleView.getHeight() / 2);
        btnStart.setVisibility(View.INVISIBLE);
        tvLoading.setVisibility(View.VISIBLE);
        particleView.startRendering();
    }

    private void hideLoading() {
        btnStart.setVisibility(View.VISIBLE);
        tvLoading.setVisibility(View.INVISIBLE);
        particleSystem.setOnDismissEndListener(new SpinnerParticleSystem.OnDismissEndListener() {
            @Override
            public void onDismissEnd() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        spinnerVisible = false;
                        particleView.stopRendering();
                    }
                });
            }
        });
        particleSystem.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (spinnerVisible) {
            particleView.startRendering();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        particleView.stopRendering();
    }

}
