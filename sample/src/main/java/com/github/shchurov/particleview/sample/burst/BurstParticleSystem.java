package com.github.shchurov.particleview.sample.burst;

import android.graphics.PointF;

import com.github.shchurov.particleview.Particle;
import com.github.shchurov.particleview.ParticleSystem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.github.shchurov.particleview.sample.SampleTextureAtlasFactory.TEXTURE_COUNT;

class BurstParticleSystem implements ParticleSystem {

    private static final int MAX_P_COUNT = 3000;
    private static final int BURST_P_COUNT = 100;
    private static final int BURST_DURATION = 2;
    private static final double FADE_DURATION = 0.4;
    private static final int MAX_VY = 500;
    private static final int MAX_VX = 500;
    private static final int MAX_VR = 10;

    private List<BurstParticle> particles = new ArrayList<>();
    private Queue<PointF> originsQueue = new ConcurrentLinkedQueue<>();
    private Random random = new Random();
    private ParticlesPool pool = new ParticlesPool();

    @Override
    public int getMaxCount() {
        return MAX_P_COUNT;
    }

    void addBurst(float x, float y) {
        originsQueue.add(new PointF(x, y));
    }

    @Override
    public List<? extends Particle> update(double timeDelta) {
        updateExistingParticles(timeDelta);
        pollOrigins();
        return particles;
    }

    private void updateExistingParticles(double timeDelta) {
        for (int i = 0; i < particles.size(); i++) {
            BurstParticle p = particles.get(i);
            if ((p.timeLeft -= timeDelta) < 0) {
                particles.remove(i--);
                pool.recycle(p);
                continue;
            }
            p.setX(p.getX() + (float) (p.vx * timeDelta));
            p.setY(p.getY() + (float) (p.vy * timeDelta));
            p.setRotation(p.getRotation() + (float) (p.vr * timeDelta));
            p.setAlpha(Math.min(1f, (float) (p.timeLeft / FADE_DURATION)));
        }
    }

    private void pollOrigins() {
        while (originsQueue.size() > 0) {
            PointF origin = originsQueue.poll();
            int n = Math.min(BURST_P_COUNT, MAX_P_COUNT - particles.size());
            for (int i = 0; i < n; i++) {
                BurstParticle p = generateParticle(origin.x, origin.y);
                particles.add(p);
            }
        }
    }

    private BurstParticle generateParticle(float x, float y) {
        float vx = (random.nextBoolean() ? 1 : -1) * MAX_VX * random.nextFloat();
        float vy = (random.nextBoolean() ? 1 : -1) * MAX_VY * random.nextFloat();
        float vr = (random.nextBoolean() ? 1 : -1) * MAX_VR * random.nextFloat();
        return pool.obtain(x, y, random.nextInt(TEXTURE_COUNT), vx, vy, vr, BURST_DURATION);
    }

    private class ParticlesPool {

        Queue<BurstParticle> pool = new LinkedList<>();

        BurstParticle obtain(float x, float y, int textureIndex, float vx, float vy, float vr, double timeLeft) {
            BurstParticle p = pool.poll();
            if (p == null) {
                p = new BurstParticle();
            }
            p.setup(x, y, textureIndex, vx, vy, vr, timeLeft);
            return p;
        }

        void recycle(BurstParticle p) {
            pool.add(p);
        }

    }

}
