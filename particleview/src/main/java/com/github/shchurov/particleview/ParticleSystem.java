package com.github.shchurov.particleview;

import java.util.List;

public interface ParticleSystem {

    int getMaxCount();

    List<? extends Particle> getParticles();

    void update();

}
