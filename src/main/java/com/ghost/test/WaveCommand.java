package com.ghost.test;

import com.ghost.test.wave.WaveManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;

public class WaveCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, WaveManager waveManager) {
        dispatcher.register(
                Commands.literal("wave")
                        .then(Commands.literal("start")
                                .then(Commands.argument("waveNumber", IntegerArgumentType.integer(1))
                                        .executes(context -> {
                                            int waveNumber = IntegerArgumentType.getInteger(context, "waveNumber");
                                            ServerLevel level = context.getSource().getLevel();
                                            waveManager.startWave(level, waveNumber);
                                            context.getSource().sendSuccess(() ->
                                                    net.minecraft.network.chat.Component.literal("Wave " + waveNumber + " started!"), true);
                                            return 1;
                                        })
                                )
                        )
        );
    }
}
