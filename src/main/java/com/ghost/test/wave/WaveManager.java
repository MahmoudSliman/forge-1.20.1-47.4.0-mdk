package com.ghost.test.wave;

import net.minecraft.server.level.ServerLevel;

/**
 * مسؤول عن إدارة الموجات.
 */
public class WaveManager {
    private final IRng rng;
    private final WaveState state;

    public WaveManager(IRng rng) {
        this.rng = rng;
        this.state = new WaveState();
    }

    public void startWave(ServerLevel level, int waveNumber) {
        state.currentWave = waveNumber;
        state.aliveCount = 0;
        state.killCounter = 0;
        System.out.println("Wave " + waveNumber + " started!");
    }

    public void tick(ServerLevel level) {
        // هنا هنيجي نضيف منطق السباون بعدين
    }

    public WaveState getState() {
        return state;
    }
}
