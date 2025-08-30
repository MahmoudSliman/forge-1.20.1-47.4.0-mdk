package com.ghost.test.items;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class WaveItem extends Item {

    private static final WaveManager waveManager = new WaveManager();

    public WaveItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            BlockPos savedPos = BlockMarkerItem.getSavedPos(); // نجيب إحداثيات الماركر

            if (savedPos != null) {
                // يرسبن الزومبي عند المكان اللي حددته بالماركر
                waveManager.startNextWave(level, savedPos.above());
                player.sendSystemMessage(Component.literal(
                        "بدأ لاويف رقم " + waveManager.getCurrentWave() +
                                " عند X=" + savedPos.getX() + " Y=" + savedPos.getY() + " Z=" + savedPos.getZ()
                ));
            } else {
                // لو مفيش ماركر متسجل
                player.sendSystemMessage(Component.literal("⚠ لازم تحدد مكان الأول باستخدام Block Marker!"));
            }
        }
        return super.use(level, player, hand);
    }
}
