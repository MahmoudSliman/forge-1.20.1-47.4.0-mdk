package com.ghost.test.mob.mobs;

import com.ghost.test.mob.WaveMobType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;

import java.util.logging.Level;

public class ExplodingZombie extends CustomZombie {
    public ExplodingZombie() {
        super(
                EntityType.ZOMBIE,
                20.0,   // baseHealth
                0.23,   // baseSpeed
                4.0,    // baseDamage
                3,      // minWave
                5,      // baseMaxPerWave
                Component.literal(" Exploding Zombie").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)
        );
    }

    @Override
    public void onSpawn(Mob mob, Level level) {

    }
}
