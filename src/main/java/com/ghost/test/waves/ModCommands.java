package com.ghost.test.waves;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

public class ModCommands {

    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {

        // Reset wave progress
        dispatcher.register(Commands.literal("resetwaves")
                .executes(context -> {
                    ServerLevel serverLevel = context.getSource().getLevel();

                    // Reset runtime waves
                    WaveItem.waveManager.resetWaves();

                    // Reset saved progress
                    WaveProgressData data = WaveProgressData.get(serverLevel);
                    data.setCurrentWave(0);
                    data.setWaveActive(false);
                    data.setDirty(); // مهم عشان يحفظ في الملف

                    context.getSource().sendSuccess(
                            () -> Component.literal("✅ Waves have been reset (memory + saved data)!"), false
                    );
                    return 1;
                })
        );


        // Clear all markers
        dispatcher.register(Commands.literal("resetmarks")
                .executes(context -> {
                    ServerLevel serverLevel = context.getSource().getLevel();
                    com.ghost.test.waves.MarkerData data = com.ghost.test.waves.MarkerData.get(serverLevel);
                    data.getAll().clear(); // نمسح الليست
                    data.setDirty();       // لازم نعلم إنها اتغيرت عشان تتحفظ

                    context.getSource().sendSuccess(
                            () -> Component.literal("✅ تم مسح كل الـ Block Markers!"), false
                    );
                    return 1;
                })
        );
    }
}
