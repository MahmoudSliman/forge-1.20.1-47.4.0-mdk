package com.ghost.test.items;

import com.ghost.test.Test;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Items {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Test.MOD_ID);

    public static final RegistryObject<Item> RADAR = ITEMS.register("radar",
            () -> new WaveItem(new Item.Properties()));

    public static final RegistryObject<Item> BLOCK_MARKER = ITEMS.register("block_marker",
            () -> new BlockMarkerItem(new Item.Properties()));



    private static final WaveManager waveManager = new WaveManager();



    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
