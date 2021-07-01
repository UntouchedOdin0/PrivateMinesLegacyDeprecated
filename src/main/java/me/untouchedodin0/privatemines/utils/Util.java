package me.untouchedodin0.privatemines.utils;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.multiblock.MultiBlockStructure;
import redempt.redlib.region.CuboidRegion;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Util {

    MultiBlockStructure multiBlockStructure;
    Location start;
    Location startFix;
    Location end;
    Location endFix;
    Location corner1;
    Location corner2;
    ItemStack cornerMaterial = new ItemStack(Material.POWERED_RAIL);

    public void fillRegion(Location one, Location two, Material material) {
        Location toFill = one.add(two);
        toFill.getBlock().setType(material);
    }

    public MultiBlockStructure createStructure(InputStream inputStream) {
        if (inputStream != null) {
            multiBlockStructure = MultiBlockStructure
                    .create(inputStream,
                            "mine",
                            false,
                            false);
        }
        return multiBlockStructure;
    }

    public List<Location> findCornerBlocks(World world, CuboidRegion cuboidRegion) {
        List<Location> cornerLocations = new ArrayList<>();

        start = cuboidRegion.getStart();
        end = cuboidRegion.getEnd();

        startFix = new Location(world,
                start.getBlockX() + 1,
                start.getBlockY(),
                start.getBlockZ());
        endFix = new Location(world,
                end.getBlockX() + 1,
                end.getBlockY(),
                end.getBlockZ());

        for (int x = start.getBlockX(); x <= end.getBlockX(); x++) {
            for (int y = start.getBlockY(); y <= end.getBlockY(); y++) {
                for (int z = start.getBlockZ(); z <= end.getBlockZ(); z++) {
                    final Block blockAt = world.getBlockAt(x, y, z);
                    Material type = blockAt.getType();

                    if (type == Material.AIR || type.name().equals("LEGACY_AIR")) continue;
                    if (type == Material.POWERED_RAIL) {
                        if (corner1 == null) {
                            corner1 = blockAt.getLocation().clone();
                            Bukkit.broadcastMessage(ChatColor.YELLOW + "Start: "
                                    + corner1.getBlockX()
                                    + " "
                                    + corner1.getBlockY()
                                    + " "
                                    + corner1.getBlockZ());
                        }
                        if (corner2 == null) {
                            corner2 = blockAt.getLocation().clone();
                            Bukkit.broadcastMessage(ChatColor.YELLOW + "Finish: "
                                    + corner2.getBlockX()
                                    + " "
                                    + corner2.getBlockY()
                                    + " "
                                    + corner2.getBlockZ());
                        }
                    }
                }
            }
        }
        if (corner1 != null) {
            cornerLocations.add(corner1);
        } else if (corner2 != null) {
            cornerLocations.add(corner2);
        }
        return cornerLocations;
    }
}
