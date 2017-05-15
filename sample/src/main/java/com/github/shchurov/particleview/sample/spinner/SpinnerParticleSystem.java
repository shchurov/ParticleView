package com.github.shchurov.particleview.sample.spinner;


import com.github.shchurov.particleview.Particle;
import com.github.shchurov.particleview.ParticleSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.github.shchurov.particleview.sample.SampleTextureAtlasFactory.TEXTURE_COUNT;

class SpinnerParticleSystem implements ParticleSystem {

    private enum Stage {SHOW, SPIN, DISMISS, NONE}

    private static final int P_COUNT = 150;
    private static final float SHOW_DISMISS_DURATION = 0.3f;
    private static final float VR = 1f;

    private List<SpinnerParticle> particles = new ArrayList<>();
    private volatile Stage stage = Stage.NONE;
    private volatile float cx;
    private volatile float cy;
    private float stageTime;
    private volatile OnDismissEndListener endListener;

    SpinnerParticleSystem(int spinnerRadius) {
        Random random = new Random();
        for (int i = 0; i < P_COUNT; i++) {
            float spinRadius = (0.7f + random.nextFloat() * 0.3f) * spinnerRadius;
            float spinRotation = (float) (random.nextFloat() * 2 * Math.PI);
            particles.add(new SpinnerParticle(random.nextInt(TEXTURE_COUNT), spinRadius, spinRotation));
        }
    }

    public void setOnDismissEndListener(OnDismissEndListener listener) {
        endListener = listener;
    }

    @Override
    public int getMaxCount() {
        return P_COUNT;
    }

    @Override
    public List<? extends Particle> update(double timeDelta) {
        if (stage == Stage.NONE) {
            return particles;
        }
        float progressFactor = 1f;
        if (stage == Stage.SHOW || stage == Stage.DISMISS) {
            float progress = stageTime / SHOW_DISMISS_DURATION;
            if (progress <= 1f) {
                if (stage == Stage.SHOW) {
                    progressFactor = progress;
                } else {
                    progressFactor = 1f - progress;
                }
                stageTime += timeDelta;
            } else {
                if (stage == Stage.SHOW) {
                    stage = Stage.SPIN;
                } else {
                    progressFactor = 0f;
                    stage = Stage.NONE;
                    if (endListener != null) {
                        endListener.onDismissEnd();
                    }
                }
                stageTime = 0;
            }
        }
        updateParticles(timeDelta, progressFactor);
        return particles;
    }

    private void updateParticles(double timeDelta, float progressFactor) {
        for (int i = 0; i < particles.size(); i++) {
            SpinnerParticle p = particles.get(i);
            p.setRotation(p.getRotation() + (float) (VR * timeDelta));
            p.setX(cx + p.spinRadius * progressFactor * (float) Math.cos(p.getRotation()));
            p.setY(cy + p.spinRadius * progressFactor * (float) Math.sin(p.getRotation()));
            p.setAlpha(progressFactor);
        }

    }

    void show(int cx, int cy) {
        this.cx = cx;
        this.cy = cy;
        stage = Stage.SHOW;
    }

    void dismiss() {
        stage = Stage.DISMISS;
    }

    interface OnDismissEndListener {
        void onDismissEnd();
    }

}
