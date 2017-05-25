package com.github.shchurov.particleview.sample.light;


import com.github.shchurov.particleview.Particle;
import com.github.shchurov.particleview.ParticleSystem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import static java.lang.Math.pow;

class BeamParticleSystem implements ParticleSystem {

    private static final int INTENSITY = 8;
    private static final int MIN_SIZE = 4;
    private static final int MAX_SIZE = 48;
    private static final float MAX_Y_VELOCITY = 300f;

    private int width;
    private float spawnY;
    private float maxLightY;
    private List<BeamParticle> particles = new ArrayList<>();
    private Random random = new Random();
    private ParticlesPool pool = new ParticlesPool();
    private double spawnCounter;

    BeamParticleSystem(int width, int height) {
        this.width = width;
        spawnY = 0.74f * height;
        maxLightY = 0.52f * height;
    }

    @Override
    public int getMaxCount() {
        float minVelocity = (float) (pow(MIN_SIZE / (float) MAX_SIZE, 2) * MAX_Y_VELOCITY);
        return (int) ((spawnY / minVelocity) * INTENSITY * 1.2f);
    }

    @Override
    public List<? extends Particle> update(double timeDelta) {
        updateExistingParticles(timeDelta);
        spawnNewParticles(timeDelta);
        return particles;
    }

    private void updateExistingParticles(double timeDelta) {
        for (int i = 0; i < particles.size(); i++) {
            BeamParticle p = particles.get(i);
            if (p.getY() + p.getHeight() < 0) {
                particles.remove(i--);
                pool.recycle(p);
                continue;
            }
            p.setY(p.getY() + (float) (p.vy * timeDelta));
            p.setRotation(p.getRotation() + (float) (p.vr * timeDelta));
            float light = calcLight(p);
            p.setAlpha(light * p.fade);
        }
    }

    private float calcLight(BeamParticle p) {
        if (p.getY() > maxLightY) {
            return 0.15f + 0.85f * ((spawnY - p.getY()) / (spawnY - maxLightY));
        } else {
            return 0.3f + 0.7f * (p.getY() / maxLightY);
        }
    }

    private void spawnNewParticles(double timeDelta) {
        double time = timeDelta + spawnCounter;
        int n = (int) (time * INTENSITY);
        spawnCounter = time % (1.0 / INTENSITY);
        for (int i = 0; i < n; i++) {
            int size = MIN_SIZE + random.nextInt(MAX_SIZE - MIN_SIZE);
            int x = random.nextInt(width);
            float fade = 0.6f + 0.4f * (size / (float) MAX_SIZE);
            float vy = (float) (-MAX_Y_VELOCITY * pow(size / (float) MAX_SIZE, 2));
            float vr = -1f + random.nextFloat() * 2;
            BeamParticle p = pool.obtain(size, x, spawnY + size, fade, vy, vr);
            p.setAlpha(0f);
            particles.add(p);
        }
    }

    private class ParticlesPool {

        Queue<BeamParticle> pool = new LinkedList<>();

        BeamParticle obtain(int size, float x, float y, float fade, float vy, float vr) {
            BeamParticle p = pool.poll();
            if (p == null) {
                p = new BeamParticle();
            }
            p.setup(size, x, y, fade, vy, vr);
            return p;
        }

        void recycle(BeamParticle p) {
            pool.add(p);
        }

    }

}
