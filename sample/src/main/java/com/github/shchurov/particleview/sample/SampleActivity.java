package com.github.shchurov.particleview.sample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.github.shchurov.particleview.ParticleView;
import com.github.shchurov.particleview.TextureAtlas;

public class SampleActivity extends AppCompatActivity {

    private ParticleView particleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
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
        final int size = 64;
        int[] drawables = {R.drawable.tex0, R.drawable.tex1, R.drawable.tex2, R.drawable.tex3, R.drawable.tex4,
                R.drawable.tex5, R.drawable.tex6, R.drawable.tex7, R.drawable.tex8, R.drawable.tex9, R.drawable.tex10,
                R.drawable.tex11, R.drawable.tex12, R.drawable.tex13, R.drawable.tex14, R.drawable.tex15};
        TextureAtlas atlas = new TextureAtlas(4 * size, 4 * size);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawables[i * 4 + j]);
                atlas.addRegion(j * size, i * size, bitmap);
            }
        }
        return atlas;
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
