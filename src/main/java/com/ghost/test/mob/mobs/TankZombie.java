package com.ghost.test.mob.mobs;

import com.ghost.test.mob.WaveMobType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;

import java.util.logging.Level;

public class TankZombie extends CustomZombie {
    public TankZombie() {
        super(
                EntityType.ZOMBIE,
                40.0,
                0.20,
                5.0,
                3,
                3,
                Component.literal(" Tank Zombie").withStyle(ChatFormatting.RED, ChatFormatting.BOLD)
        );
    }

    @Override
    public void onSpawn(Mob mob, Level level) {

    }
}
