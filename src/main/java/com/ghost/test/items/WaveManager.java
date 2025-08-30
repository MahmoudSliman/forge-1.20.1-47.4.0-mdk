package com.ghost.test.items;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;

import java.util.List;
import java.util.Random;

public class WaveManager {
    private int currentWave = 0;

    public void startNextWave(Level level, List<BlockPos> markerPositions) {
        if (markerPositions.isEmpty()) return;

        currentWave++;

        int numberOfZombies = currentWave * 3; // كل Wave يزود 3 زومبي
        Random random = new Random(); // <-- هنا عرفنا random

        for (int i = 0; i < numberOfZombies; i++) {
            Zombie zombie = EntityType.ZOMBIE.create(level);
            if (zombie != null) {
                // نختار ماركر عشوائي لكل زومبي
                BlockPos markerPos = markerPositions.get(random.nextInt(markerPositions.size()));
                zombie.moveTo(markerPos.getX() + 0.5, markerPos.getY() + 1, markerPos.getZ() + 0.5, 0.0F, 0.0F);
                level.addFreshEntity(zombie);
            }
        }

        System.out.println("Wave " + currentWave + " started at multiple markers!");
    }

    public int getCurrentWave() {
        return currentWave;
    }
}
