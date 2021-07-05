package me.untouchedodin0.privatemines.factory;

import me.untouchedodin0.privatemines.utils.storage.MineStorage;
import me.untouchedodin0.privatemines.world.MineWorldManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import redempt.redlib.multiblock.MultiBlockStructure;
import redempt.redlib.region.CuboidRegion;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MineFactory {

    MineStorage mineStorage;
    InputStream inputStream;
    MultiBlockStructure multiBlockStructure;
    World world;
    CuboidRegion cuboidRegion;
    CuboidRegion miningRegion;
    Location to;
    Location start;
    Location end;
    Location corner1;
    Location corner2;
    Location spawnLocation;
    Location npcLocation;
    MineWorldManager mineWorldManager;

    List<Location> cornerBlocks = new ArrayList<>();

    public MineFactory(MineStorage storage,
                       MineWorldManager mineWorldManager) {
        this.mineStorage = storage;
        this.mineWorldManager = mineWorldManager;
    }

    public void createMine(Player player, Location location, InputStream stream) {
        this.inputStream = stream;
        if (mineStorage.hasMine(player)) {
            Bukkit.getLogger().info("Player was already in the storage, no need to give another mine!");
        } else if (inputStream != null) {
            player.sendMessage("Creating mine from storage");
            to = location;
            multiBlockStructure = MultiBlockStructure
                    .create(inputStream,
                            "mine",
                            false,
                            false);
            world = mineWorldManager.getMinesWorld();
            multiBlockStructure.build(to);
            cuboidRegion = multiBlockStructure.getRegion(to);
            start = cuboidRegion.getStart().clone();
            end = cuboidRegion.getEnd().clone();
            cornerBlocks = findCornerBlocks(start, end);
            spawnLocation = findSpawnLocation(start, end);
            npcLocation = findNPCLocation(start, end);

            miningRegion = new CuboidRegion(cornerBlocks.get(0), cornerBlocks.get(1))
                    .expand(1, 0, 1, 0, 1, 0);
        }
        player.teleport(spawnLocation);
        player.sendMessage(ChatColor.GREEN + "You've been given a mine!");
    }

    public List<Location> findCornerBlocks(Location start, Location end) {
        for (int x = start.getBlockX(); x <= end.getBlockX(); x++) {
            for (int y = start.getBlockY(); y <= end.getBlockY(); y++) {
                for (int z = start.getBlockZ(); z <= end.getBlockZ(); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType() == Material.POWERED_RAIL) {
                        if (corner1 == null) {
                            corner1 = block.getLocation();
                        } else if (corner2 == null) {
                            corner2 = block.getLocation();
                        }
                    }
                }
            }
        }
        cornerBlocks.add(corner1);
        cornerBlocks.add(corner2);
        return cornerBlocks;
    }

    public Location findSpawnLocation(Location start, Location end) {
        for (int x = start.getBlockX(); x <= end.getBlockX(); x++) {
            for (int y = start.getBlockY(); y <= end.getBlockY(); y++) {
                for (int z = start.getBlockZ(); z <= end.getBlockZ(); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType() == Material.POWERED_RAIL) {
                        spawnLocation = block.getLocation();
                    }
                }
            }
        }
        return spawnLocation;
    }

    public Location findNPCLocation(Location start, Location end) {
        for (int x = start.getBlockX(); x <= end.getBlockX(); x++) {
            for (int y = start.getBlockY(); y <= end.getBlockY(); y++) {
                for (int z = start.getBlockZ(); z <= end.getBlockZ(); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType() == Material.WHITE_WOOL) {
                        npcLocation = block.getLocation();
                    }
                }
            }
        }
        return npcLocation;
    }
}
