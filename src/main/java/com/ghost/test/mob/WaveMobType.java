package com.ghost.test.mob;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;

public class WaveMobType {
    public final EntityType<? extends Mob> type;
    public final double health;
    public final double speed;
    public final int minWave;
    public final int maxPerWave;
    public final Component displayName; // الاسم اللي يبان فوق الموب

    public WaveMobType(EntityType<? extends Mob> type, double health, double speed, int minWave, int maxPerWave, Component displayName) {
        this.type = type;
        this.health = health;
        this.speed = speed;
        this.minWave = minWave;
        this.maxPerWave = maxPerWave;
        this.displayName = displayName;
    }

// هنا نحسب الليمت الديناميكي
    public int getLimitForWave(int currentWave) {
        if (currentWave < minWave) {
            return 0; // لسه ما يترسبنش
        }
        // مثال: بيزيد +1 كل ويف بعد minWave
        return maxPerWave + (currentWave - minWave);
        // لو عايز +2 كل ويف بدل +1:
        // return maxPerWave + (currentWave - minWave) * 2;
    }
}
