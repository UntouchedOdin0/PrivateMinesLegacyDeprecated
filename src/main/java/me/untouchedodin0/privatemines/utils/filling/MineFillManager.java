package me.untouchedodin0.privatemines.utils.filling;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.misc.WeightedRandom;
import redempt.redlib.region.CuboidRegion;

import java.util.List;

public class MineFillManager {

    CuboidRegion cuboidRegion;
    WeightedRandom<Material> weightedRandom = new WeightedRandom<>();

    public void fillMine(Location location1,
                         Location location2,
                         ItemStack itemStack) {
        cuboidRegion = new CuboidRegion(location1, location2)
                .expand(1, 0, 1, 0, 1, 0);
        cuboidRegion.forEachBlock(block -> {
            Material toSet = itemStack.getType();
            block.setType(toSet);
        });
    }

    public void fillMineMultiple(Location location1, Location location2, List<ItemStack> items) {
        cuboidRegion = new CuboidRegion(location1, location2)
                .expand(1, 0, 1, 0, 1, 0);
        for (ItemStack item : items) {
            weightedRandom.set(item.getType(), 1);
        }
        cuboidRegion.forEachBlock(block -> {
            Material toSet = weightedRandom.roll();
            block.setType(toSet);
        });
    }
}
