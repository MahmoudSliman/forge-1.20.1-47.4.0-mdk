package com.ghost.test.items;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class BlockMarkerItem extends Item {


    private static BlockPos savedPos = null;

    public BlockMarkerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos(); // مكان البلوك اللي ضغط عليه

        if (!level.isClientSide && player != null) {
            savedPos = pos; // نحفظ الإحداثيات
            player.sendSystemMessage(Component.literal(
                    "تم تسجيل البلوك عند: X=" + pos.getX() + " Y=" + pos.getY() + " Z=" + pos.getZ()
            ));
        }

        return InteractionResult.SUCCESS;
    }

    public static BlockPos getSavedPos() {
        return savedPos;
    }
}
