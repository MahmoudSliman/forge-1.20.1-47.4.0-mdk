package com.ghost.test.mob;

import net.minecraft.world.entity.EntityType;

import java.util.List;

public class WaveMobRegistry {
    public static final List<WaveMobType> MOB_TYPES = List.of(
            new WaveMobType(EntityType.ZOMBIE, 20.0, 0.23, 1, Integer.MAX_VALUE), // عادي - من ويف 1 - بدون حد
            new WaveMobType(EntityType.ZOMBIE, 10.0, 0.35, 3, 5), // سريع وضعيف - من ويف 3 - 5 بس في الويف
            new WaveMobType(EntityType.ZOMBIE, 40.0, 0.20, 5, 3)  // تقيل وصحته عالية - من ويف 5 - 3 بس
    );
}
