package com.ghost.test.waves;

import com.ghost.test.killcounter.WaveKillCounter;
import com.ghost.test.mob.WaveMobRegistry;
import com.ghost.test.mob.WaveMobType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WaveManager {
    private int currentWave = 0;
    private boolean waveActive = false;

    private final List<Zombie> allZombies = new ArrayList<>();
    private final List<Zombie> activeZombies = new ArrayList<>();

    // إعدادات الـspawn
    private int spawnDelayTicks = 20; // عدد ticks بين كل Zombie
    private int tickCounter = 0;
    private int spawnIndex = 0;

    private List<BlockPos> markerPositions = new ArrayList<>();
    private Level currentLevel;

    private int numberOfZombies = 0; // عدد الزومبي المطلوب في الويف

    public void startNextWave(Level level, List<BlockPos> markers) {
        if (markers.isEmpty() || waveActive) return;

        waveActive = true;
        currentWave++;
        spawnIndex = 0;
        tickCounter = 0;

        allZombies.clear();
        activeZombies.clear();
        this.markerPositions = markers;
        this.currentLevel = level;

        numberOfZombies = currentWave * 5;
        Random random = new Random();

        // Reset kills لكل لاعب في بداية الويف
        for (Player p : currentLevel.players()) {
            if (p instanceof ServerPlayer player) {
                // Title
                player.connection.send(
                        new net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket(
                                Component.literal(" Wave " + currentWave + " Started! : ")
                                        .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD) // لون دهبي وخط عريض
                        )
                );

                // Subtitle
                player.connection.send(
                        new net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket(
                                Component.literal("🎉 Get Ready!!!!")
                                        .withStyle(ChatFormatting.AQUA) // لون أزرق فاتح
                        )
                );

                // Animation (fade in/out)
                player.connection.send(
                        new net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket(
                                10,   // fade in
                                60,   // stay
                                10    // fade out
                        )
                );
            }
        }


        // الأول نضيف الأنواع الخاصة
        for (WaveMobType mobType : WaveMobRegistry.MOB_TYPES) {
            if (currentWave >= mobType.minWave) {
                int count = Math.min(mobType.maxPerWave, currentWave); // عدد الأنواع الخاصة
                for (int i = 0; i < count; i++) {
                    var mob = mobType.type.create(level);
                    if (mob != null) {
                        // تطبيق الإعدادات (health, speed)
                        if (mob.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH) != null) {
                            mob.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH).setBaseValue(mobType.health);
                        }
                        mob.setHealth((float) mobType.health);

                        if (mob.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED) != null) {
                            mob.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED).setBaseValue(mobType.speed);
                        }

                        mob.getPersistentData().putBoolean("WaveZombie", true);
                        allZombies.add((Zombie) mob);
                        activeZombies.add((Zombie) mob);
                    }
                }
            }
        }

// نملأ الباقي بزومبي عادي
        int remaining = numberOfZombies - allZombies.size();
        for (int i = 0; i < remaining; i++) {
            Zombie zombie = EntityType.ZOMBIE.create(level);
            if (zombie != null) {
                zombie.getPersistentData().putBoolean("WaveZombie", true);
                allZombies.add(zombie);
                activeZombies.add(zombie);
            }
        }

    }

    // دالة تتنادى كل tick للتحكم في الـspawn بالتأخير
    public void tick() {
        if (!waveActive || allZombies.isEmpty() || currentLevel == null) return;

        tickCounter++;
        if (tickCounter < spawnDelayTicks) return;
        tickCounter = 0;

        if (spawnIndex >= allZombies.size()) return;

        Zombie zombie = allZombies.get(spawnIndex);
        if (zombie != null && !zombie.isAddedToWorld()) {
            Random random = new Random();
            BlockPos markerPos = markerPositions.get(random.nextInt(markerPositions.size()));
            zombie.moveTo(markerPos.getX() + 0.5, markerPos.getY() + 1, markerPos.getZ() + 0.5, 0.0F, 0.0F);
            currentLevel.addFreshEntity(zombie);
        }

        spawnIndex++;
    }

    // دالة تستدعى عند قتل زومبي
    public void onZombieKilled(Player player) {
        if (!waveActive) return;

        int kills = WaveKillCounter.getKills(player);


        if (kills >= numberOfZombies) {
            endWave();
        }
    }

    // التحقق من انتهاء Wave
    public void checkWaveStatus() {
        activeZombies.removeIf(z -> z.isRemoved() || !z.getPersistentData().getBoolean("WaveZombie"));

        if (activeZombies.isEmpty() && waveActive) {
            endWave();
        }
    }

    // إنهاء الويف
    private void endWave() {
        waveActive = false;

        for (Player p : currentLevel.players()) {
            if (p instanceof ServerPlayer player) {
                player.connection.send(
                        new net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket(
                                Component.literal("✅ Wave " + currentWave + " ended!")
                                        .withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD) // أخضر وعريض
                        )
                );

                player.connection.send(
                        new net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket(
                                10, // fade in
                                60, // stay
                                10  // fade out
                        )
                );
            }
        }




        // إعادة تعيين متغيرات الـWave
        allZombies.clear();
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
        allZombies.clear();
        activeZombies.clear();
        markerPositions.clear();
        currentLevel = null;
        spawnIndex = 0;
        tickCounter = 0;
        numberOfZombies = 0;
    }

    public void setSpawnDelayTicks(int ticks) {
        this.spawnDelayTicks = ticks;
    }

    public int getSpawnDelayTicks() {
        return spawnDelayTicks;
    }
}
