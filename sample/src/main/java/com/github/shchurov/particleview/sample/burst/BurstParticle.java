package com.github.shchurov.particleview.sample.burst;

import com.github.shchurov.particleview.Particle;

class BurstParticle extends Particle {

    private static final int SIZE = 64;

    float vx;
    float vy;
    float vr;
    double timeLeft;

    BurstParticle() {
        super(SIZE, SIZE, 0, 0, 0);
    }

    void setup(float x, float y, int textureIndex, float vx, float vy, float vr, double timeLeft) {
        setX(x);
        setY(y);
        setTextureIndex(textureIndex);
        this.vx = vx;
        this.vy = vy;
        this.vr = vr;
        this.timeLeft = timeLeft;
    }

}
