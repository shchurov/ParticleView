package com.github.shchurov.particleview.sample.rain;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.shchurov.particleview.ParticleView;
import com.github.shchurov.particleview.TextureAtlas;
import com.github.shchurov.particleview.sample.R;
import com.github.shchurov.particleview.sample.TextureAtlasFactory;

public class RainActivity extends AppCompatActivity {

    private ParticleView particleView;

    public static void start(Context context) {
        Intent i = new Intent(context, RainActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rain);
        particleView = (ParticleView) findViewById(R.id.particleView);
        TextureAtlas textureAtlas = TextureAtlasFactory.createTextureAtlas(getResources());
        final RainParticleSystem particleSystem = new RainParticleSystem();
        particleView.setTextureAtlas(textureAtlas);
        particleView.setFpsLogEnabled(true);
        final View vFloor = findViewById(R.id.vFloor);
        vFloor.post(new Runnable() {
            @Override
            public void run() {
                particleSystem.setDimensions(particleView.getWidth(), (int) vFloor.getY());
                particleView.setParticleSystem(particleSystem);
            }
        });
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
