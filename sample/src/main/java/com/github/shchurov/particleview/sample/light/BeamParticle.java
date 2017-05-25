package com.github.shchurov.particleview.sample.light;

import com.github.shchurov.particleview.Particle;

class BeamParticle extends Particle {

    float fade;
    float vy;
    float vr;

    void setup(int size, float x, float y, float fade, float vy, float vr) {
        setWidth(size);
        setHeight(size);
        setX(x);
        setY(y);
        this.fade = fade;
        this.vy = vy;
        this.vr = vr;
    }

}
