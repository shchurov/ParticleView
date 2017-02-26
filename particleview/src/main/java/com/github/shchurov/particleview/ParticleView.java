package com.github.shchurov.particleview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class ParticleView extends FrameLayout {

    private GlTextureView glTextureView;
    private ParticleRenderer renderer;

    public ParticleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        attachTextureView();
        setupTextureView();
    }

    private void attachTextureView() {
        glTextureView = new GlTextureView(getContext());
        glTextureView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(glTextureView);
    }

    private void setupTextureView() {
        glTextureView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        glTextureView.setEGLContextClientVersion(2);
        glTextureView.setOpaque(false);
        renderer = new ParticleRenderer();
        glTextureView.setRenderer(renderer);
        glTextureView.setRenderMode(GlTextureView.RENDERMODE_CONTINUOUSLY);
    }

    public void setTextureAtlasFactory(TextureAtlasFactory factory) {
        renderer.setTextureAtlasFactory(factory);
    }

    public void setParticleSystem(ParticleSystem system) {
        renderer.setParticleSystem(system);
    }

    public void setFpsLogEnabled(boolean enabled) {
        renderer.setFpsLogEnabled(enabled);
    }

    public void startRendering() {
        glTextureView.onResume();
    }

    public void stopRendering() {
        glTextureView.onPause();
    }

}
