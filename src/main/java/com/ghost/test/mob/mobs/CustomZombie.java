package com.ghost.test.mob.mobs;

import com.ghost.test.mob.WaveMobType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.Random;

public abstract class CustomZombie extends WaveMobType {

    private static final Random RANDOM = new Random();

    public CustomZombie(EntityType<?> entityType, double health, double speed, double damage,
                        int minWave, int maxPerWave, Component name) {
        super(entityType, health, speed, damage, minWave, maxPerWave, name);
    }

    // Loot افتراضي لكل الزومبي: 2-3 Gold Nuggets
    public void dropLoot(Mob mob, Level level) {
        if (!level.isClientSide) {
            int count = 2 + RANDOM.nextInt(2); // 2 أو 3
            ItemStack loot = new ItemStack(Items.GOLD_NUGGET, count);
            mob.spawnAtLocation(loot);
        }
    }

    @Override
    public void onSpawn(Mob mob, Level level) {
        // أي سلوك إضافي عند الظهور
    }
}
