package com.github.shchurov.particleview.sample.rain;


import com.github.shchurov.particleview.Particle;

class RainParticle extends Particle {

    private static final int SIZE_PRIMARY = 24;
    private static final int SIZE_SECONDARY = 12;

    float vx;
    float vy;
    boolean primary;
    double timeLeft;

    static RainParticle createPrimary(float x, float y, int textureIndex, float vx, float vy) {
        RainParticle p = new RainParticle();
        p.setupAsPrimary(x, y, textureIndex, vx, vy);
        return p;
    }

    static RainParticle createSecondary(float x, float y, int textureIndex, float vx, float vy, double timeLeft) {
        RainParticle p = new RainParticle();
        p.setupAsSecondary(x, y, textureIndex, vx, vy, timeLeft);
        return p;

    }

    void setupAsPrimary(float x, float y, int textureIndex, float vx, float vy) {
        setup(true, x, y, textureIndex, vx, vy, 0);
    }

    private void setup(boolean primary, float x, float y, int textureIndex, float vx, float vy, double timeLeft) {
        int size = primary ? SIZE_PRIMARY : SIZE_SECONDARY;
        setWidth(size);
        setHeight(size);
        setX(x);
        setY(y);
        setTextureIndex(textureIndex);
        this.primary = primary;
        this.vx = vx;
        this.vy = vy;
        this.timeLeft = timeLeft;
        setAlpha(1f);
    }

    void setupAsSecondary(float x, float y, int textureIndex, float vx, float vy, double timeLeft) {
        setup(false, x, y, textureIndex, vx, vy, timeLeft);
    }

}
