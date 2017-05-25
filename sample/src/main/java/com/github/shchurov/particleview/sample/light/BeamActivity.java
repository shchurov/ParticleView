package com.github.shchurov.particleview.sample.light;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.shchurov.particleview.ParticleView;
import com.github.shchurov.particleview.TextureAtlas;
import com.github.shchurov.particleview.TextureAtlasFactory;
import com.github.shchurov.particleview.sample.R;

public class BeamActivity extends AppCompatActivity {

    private ParticleView particleView;

    public static void start(Context context) {
        Intent i = new Intent(context, BeamActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beam);
        particleView = (ParticleView) findViewById(R.id.particleView);
        particleView.setTextureAtlasFactory(textureAtlasFactory);
        particleView.post(new Runnable() {
            @Override
            public void run() {
                int width = particleView.getWidth();
                int height = particleView.getHeight();
                particleView.setParticleSystem(new BeamParticleSystem(width, height));
            }
        });
    }

    private TextureAtlasFactory textureAtlasFactory = new TextureAtlasFactory() {
        @Override
        public TextureAtlas createTextureAtlas() {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.triangle);
            TextureAtlas atlas = new TextureAtlas(bmp.getWidth(), bmp.getHeight());
            atlas.addRegion(0, 0, bmp);
            return atlas;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        particleView.startRendering();
    }

    @Override
    protected void onPause() {
        super.onPause();
        particleView.stopRendering();
    }

}
