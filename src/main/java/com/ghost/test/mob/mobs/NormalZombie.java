package com.ghost.test.mob.mobs;

import com.ghost.test.mob.WaveMobType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;

public class NormalZombie extends WaveMobType {
    public NormalZombie() {
        super(
                EntityType.ZOMBIE,
                20.0,
                0.23,
                3.0,
                1,
                Integer.MAX_VALUE,
                Component.literal(" Zombie").withStyle(ChatFormatting.GRAY)
        );
    }
}


