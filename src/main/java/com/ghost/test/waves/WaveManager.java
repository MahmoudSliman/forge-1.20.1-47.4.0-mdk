package com.ghost.test.waves;

import com.ghost.test.killcounter.WaveKillCounter;
import net.minecraft.core.BlockPos;
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

        numberOfZombies = currentWave * 3;
        Random random = new Random();

        // Reset kills لكل لاعب في بداية الويف
        for (Player player : level.players()) {
            WaveKillCounter.resetKills(player);
            player.sendSystemMessage(
                    net.minecraft.network.chat.Component.literal("⚔️ Wave " + currentWave + " بدأت! عدد الزومبي: " + numberOfZombies)
            );
        }

        // إعداد جميع الزومبي بدون إضافتهم للعالم بعد
        for (int i = 0; i < numberOfZombies; i++) {
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
        player.sendSystemMessage(
                net.minecraft.network.chat.Component.literal("قتلت: " + kills + " / " + numberOfZombies)
        );

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

        for (Player player : currentLevel.players()) {
            player.sendSystemMessage(
                    net.minecraft.network.chat.Component.literal("✅ Wave " + currentWave + " انتهت!")
            );
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
