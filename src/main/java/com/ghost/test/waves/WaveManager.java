package com.ghost.test.waves;

import com.ghost.test.killcounter.WaveKillCounter;
import com.ghost.test.mob.WaveMobRegistry;
import com.ghost.test.mob.WaveMobType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WaveManager {
    private final List<Mob> allMobs = new ArrayList<>();
    private final List<Mob> activeMobs = new ArrayList<>();

    // إعدادات الـ spawn
    private int spawnDelayTicks = 20;
    private int tickCounter = 0;
    private int spawnIndex = 0;

    private List<BlockPos> markerPositions = new ArrayList<>();
    private Level currentLevel;

    private int numberOfMobs = 0;

    public void startNextWave(Level level, List<BlockPos> markers) {
        if (markers.isEmpty() || isWaveActive()) return;

        if (level instanceof ServerLevel serverLevel) {
            WaveProgressData data = WaveProgressData.get(serverLevel);
            data.setCurrentWave(data.getCurrentWave() + 1);
            data.setWaveActive(true);
        }

        spawnIndex = 0;
        tickCounter = 0;

        allMobs.clear();
        activeMobs.clear();
        this.markerPositions = markers;
        this.currentLevel = level;

        numberOfMobs = getCurrentWave() * 5;
        Random random = new Random();

        // Reset kills لكل لاعب + عرض عنوان البداية
        for (Player p : currentLevel.players()) {
            if (p instanceof ServerPlayer player) {
                player.connection.send(
                        new net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket(
                                Component.literal(" Wave " + getCurrentWave() + " Started! : ")
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
            if (getCurrentWave() >= mobType.getMinWave()) {
                int count = Math.min(mobType.getMaxPerWaveForWave(getCurrentWave()), getCurrentWave());

                for (int i = 0; i < count; i++) {
                    Mob mob = (Mob) mobType.getEntityType().create(level);
                    if (mob != null) {
                        double health = mobType.getHealthForWave(getCurrentWave());
                        double speed = mobType.getSpeedForWave(getCurrentWave());
                        double damage = mobType.getDamageForWave(getCurrentWave());

                        if (mob.getAttribute(Attributes.MAX_HEALTH) != null) {
                            mob.getAttribute(Attributes.MAX_HEALTH).setBaseValue(health);
                        }
                        mob.setHealth((float) health);

                        if (mob.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
                            mob.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(speed);
                        }

                        if (mob.getAttribute(Attributes.ATTACK_DAMAGE) != null) {
                            mob.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(damage);
                        }

                        mob.setCustomName(mobType.getDisplayName());
                        mob.setCustomNameVisible(true);

                        mob.getPersistentData().putBoolean("WaveMob", true);

                        // 🟢 هنا بيتفعل السلوك الإضافي (مثلاً استدعاء الزومبي الصغير)
                        mobType.onSpawn(mob, level);

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
        if (!isWaveActive() || allMobs.isEmpty() || currentLevel == null) return;

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
        if (!isWaveActive()) return;

        int kills = WaveKillCounter.getKills(player);

        if (kills >= numberOfMobs) {
            endWave();
        }
    }

    public void checkWaveStatus() {
        activeMobs.removeIf(z -> z.isRemoved() || !z.getPersistentData().getBoolean("WaveMob"));

        if (activeMobs.isEmpty() && isWaveActive()) {
            endWave();
        }
    }

    private void endWave() {
        if (currentLevel instanceof ServerLevel serverLevel) {
            WaveProgressData data = WaveProgressData.get(serverLevel);
            data.setWaveActive(false);
        }

        for (Player p : currentLevel.players()) {
            if (p instanceof ServerPlayer player) {
                player.connection.send(
                        new net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket(
                                Component.literal("✅ Wave " + getCurrentWave() + " ended!")
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
        if (currentLevel instanceof ServerLevel serverLevel) {
            return WaveProgressData.get(serverLevel).isWaveActive();
        }
        return false;
    }

    public int getCurrentWave() {
        if (currentLevel instanceof ServerLevel serverLevel) {
            return WaveProgressData.get(serverLevel).getCurrentWave();
        }
        return 0;
    }

    public void resetWaves() {
        if (currentLevel instanceof ServerLevel serverLevel) {
            WaveProgressData data = WaveProgressData.get(serverLevel);
            data.setCurrentWave(0);
            data.setWaveActive(false);
        }

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
