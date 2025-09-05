package com.ghost.test.mob.mobs;

import com.ghost.test.mob.WaveMobType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;

public class TankZombie extends WaveMobType {
    public TankZombie() {
        super(
                EntityType.ZOMBIE,
                40.0,   // baseHealth (كبير)
                0.20,   // baseSpeed (بطيء)
                5.0,    // baseDamage (أعلى من العادي)
                3,      // minWave (يبدأ يظهر من Wave 3)
                3,      // baseMaxPerWave (عدد قليل)
                Component.literal(" Tank Zombie").withStyle(ChatFormatting.RED, ChatFormatting.BOLD)
        );
    }
}
