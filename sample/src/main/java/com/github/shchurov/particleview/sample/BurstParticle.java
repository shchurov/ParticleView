package com.github.shchurov.particleview.sample;

import com.github.shchurov.particleview.Particle;

class BurstParticle extends Particle {

    float vx;
    float vy;
    float vr;
    long timeLeft;

    BurstParticle(float width, float height, float x, float y, int texture, float vx, float vy, float vr,
                  long timeLeft) {
        super(width, height, x, y, texture);
        this.vx = vx;
        this.vy = vy;
        this.vr = vr;
        this.timeLeft = timeLeft;
    }

}
