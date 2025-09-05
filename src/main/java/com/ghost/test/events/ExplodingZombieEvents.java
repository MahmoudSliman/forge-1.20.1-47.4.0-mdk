package com.ghost.test.events;

import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

@Mod.EventBusSubscriber
public class ExplodingZombieEvents {
    @SubscribeEvent
    public static void onZombieDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Zombie zombie) {
            // نتاكد انه "Exploding Zombie" (بـ customName مثلاً)
            if (zombie.hasCustomName() && zombie.getName().getString().contains("Exploding Zombie")) {
                Level level = zombie.level();
                if (!level.isClientSide) {
                    level.explode(
                            zombie,
                            zombie.getX(),
                            zombie.getY(),
                            zombie.getZ(),
                            2.0F, // قوة الانفجار
                            false,
                            Level.ExplosionInteraction.NONE // ما يكسرش بلوكات
                    );
                }
            }
        }
    }
}
