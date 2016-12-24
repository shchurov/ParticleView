package com.github.shchurov.particleview.sample.spinner;


import com.github.shchurov.particleview.Particle;

class SpinnerParticle extends Particle {

    private static final int SIZE = 24;

    float spinRadius;

    SpinnerParticle(int texture, float spinRadius, float rotation) {
        super(SIZE, SIZE, 0, 0, texture);
        this.spinRadius = spinRadius;
        setRotation(rotation);
        setAlpha(0f);
    }
}
