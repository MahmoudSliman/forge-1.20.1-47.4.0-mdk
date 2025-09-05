package com.ghost.test.mob;

import com.ghost.test.mob.mobs.*;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;

import java.util.List;

public class WaveMobRegistry {
    public static final List<WaveMobType> MOB_TYPES = List.of(
            new NormalZombie(),
            new FastZombie(),
            new TankZombie(),
            new SummonerZombie(),
            new ExplodingZombie()
    );
}
