package me.untouchedodin0.privatemines.worldedit;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface WorldEditHook {

    void fill(WorldEditRegion region, List<ItemStack> blocks);

    default void fillMine(WorldEditRegion region, List<ItemStack> blocks) {
        fill(region, blocks);
    }
}
