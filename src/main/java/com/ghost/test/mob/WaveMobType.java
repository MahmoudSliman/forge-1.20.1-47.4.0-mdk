package com.ghost.test.mob;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level; // ✅ استخدم Level الصح مش java.util.logging.Level

public abstract class WaveMobType {
    private final EntityType<?> entityType;
    private final double baseHealth;
    private final double baseSpeed;
    private final double baseDamage;
    private final int minWave;
    private final int baseMaxPerWave;
    private final Component displayName;

    public WaveMobType(EntityType<?> entityType, double baseHealth, double baseSpeed, double baseDamage,
                       int minWave, int baseMaxPerWave, Component displayName) {
        this.entityType = entityType;
        this.baseHealth = baseHealth;
        this.baseSpeed = baseSpeed;
        this.baseDamage = baseDamage;
        this.minWave = minWave;
        this.baseMaxPerWave = baseMaxPerWave;
        this.displayName = displayName;
    }

    public EntityType<?> getEntityType() { return entityType; }
    public Component getDisplayName() { return displayName; }
    public int getMinWave() { return minWave; }

    // Health scaling (+5% per wave)
    public double getHealthForWave(int wave) {
        return baseHealth * Math.pow(1.05, wave - 1);
    }

    // Speed ثابت
    public double getSpeedForWave(int wave) {
        return baseSpeed;
    }

    // Damage scaling (+2% per wave)
    public double getDamageForWave(int wave) {
        return baseDamage * Math.pow(1.02, wave - 1);
    }

    // Max per wave scaling (+8% per wave)
    public int getMaxPerWaveForWave(int wave) {
        return (int) Math.ceil(baseMaxPerWave * Math.pow(1.08, wave - 1));
    }

    // 🟢 onSpawn الافتراضية (أي كلاس يقدر يعمل override لو عايز)
    public void onSpawn(Mob mob, Level level) {
        // افتراضي: مفيش حاجة
    }

    public abstract void onSpawn(Mob mob, java.util.logging.Level level);
}
