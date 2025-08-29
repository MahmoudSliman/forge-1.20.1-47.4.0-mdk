package com.ghost.test.wave;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;

public class SpawnManager {

    public static Mob spawnMob(ServerLevel level, EntityType<? extends Mob> type, double x, double y, double z) {
        Mob mob = type.create(level);
        if (mob != null) {
            mob.moveTo(x, y, z, level.random.nextFloat() * 360F, 0.0F);
            level.addFreshEntity(mob);
        }
        return mob;
    }
}
