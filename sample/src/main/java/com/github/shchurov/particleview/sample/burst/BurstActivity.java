package com.github.shchurov.particleview.sample.burst;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.github.shchurov.particleview.ParticleView;
import com.github.shchurov.particleview.SimpleTextureAtlasPacker;
import com.github.shchurov.particleview.TextureAtlas;
import com.github.shchurov.particleview.sample.R;

import java.util.Arrays;
import java.util.List;

public class BurstActivity extends AppCompatActivity {

    private ParticleView particleView;

    public static void start(Context context) {
        Intent i = new Intent(context, BurstActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burst);
        particleView = (ParticleView) findViewById(R.id.particleView);
        TextureAtlas textureAtlas = createTextureAtlas();
        final BurstParticleSystem particleSystem = new BurstParticleSystem();
        particleView.setTextureAtlas(textureAtlas);
        particleView.setParticleSystem(particleSystem);
        particleView.setFpsLogEnabled(true);
        particleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    particleSystem.addBurst(event.getX(), event.getY());
                }
                return true;
            }
        });
    }

    private TextureAtlas createTextureAtlas() {
        List<Integer> drawables = Arrays.asList(R.drawable.tex0, R.drawable.tex1, R.drawable.tex2, R.drawable.tex3,
                R.drawable.tex4, R.drawable.tex5, R.drawable.tex6, R.drawable.tex7, R.drawable.tex8, R.drawable.tex9,
                R.drawable.tex10, R.drawable.tex11, R.drawable.tex12, R.drawable.tex13, R.drawable.tex14,
                R.drawable.tex15);
        SimpleTextureAtlasPacker packer = new SimpleTextureAtlasPacker();
        return packer.pack(drawables, getResources(), 300, 300);
    }

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
