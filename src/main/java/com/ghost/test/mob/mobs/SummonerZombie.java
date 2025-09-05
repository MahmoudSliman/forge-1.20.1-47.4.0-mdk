package com.ghost.test.mob.mobs;

import com.ghost.test.mob.WaveMobType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Zombie;

import java.util.EnumSet;

public class SummonerZombie extends WaveMobType {
    public SummonerZombie() {
        super(
                EntityType.ZOMBIE,
                25.0,   // baseHealth
                0.22,   // baseSpeed
                4.0,    // baseDamage
                3,      // minWave (يظهر من Wave 5 مثلاً)
                2,      // baseMaxPerWave (قليل)
                Component.literal(" Summoner Zombie").withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.BOLD)
        );
    }

    @Override
    public void onSpawn(Mob mob, net.minecraft.world.level.Level level) {
        if (mob instanceof Zombie zombie && level instanceof ServerLevel serverLevel) {
            zombie.goalSelector.addGoal(1, new SummonBabiesGoal(zombie, serverLevel));
        }
    }

    // الكلاس الداخلي المسؤول عن الاستدعاء
    static class SummonBabiesGoal extends Goal {
        private final Zombie parent;
        private final ServerLevel level;
        private int cooldown = 0;
        private int summoned = 0;

        public SummonBabiesGoal(Zombie parent, ServerLevel level) {
            this.parent = parent;
            this.level = level;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return true; // دايمًا شغال
        }

        @Override
        public void tick() {
            if (cooldown > 0) {
                cooldown--;
                return;
            }

            if (summoned < 3) { // يستدعي 3 بس
                Zombie baby = EntityType.ZOMBIE.create(level);
                if (baby != null) {
                    baby.setBaby(true); // يخليه صغير
                    baby.moveTo(parent.getX(), parent.getY(), parent.getZ(), parent.getYRot(), parent.getXRot());
                    level.addFreshEntity(baby);
                    summoned++;
                }
            }

            cooldown = 200; // 200 تيك = 10 ثواني
        }
    }
}
