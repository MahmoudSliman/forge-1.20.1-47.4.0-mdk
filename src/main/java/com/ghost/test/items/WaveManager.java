package com.ghost.test.items;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;

public class WaveManager {
    private int currentWave = 0;

    public void startNextWave(Level level, BlockPos pos) {
        currentWave++;

        int numberOfZombies = currentWave * 3; // كل Wave يزود 3 زومبي
        for (int i = 0; i < numberOfZombies; i++) {
            Zombie zombie = EntityType.ZOMBIE.create(level);
            if (zombie != null) {
                zombie.moveTo(pos.offset(i, 0, 0), 0.0F, 0.0F);
                level.addFreshEntity(zombie);
            }
        }

        System.out.println("Wave " + currentWave + " started!");
    }

    public int getCurrentWave() {
        return currentWave;
    }
}
