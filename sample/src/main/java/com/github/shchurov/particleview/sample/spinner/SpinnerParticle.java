package com.github.shchurov.particleview.sample.spinner;


import com.github.shchurov.particleview.Particle;

class SpinnerParticle extends Particle {

    private static final int HEIGHT = 24;
    private static final int WIDTH = 24;

    float spinRadius;

    SpinnerParticle(int texture, float spinRadius, float rotation) {
        super(WIDTH, HEIGHT, 0, 0, texture);
        this.spinRadius = spinRadius;
        setRotation(rotation);
        setAlpha(0f);
    }
}
