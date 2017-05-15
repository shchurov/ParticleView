package com.github.shchurov.particleview.sample.getting_started;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.shchurov.particleview.Particle;
import com.github.shchurov.particleview.ParticleSystem;
import com.github.shchurov.particleview.ParticleView;
import com.github.shchurov.particleview.TextureAtlas;
import com.github.shchurov.particleview.TextureAtlasFactory;
import com.github.shchurov.particleview.sample.R;

import java.util.Collections;
import java.util.List;

public class GettingStartedActivity extends AppCompatActivity {

    private ParticleView particleView;

    public static void start(Context context) {
        Intent i = new Intent(context, GettingStartedActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getting_started);
        particleView = (ParticleView) findViewById(R.id.particleView);
        particleView.setTextureAtlasFactory(textureAtlasFactory);
        particleView.setParticleSystem(particleSystem);
    }

    private ParticleSystem particleSystem = new ParticleSystem() {
        List<Particle> particles = Collections.singletonList(new Particle(200, 200, 200, 400, 0));

        @Override
        public int getMaxCount() {
            return 1;
        }

        @Override
        public List<? extends Particle> update(double timeDelta) {
            // static image
            return particles;
        }
    };

    private TextureAtlasFactory textureAtlasFactory = new TextureAtlasFactory() {
        @Override
        public TextureAtlas createTextureAtlas() {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
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
