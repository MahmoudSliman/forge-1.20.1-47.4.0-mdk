package com.ghost.test.mob;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;

import java.util.List;

public class WaveMobRegistry {
    public static final List<WaveMobType> MOB_TYPES = List.of(
            new WaveMobType(EntityType.ZOMBIE, 20.0, 0.23, 1, Integer.MAX_VALUE,
                    Component.literal(" Zombie").withStyle(ChatFormatting.GRAY)),

            new WaveMobType(EntityType.ZOMBIE, 10.0, 0.35, 2, 5,
                    Component.literal(" Fast Zombie").withStyle(ChatFormatting.AQUA)),

            new WaveMobType(EntityType.ZOMBIE, 40.0, 0.20, 3, 3,
                    Component.literal(" Tank Zombie").withStyle(ChatFormatting.RED, ChatFormatting.BOLD)),

            new WaveMobType(EntityType.CREEPER, 20.0, 0.22, 4, 2,
                    Component.literal(" Creeper").withStyle(ChatFormatting.GREEN)),

            new WaveMobType(EntityType.HUSK, 25.0, 0.23, 1, 4,
                    Component.literal(" Desert Zombie").withStyle(ChatFormatting.YELLOW))
    );
}
