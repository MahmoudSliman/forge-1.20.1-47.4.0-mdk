package com.ghost.test.waves;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.List;

public class MarkerData extends SavedData {
    private static final String ID = "wave_markers";
    private final List<BlockPos> positions = new ArrayList<>();

    public void add(BlockPos pos) {
        positions.add(pos);
        setDirty();
    }

    public List<BlockPos> getAll() {
        return positions;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag list = new ListTag();
        for (BlockPos pos : positions) {
            CompoundTag posTag = new CompoundTag();
            posTag.putInt("x", pos.getX());
            posTag.putInt("y", pos.getY());
            posTag.putInt("z", pos.getZ());
            list.add(posTag);
        }
        tag.put("positions", list);
        return tag;
    }

    public static MarkerData load(CompoundTag tag) {
        MarkerData data = new MarkerData();
        ListTag list = tag.getList("positions", Tag.TAG_COMPOUND);
        for (Tag t : list) {
            CompoundTag posTag = (CompoundTag) t;
            data.positions.add(new BlockPos(
                    posTag.getInt("x"),
                    posTag.getInt("y"),
                    posTag.getInt("z")
            ));
        }
        return data;
    }

    public static MarkerData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(MarkerData::load, MarkerData::new, ID);
    }
}
