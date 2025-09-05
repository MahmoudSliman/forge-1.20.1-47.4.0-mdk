package com.ghost.test.waves;

import com.ghost.test.waves.marks.BlockMarkerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class WaveItem extends Item {

    public static final WaveManager waveManager = new WaveManager();

    public WaveItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            if (waveManager.isWaveActive()) {
                player.sendSystemMessage(Component.literal("⚠ Wave is active now! Wait until it finishes."));
                return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
            }

            if (level instanceof ServerLevel serverLevel) {
                List<BlockPos> markers = com.ghost.test.waves.MarkerData.get(serverLevel).getAll();

                if (!markers.isEmpty()) {
                    waveManager.startNextWave(level, markers);
                    player.sendSystemMessage(Component.literal(
                            "Wave " + waveManager.getCurrentWave() + " Started!"
                    ));
                } else {
                    player.sendSystemMessage(Component.literal("⚠ At least one marker is required!"));
                }
            }
        }

        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }

}
