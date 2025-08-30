package com.ghost.test.waves;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class ModCommands {

    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(Commands.literal("resetwaves")
                .executes(context -> {
                    WaveItem.waveManager.resetWaves();
                    context.getSource().sendSuccess(() -> Component.literal("✅wave have reset!"), false);
                    return 1;
                })
        );

        dispatcher.register(Commands.literal("clearmarkers")
                .executes(context -> {
                    BlockMarkerItem.clearSavedPositions();
                    context.getSource().sendSuccess(() -> Component.literal("✅ تم مسح كل الـ Block Markers!"), false);
                    return 1;
                })
        );
    }
}
