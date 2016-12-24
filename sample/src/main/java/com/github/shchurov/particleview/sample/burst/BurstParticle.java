package com.github.shchurov.particleview.sample.burst;

import com.github.shchurov.particleview.Particle;

class BurstParticle extends Particle {

    private static final int SIZE = 64;

    float vx;
    float vy;
    float vr;
    double timeLeft;

    BurstParticle(float x, float y, int texture, float vx, float vy, float vr, double timeLeft) {
        super(SIZE, SIZE, x, y, texture);
        this.vx = vx;
        this.vy = vy;
        this.vr = vr;
        this.timeLeft = timeLeft;
    }

}
