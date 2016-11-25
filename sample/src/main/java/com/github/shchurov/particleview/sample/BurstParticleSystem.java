package com.github.shchurov.particleview.sample;

import android.graphics.PointF;

import com.github.shchurov.particleview.Particle;
import com.github.shchurov.particleview.ParticleSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

class BurstParticleSystem implements ParticleSystem {

    private static final int MAX_P = 5000;
    private static final int BURST_P_COUNT = 100;
    private static final int P_WIDTH = 64;
    private static final int P_HEIGHT = 64;
    private static final int BURST_DURATION = 2000;
    private static final int FADE_DURATION = 400;
    private static final float MAX_VY = 0.5f;
    private static final float MAX_VX = 0.5f;
    private static final float MAX_VR = 0.01f;

    private List<BurstParticle> particles = new ArrayList<>();
    private Queue<PointF> originsQueue = new ConcurrentLinkedQueue<>();
    private long prevUpdate = -1;
    private Random random = new Random();

    @Override
    public int getMaxCount() {
        return MAX_P;
    }

    @Override
    public List<? extends Particle> getParticles() {
        return particles;
    }

    void addBurst(float x, float y) {
        originsQueue.add(new PointF(x, y));
    }

    @Override
    public void update() {
        long time = System.currentTimeMillis();
        if (prevUpdate != -1) {
            updateParticles(time - prevUpdate);
            pollOrigins();
        }
        prevUpdate = time;
    }

    private void updateParticles(long timeDelta) {
        for (int i = 0; i < particles.size(); i++) {
            BurstParticle p = particles.get(i);
            if ((p.timeLeft -= timeDelta) < 0) {
                particles.remove(i--);
                continue;
            }
            p.setX(p.getX() + p.vx * timeDelta);
            p.setY(p.getY() + p.vy * timeDelta);
            p.setRotation(p.getRotation() + p.vr * timeDelta);
            p.setAlpha(Math.min(1f, p.timeLeft / (float) FADE_DURATION));
        }
    }

    private void pollOrigins() {
        while (originsQueue.size() > 0) {
            PointF origin = originsQueue.poll();
            int n = Math.min(BURST_P_COUNT, MAX_P - particles.size());
            for (int i = 0; i < n; i++) {
                float vx = (random.nextBoolean() ? 1 : -1) * MAX_VX * random.nextFloat();
                float vy = (random.nextBoolean() ? 1 : -1) * MAX_VY * random.nextFloat();
                float vr = (random.nextBoolean() ? 1 : -1) * MAX_VR * random.nextFloat();
                particles.add(new BurstParticle(P_WIDTH, P_HEIGHT, origin.x, origin.y, random.nextInt(16), vx, vy, vr,
                        BURST_DURATION));
            }
        }
    }

}
