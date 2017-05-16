package com.github.shchurov.particleview;

import java.util.List;

/**
 * Used to update particles over time.
 */
public interface ParticleSystem {

    /**
     * Used for allocating buffers of sufficient size.
     * @return maximum expected number of particles that will be displayed at the same time
     */
    int getMaxCount();

    /**
     * Called before rendering every frame. Note that it's called from rendering thread (not UI).
     * @param timeDelta time passed since previous frame (in seconds)
     * @return list of particles to render
     */
    List<? extends Particle> update(double timeDelta);

}
