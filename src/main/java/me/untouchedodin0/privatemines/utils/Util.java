package me.untouchedodin0.privatemines.utils;

import com.sk89q.worldedit.math.BlockVector3;
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

    public List<Location> findCornerBlocks(World world, Location start, Location finish) {
        List<Location> cornerLocations = new ArrayList<>();

        startFix = new Location(world,
                start.getBlockX() + 1,
                start.getBlockY(),
                start.getBlockZ());
        endFix = new Location(world,
                finish.getBlockX() + 1,
                finish.getBlockY(),
                finish.getBlockZ());

        for (int x = 0; x <= Math.abs(start.getX() - finish.getX()); x++) {
            for (int y = 0; y <= Math.abs(start.getY() - finish.getY()); y++) {
                for (int z = 0; z <= Math.abs(start.getZ() - finish.getZ()); z++) {
                    Block block = world.getBlockAt(
                            (Math.min(start.getBlockX(), finish.getBlockX())) + x,
                            (Math.min(start.getBlockY(), finish.getBlockY())) + y,
                            (Math.min(start.getBlockZ(), finish.getBlockZ())) + z
                    );

                    if (block.getType().isAir()) {
                        continue;
                    }
                    if (block.getType().equals(Material.POWERED_RAIL)) {
                        Bukkit.broadcastMessage("powered rail at " + block.getLocation());
                        cornerLocations.add(block.getLocation());
                    }

//                    cornerBlocks = util.findCornerBlocks(world, miningRegion);
//                    Bukkit.broadcastMessage("corner blocks: " + cornerBlocks);
                }
            }
        }
        return cornerLocations;
    }

    public String coordFormat(double nRaw) {
        String result = "";
        String n = (new StringBuilder(String.valueOf(nRaw))).toString();
        int decimals = 1;
        boolean atDecimal = false;
        int current = 0;
        for (int i = 0; i < n.length(); ) {
            char c = n.charAt(i);
            if (atDecimal)
                current++;
            if (c == '.')
                atDecimal = true;
            if (decimals >= current) {
                result = result + c;
                i++;
            }
            break;
        }
        return result;
    }
}
