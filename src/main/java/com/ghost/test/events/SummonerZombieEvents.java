package com.ghost.test.events;

import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.ai.goal.Goal;

@Mod.EventBusSubscriber
public class SummonerZombieEvents {

    @SubscribeEvent
    public static void onZombieSpawn(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Zombie zombie) {
            if (zombie.hasCustomName() && zombie.getName().getString().contains("Summoner Zombie")) {
                if (event.getLevel() instanceof ServerLevel serverLevel) {
                    zombie.goalSelector.addGoal(1, new SummonBabiesGoal(zombie, serverLevel));
                }
            }
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
            this.setFlags(java.util.EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public void tick() {
            if (cooldown > 0) { cooldown--; return; }

            if (summoned < 3) {
                Zombie baby = EntityType.ZOMBIE.create(level);
                if (baby != null) {
                    baby.setBaby(true);
                    baby.moveTo(parent.getX(), parent.getY(), parent.getZ(), parent.getYRot(), parent.getXRot());
                    level.addFreshEntity(baby);
                    summoned++;
                }
            }

            cooldown = 200;
        }
    }
}
