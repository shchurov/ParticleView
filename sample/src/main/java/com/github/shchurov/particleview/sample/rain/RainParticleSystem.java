package com.github.shchurov.particleview.sample.rain;


import com.github.shchurov.particleview.Particle;
import com.github.shchurov.particleview.ParticleSystem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import static com.github.shchurov.particleview.sample.SampleTextureAtlasFactory.TEXTURE_COUNT;

class RainParticleSystem implements ParticleSystem {

    private static final int INTENSITY = 150;
    private static final boolean WIND_DIRECTION_LEFT = true;
    private static final int G = 600;
    private static final int MIN_PRIMARY_VX = 20;
    private static final int PRIMARY_VX_RANGE = 60;
    private static final int MIN_PRIMARY_VY = 700;
    private static final int PRIMARY_VY_RANGE = 150;
    private static final int MIN_SECONDARY_VX = 0;
    private static final int SECONDARY_VX_RANGE = 150;
    private static final int MIN_SECONDARY_VY = 50;
    private static final int SECONDARY_VY_RANGE = 500;
    private static final double SECONDARY_DURATION = 1.5;
    private static final double SECONDARY_FADE_DURATION = 0.3;
    private static final int SECONDARY_COUNT = 7;
    private static final float MAX_COUNT_RESERVE = 1.05f;
    private static final double REVERSED_INTENSITY = 1.0 / INTENSITY;

    private List<RainParticle> particles = new ArrayList<>();
    private ParticlesPool pool = new ParticlesPool();
    private Random random = new Random();
    private int minSpawnX;
    private int spawnXRange;
    private int height;
    private int maxCount;
    private double extraTime;

    @SuppressWarnings("ConstantConditions")
    void setDimensions(int width, int height) {
        this.height = height;
        int v0 = MIN_PRIMARY_VY;
        double primaryDuration = (-v0 + Math.sqrt(Math.pow(v0, 2) + 2 * G * height)) / G;
        int maxDx = (int) ((MIN_PRIMARY_VX + PRIMARY_VX_RANGE) * primaryDuration);
        minSpawnX = WIND_DIRECTION_LEFT ? 0 : -maxDx;
        spawnXRange = width + maxDx;
        maxCount = (int) (MAX_COUNT_RESERVE * INTENSITY * (SECONDARY_DURATION * SECONDARY_COUNT + primaryDuration));
    }

    @Override
    public int getMaxCount() {
        return maxCount;
    }

    @Override
    public List<? extends Particle> getParticles() {
        return particles;
    }

    @Override
    public void update(double timeDelta) {
        updateExistingParticles(timeDelta);
        spawnPrimaryParticles(timeDelta);
    }

    private void updateExistingParticles(double timeDelta) {
        for (int i = 0; i < particles.size(); i++) {
            RainParticle p = particles.get(i);
            if (!p.primary) {
                p.timeLeft -= timeDelta;
                p.setAlpha(Math.min(1f, (float) (p.timeLeft / SECONDARY_FADE_DURATION)));
                if (p.timeLeft <= 0) {
                    particles.remove(i--);
                    pool.recycle(p);
                    continue;
                }
            }
            p.setX(p.getX() + (float) (p.vx * timeDelta));
            float dvy = (float) (G * timeDelta);
            p.vy += dvy;
            float y = p.getY() + (float) ((p.vy - dvy / 2) * timeDelta);
            if (y > height) {
                if (p.primary) {
                    spawnSecondaryParticles(p.getX());
                    particles.remove(i--);
                    pool.recycle(p);
                } else {
                    p.setY(height);
                    p.vx = 0;
                }
            } else {
                p.setY(y);
            }
        }
    }

    private void spawnSecondaryParticles(float x) {
        for (int i = 0; i < SECONDARY_COUNT; i++) {
            RainParticle p = pool.obtainSecondary(
                    x,
                    height,
                    random.nextInt(TEXTURE_COUNT),
                    (random.nextBoolean() ? 1 : -1) * (MIN_SECONDARY_VX + nextGaussianInt(SECONDARY_VX_RANGE)),
                    -MIN_SECONDARY_VY - nextGaussianInt(SECONDARY_VY_RANGE),
                    SECONDARY_DURATION
            );
            particles.add(p);
        }
    }

    private int nextGaussianInt(int n) {
        // using 99.7 rule
        double d = Math.max(-3, Math.min(3, random.nextGaussian()));
        return (int) ((d + 3) * (n - 1) / 6);
    }

    @SuppressWarnings("ConstantConditions")
    private void spawnPrimaryParticles(double timeDelta) {
        double time = timeDelta + extraTime;
        int n = (int) (time / REVERSED_INTENSITY);
        extraTime = time % REVERSED_INTENSITY;
        for (int i = 0; i < n; i++) {
            RainParticle p = pool.obtainPrimary(
                    minSpawnX + random.nextInt(spawnXRange),
                    0,
                    random.nextInt(TEXTURE_COUNT),
                    (WIND_DIRECTION_LEFT ? -1 : 1) * (MIN_PRIMARY_VX + random.nextInt(PRIMARY_VX_RANGE)),
                    MIN_PRIMARY_VY + random.nextInt(PRIMARY_VY_RANGE));
            particles.add(p);
        }
    }

    private class ParticlesPool {

        Queue<RainParticle> pool = new LinkedList<>();

        RainParticle obtainPrimary(float x, float y, int textureIndex, float vx, float vy) {
            RainParticle p = pool.poll();
            if (p == null) {
                p = RainParticle.createPrimary(x, y, textureIndex, vx, vy);
            } else {
                p.setupAsPrimary(x, y, textureIndex, vx, vy);
            }
            return p;
        }

        RainParticle obtainSecondary(float x, float y, int textureIndex, float vx, float vy, double timeLeft) {
            RainParticle p = pool.poll();
            if (p == null) {
                p = RainParticle.createSecondary(x, y, textureIndex, vx, vy, timeLeft);
            } else {
                p.setupAsSecondary(x, y, textureIndex, vx, vy, timeLeft);
            }
            return p;
        }

        void recycle(RainParticle p) {
            pool.add(p);
        }

    }

}
