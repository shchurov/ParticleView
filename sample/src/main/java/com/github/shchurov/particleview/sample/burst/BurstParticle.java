package com.github.shchurov.particleview.sample.burst;

import com.github.shchurov.particleview.Particle;

class BurstParticle extends Particle {

    private static final int WIDTH = 64;
    private static final int HEIGHT = 64;

    float vx;
    float vy;
    float vr;
    double timeLeft;

    BurstParticle(float x, float y, int texture, float vx, float vy, float vr, double timeLeft) {
        super(WIDTH, HEIGHT, x, y, texture);
        this.vx = vx;
        this.vy = vy;
        this.vr = vr;
        this.timeLeft = timeLeft;
    }

}
