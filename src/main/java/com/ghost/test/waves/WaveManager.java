package com.ghost.test.waves;

import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WaveManager {
    private int currentWave = 0;
    private boolean waveActive = false;

    private final List<Zombie> activeZombies = new ArrayList<>();
    private final List<BlockPos> markerPositions = new ArrayList<>();
    private int zombiesToSpawn = 0;
    private int spawnedZombies = 0;

    private int spawnDelayTicks = 60; // 3 ثانية = 60 تيكس
    private int tickCounter = 0;
    private Level currentLevel;

    private final Random random = new Random();

    // تعديل التاخير بين كل Zombie
    public void setSpawnDelay(int ticks) {
        this.spawnDelayTicks = ticks;
    }

    public void startNextWave(Level level, List<BlockPos> markers) {
        if (markers.isEmpty() || waveActive) return;

        waveActive = true;
        currentWave++;
        markerPositions.clear();
        markerPositions.addAll(markers);

        zombiesToSpawn = currentWave * 3;
        spawnedZombies = 0;
        tickCounter = 0;
        activeZombies.clear();
        currentLevel = level;

        System.out.println("✅ Wave " + currentWave + " بدأت عند " + markers.size() + " ماركرز!");
    }

    // استدعاء كل Tick
    public void tick() {
        if (!waveActive || currentLevel == null) return;

        tickCounter++;
        // ريسبون Zombie بعد انتهاء التاخير
        if (spawnedZombies < zombiesToSpawn && tickCounter >= spawnDelayTicks) {
            tickCounter = 0;

            BlockPos markerPos = markerPositions.get(random.nextInt(markerPositions.size()));
            Zombie zombie = EntityType.ZOMBIE.create(currentLevel);
            if (zombie != null) {
                zombie.moveTo(markerPos.getX() + 0.5, markerPos.getY() + 1, markerPos.getZ() + 0.5, 0f, 0f);
                zombie.getPersistentData().putBoolean("WaveZombie", true);
                currentLevel.addFreshEntity(zombie);
                activeZombies.add(zombie);
            }

            spawnedZombies++;
        }

        // إزالة الزومبي اللي ماتوا
        activeZombies.removeIf(z -> z.isRemoved() || !z.getPersistentData().getBoolean("WaveZombie"));

        // انتهاء Wave
        if (spawnedZombies >= zombiesToSpawn && activeZombies.isEmpty()) {
            waveActive = false;
            System.out.println("✅ Wave " + currentWave + " انتهت!");
        }
    }

    public boolean isWaveActive() {
        return waveActive;
    }

    public int getCurrentWave() {
        return currentWave;
    }

    public void resetWaves() {
        currentWave = 0;
        waveActive = false;
        activeZombies.clear();
        markerPositions.clear();
        spawnedZombies = 0;
        tickCounter = 0;
        currentLevel = null;
    }
}
