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
import com.github.shchurov.particleview.SimpleTextureAtlasPacker;
import com.github.shchurov.particleview.TextureAtlas;
import com.github.shchurov.particleview.sample.R;

import java.util.Arrays;
import java.util.List;

public class SpinnerActivity extends AppCompatActivity {

    private static final int SPINNER_RADIUS_DP = 100;

    private ParticleView particleView;
    private Button btnStart;
    private TextView tvLoading;
    private SpinnerParticleSystem particleSystem;

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
        TextureAtlas textureAtlas = createTextureAtlas();
        int spinnerRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, SPINNER_RADIUS_DP,
                getResources().getDisplayMetrics());
        particleSystem = new SpinnerParticleSystem(spinnerRadius);
        particleView.setTextureAtlas(textureAtlas);
        particleView.setParticleSystem(particleSystem);
        btnStart.setOnClickListener(onClickListener);
    }

    private TextureAtlas createTextureAtlas() {
        List<Integer> drawables = Arrays.asList(R.drawable.tex0, R.drawable.tex1, R.drawable.tex2, R.drawable.tex3,
                R.drawable.tex4, R.drawable.tex5, R.drawable.tex6, R.drawable.tex7, R.drawable.tex8, R.drawable.tex9,
                R.drawable.tex10, R.drawable.tex11, R.drawable.tex12, R.drawable.tex13, R.drawable.tex14,
                R.drawable.tex15);
        SimpleTextureAtlasPacker packer = new SimpleTextureAtlasPacker();
        return packer.pack(drawables, getResources(), 300, 300);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            particleSystem.show(particleView.getWidth() / 2, particleView.getHeight() / 2);
            btnStart.setVisibility(View.INVISIBLE);
            tvLoading.setVisibility(View.VISIBLE);
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    particleSystem.dismiss();
                    btnStart.setVisibility(View.VISIBLE);
                    tvLoading.setVisibility(View.INVISIBLE);
                }
            }, 4500);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        particleView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        particleView.onPause();
    }

}
