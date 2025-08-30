package com.ghost.test.items;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class BlockMarkerItem extends Item {

    // نخزن أكثر من إحداثية
    private static final List<BlockPos> savedPositions = new ArrayList<>();

    public BlockMarkerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos(); // مكان البلوك اللي ضغط عليه

        if (!level.isClientSide && player != null) {
            savedPositions.add(pos); // نضيف الإحداثيات للقائمة
            player.sendSystemMessage(Component.literal(
                    "تم تسجيل الماركر عند: X=" + pos.getX() + " Y=" + pos.getY() + " Z=" + pos.getZ()
            ));
        }

        return InteractionResult.SUCCESS;
    }

    // نرجع كل الماركرز
    public static List<BlockPos> getSavedPositions() {
        return savedPositions;
    }

    // لو عايز تمسح كل الماركرز
    public static void clearSavedPositions() {
        savedPositions.clear();
    }
}
