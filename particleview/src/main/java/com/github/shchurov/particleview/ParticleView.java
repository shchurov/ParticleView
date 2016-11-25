package com.github.shchurov.particleview;

import android.content.Context;
import android.util.AttributeSet;

public class ParticleView extends GlTextureView {

    private ParticleRenderer renderer;

    public ParticleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        setEGLContextClientVersion(2);
        setOpaque(false);
        renderer = new ParticleRenderer();
        setRenderer(renderer);
        setRenderMode(RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setTextureAtlas(TextureAtlas atlas) {
        renderer.setTextureAtlas(atlas);
    }

    public void setParticleSystem(ParticleSystem system) {
        renderer.setParticleSystem(system);
    }

    public void setFpsLogEnabled(boolean enabled) {
        renderer.setFpsLogEnabled(enabled);
    }

}
