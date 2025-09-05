package com.ghost.test.mob.mobs;

import com.ghost.test.mob.WaveMobType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;

import java.util.logging.Level;

public class SummonerZombie extends WaveMobType {

    public SummonerZombie() {
        super(
                EntityType.ZOMBIE,
                25.0,   // baseHealth
                0.22,   // baseSpeed
                4.0,    // baseDamage
                3,      // minWave
                2,      // baseMaxPerWave
                Component.literal(" Summoner Zombie").withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.BOLD)
        );
    }

    @Override
    public void onSpawn(Mob mob, Level level) {
        
    }

    // ممكن تسيب onSpawn فاضية أو تلغيها خالص
}
