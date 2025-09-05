package com.ghost.test.mob.mobs;

import com.ghost.test.mob.WaveMobType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;

import java.util.logging.Level;

public class FastZombie extends CustomZombie {
    public FastZombie() {
        super(
                EntityType.ZOMBIE,
                10.0,
                0.35,
                3.0,
                2,
                5,
                Component.literal(" Fast Zombie").withStyle(ChatFormatting.AQUA)
        );
    }

    @Override
    public void onSpawn(Mob mob, Level level) {

    }
}
