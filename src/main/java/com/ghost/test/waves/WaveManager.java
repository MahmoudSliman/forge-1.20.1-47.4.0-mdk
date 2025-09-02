package com.ghost.test.waves;

import com.ghost.test.killcounter.WaveKillCounter;
import com.ghost.test.mob.WaveMobRegistry;
import com.ghost.test.mob.WaveMobType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WaveManager {
    private int currentWave = 0;
    private boolean waveActive = false;

    private final List<Mob> allMobs = new ArrayList<>();
    private final List<Mob> activeMobs = new ArrayList<>();

    // إعدادات الـspawn
    private int spawnDelayTicks = 20;
    private int tickCounter = 0;
    private int spawnIndex = 0;

    private List<BlockPos> markerPositions = new ArrayList<>();
    private Level currentLevel;

    private int numberOfMobs = 0;

    public void startNextWave(Level level, List<BlockPos> markers) {
        if (markers.isEmpty() || waveActive) return;

        waveActive = true;
        currentWave++;
        spawnIndex = 0;
        tickCounter = 0;

        allMobs.clear();
        activeMobs.clear();
        this.markerPositions = markers;
        this.currentLevel = level;

        numberOfMobs = currentWave * 5;
        Random random = new Random();

        // Reset kills لكل لاعب
        for (Player p : currentLevel.players()) {
            if (p instanceof ServerPlayer player) {
                player.connection.send(
                        new net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket(
                                Component.literal(" Wave " + currentWave + " Started! : ")
                                        .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)
                        )
                );

                player.connection.send(
                        new net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket(
                                Component.literal("🎉 Get Ready!!!!")
                                        .withStyle(ChatFormatting.AQUA)
                        )
                );

                player.connection.send(
                        new net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket(
                                10, 60, 10
                        )
                );
            }
        }

        // موبات خاصة من WaveMobRegistry
        for (WaveMobType mobType : WaveMobRegistry.MOB_TYPES) {
            if (currentWave >= mobType.minWave) {
                int count = Math.min(mobType.maxPerWave, currentWave);
                for (int i = 0; i < count; i++) {
                    Mob mob = mobType.type.create(level);
                    if (mob != null) {
                        if (mob.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH) != null) {
                            mob.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH).setBaseValue(mobType.health);
                        }
                        mob.setHealth((float) mobType.health);

                        if (mob.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED) != null) {
                            mob.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED).setBaseValue(mobType.speed);
                        }

                        // الاسم من WaveMobRegistry
                        mob.setCustomName(mobType.displayName);
                        mob.setCustomNameVisible(true);

                        mob.getPersistentData().putBoolean("WaveMob", true);
                        allMobs.add(mob);
                        activeMobs.add(mob);
                    }

                }
            }
        }

        // نملأ الباقي بزومبي عادي
        int remaining = numberOfMobs - allMobs.size();
        for (int i = 0; i < remaining; i++) {
            Zombie zombie = EntityType.ZOMBIE.create(level);
            if (zombie != null) {
                zombie.setCustomName(Component.literal("Zombie").withStyle(ChatFormatting.GRAY));
                zombie.setCustomNameVisible(true);

                zombie.getPersistentData().putBoolean("WaveMob", true);
                allMobs.add(zombie);
                activeMobs.add(zombie);
            }
        }
    }

    // دالة تتنادى كل tick
    public void tick() {
        if (!waveActive || allMobs.isEmpty() || currentLevel == null) return;

        tickCounter++;
        if (tickCounter < spawnDelayTicks) return;
        tickCounter = 0;

        if (spawnIndex >= allMobs.size()) return;

        Mob mob = allMobs.get(spawnIndex);
        if (mob != null && !mob.isAddedToWorld()) {
            Random random = new Random();
            BlockPos markerPos = markerPositions.get(random.nextInt(markerPositions.size()));
            mob.moveTo(markerPos.getX() + 0.5, markerPos.getY() + 1, markerPos.getZ() + 0.5, 0.0F, 0.0F);
            currentLevel.addFreshEntity(mob);
        }

        spawnIndex++;
    }

    public void onZombieKilled(Player player) {
        if (!waveActive) return;

        int kills = WaveKillCounter.getKills(player);

        if (kills >= numberOfMobs) {
            endWave();
        }
    }

    public void checkWaveStatus() {
        activeMobs.removeIf(z -> z.isRemoved() || !z.getPersistentData().getBoolean("WaveMob"));

        if (activeMobs.isEmpty() && waveActive) {
            endWave();
        }
    }

    private void endWave() {
        waveActive = false;

        for (Player p : currentLevel.players()) {
            if (p instanceof ServerPlayer player) {
                player.connection.send(
                        new net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket(
                                Component.literal("✅ Wave " + currentWave + " ended!")
                                        .withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD)
                        )
                );

                player.connection.send(
                        new net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket(
                                10, 60, 10
                        )
                );
            }
        }

        allMobs.clear();
        currentLevel = null;
    }

    public boolean isWaveActive() {
        return waveActive;
    }

    public int getCurrentWave() {
        return currentWave;
    }

    public void resetWaves() {
        currentWave = 0;
        waveActive = false;
        allMobs.clear();
        activeMobs.clear();
        markerPositions.clear();
        currentLevel = null;
        spawnIndex = 0;
        tickCounter = 0;
        numberOfMobs = 0;
    }

    public void setSpawnDelayTicks(int ticks) {
        this.spawnDelayTicks = ticks;
    }

    public int getSpawnDelayTicks() {
        return spawnDelayTicks;
    }

    private String getMobName(EntityType<?> type) {
        if (type == EntityType.ZOMBIE) return "Zombie";
        if (type == EntityType.CREEPER) return "Creeper";
        if (type == EntityType.HUSK) return "Husk";
        return type.toShortString();
    }
}
