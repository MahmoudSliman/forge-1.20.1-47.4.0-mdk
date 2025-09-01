package com.ghost.test.mob;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;

public class WaveMobType {
    public final EntityType<? extends Mob> type;
    public final double health;
    public final double speed;
    public final int minWave; // يبدأ يرسبن من ويف كام
    public final int maxPerWave; // أقصى عدد في كل ويف

    public WaveMobType(EntityType<? extends Mob> type, double health, double speed, int minWave, int maxPerWave) {
        this.type = type;
        this.health = health;
        this.speed = speed;
        this.minWave = minWave;
        this.maxPerWave = maxPerWave;
    }
}
