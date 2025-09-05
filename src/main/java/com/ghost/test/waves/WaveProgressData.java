package com.ghost.test.waves;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class WaveProgressData extends SavedData {
    private static final String ID = "wave_progress";

    private int currentWave = 0;
    private boolean waveActive = false;

    public int getCurrentWave() {
        return currentWave;
    }

    public void setCurrentWave(int wave) {
        this.currentWave = wave;
        setDirty();
    }

    public boolean isWaveActive() {
        return waveActive;
    }

    public void setWaveActive(boolean active) {
        this.waveActive = active;
        setDirty();
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putInt("CurrentWave", currentWave);
        tag.putBoolean("WaveActive", waveActive);
        return tag;
    }

    public static WaveProgressData load(CompoundTag tag) {
        WaveProgressData data = new WaveProgressData();
        data.currentWave = tag.getInt("CurrentWave");
        data.waveActive = tag.getBoolean("WaveActive");
        return data;
    }

    public static WaveProgressData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(WaveProgressData::load, WaveProgressData::new, ID);
    }
}
