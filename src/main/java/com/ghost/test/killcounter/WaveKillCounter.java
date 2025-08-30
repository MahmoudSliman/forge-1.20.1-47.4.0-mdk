package com.ghost.test.killcounter;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WaveKillCounter {

    public WaveKillCounter() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    // زيادة الكيلز عند موت زومبي من الـ Wave
    @SubscribeEvent
    public void onZombieDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Zombie zombie)) return;
        if (!zombie.getPersistentData().getBoolean("WaveZombie")) return; // نتأكد انه Wave Zombie

        if (event.getSource().getEntity() instanceof Player player) {
            addKill(player);
        }
    }

    // إضافة kill في الـ Scoreboard
    public static void addKill(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        Scoreboard scoreboard = serverPlayer.level().getServer().getScoreboard();
        Objective objective = scoreboard.getObjective("waveKills");

        if (objective == null) {
            objective = scoreboard.addObjective("waveKills", ObjectiveCriteria.DUMMY,
                    Component.literal("Wave Kills"), ObjectiveCriteria.RenderType.INTEGER);
        }

        Score score = scoreboard.getOrCreatePlayerScore(player.getName().getString(), objective);
        score.setScore(score.getScore() + 1);

        // نرسل رسالة بالشات
        serverPlayer.sendSystemMessage(Component.literal("✅ قتلت " + score.getScore() + " Wave Zombies حتى الآن"));
    }


    public static void resetKills(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        Scoreboard scoreboard = serverPlayer.level().getServer().getScoreboard();
        Objective objective = scoreboard.getObjective("waveKills");

        if (objective == null) return;

        Score score = scoreboard.getOrCreatePlayerScore(player.getName().getString(), objective);
        score.setScore(0);
    }

    public static int getKills(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) return 0;

        Scoreboard scoreboard = serverPlayer.level().getServer().getScoreboard();
        Objective objective = scoreboard.getObjective("waveKills");
        if (objective == null) return 0;

        Score score = scoreboard.getOrCreatePlayerScore(player.getName().getString(), objective);
        return score.getScore();
    }

}
