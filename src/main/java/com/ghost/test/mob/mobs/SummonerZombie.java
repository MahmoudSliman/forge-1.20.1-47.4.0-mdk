package com.ghost.test.mob.mobs;

import com.ghost.test.mob.WaveMobType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;

public class SummonerZombie extends WaveMobType {
    public SummonerZombie() {
        super(
                EntityType.ZOMBIE,
                25.0,
                0.15,
                4.0,
                3,
                2,
                Component.literal("Summoner Zombie").withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.BOLD)
        );
    }
}
