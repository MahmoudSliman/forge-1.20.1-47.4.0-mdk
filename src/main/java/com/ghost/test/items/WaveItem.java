package com.ghost.test.items;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class WaveItem extends Item {

    private static final WaveManager waveManager = new WaveManager();

    public WaveItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            List<BlockPos> markers = BlockMarkerItem.getSavedPositions();

            if (!markers.isEmpty()) {
                waveManager.startNextWave(level, markers);
                player.sendSystemMessage(Component.literal(
                        "بدأ لاويف رقم " + waveManager.getCurrentWave() +
                                " عند " + markers.size() + " ماركرز مسجلة."
                ));
            } else {
                player.sendSystemMessage(Component.literal("⚠ لازم تحدد على الأقل ماركر واحد!"));
            }
        }

        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }


}
