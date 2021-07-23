package me.untouchedodin0.privatemines.utils.mine.loop;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class MineLoopUtil {

    //    private final List<Location> locations = new ArrayList<>();
    World world;
    List<Location> cornerLocations = new ArrayList<>();
    Location spawnLocation;
    Location npcLocation;

    public List<Location> findCornerLocations(Location startLocation, Location endLocation, Material cornerMaterial) {
        List<Location> cornerLocations = new ArrayList<>();

        world = startLocation.getWorld();
        for (int x = startLocation.getBlockX(); x <= endLocation.getBlockX(); x++) {
            for (int y = startLocation.getBlockY(); y <= endLocation.getBlockY(); y++) {
                for (int z = startLocation.getBlockZ(); z <= endLocation.getBlockZ(); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType() == cornerMaterial) {
                        cornerLocations.add(block.getLocation());
                    }
                }
            }
        }
        return cornerLocations;
    }

    public Location findSpawnPointLocation(Location startLocation, Location endLocation, Material spawnPointMaterial) {
        Location spawnLocation = null;
        for (int x = startLocation.getBlockX(); x <= endLocation.getBlockX(); x++) {
            for (int y = startLocation.getBlockY(); y <= endLocation.getBlockY(); y++) {
                for (int z = startLocation.getBlockZ(); z <= endLocation.getBlockZ(); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType() == spawnPointMaterial && spawnLocation == null) {
                        spawnLocation = block.getLocation();
                    }
                }
            }
        }
        return spawnLocation;
    }

    public Location findNpcLocation(Location startLocation, Location endLocation, Material npcMaterial) {
        Location npcLocation = null;
        for (int x = startLocation.getBlockX(); x <= endLocation.getBlockX(); x++) {
            for (int y = startLocation.getBlockY(); y <= endLocation.getBlockY(); y++) {
                for (int z = startLocation.getBlockZ(); z <= endLocation.getBlockZ(); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType() == npcMaterial && npcLocation == null) {
                        npcLocation = block.getLocation();
                    }
                }
            }
        }
        return npcLocation;
    }

    public void setBlockLocations(Location startLocation,
                                  Location endLocation,
                                  Material cornerMaterial,
                                  Material npcMaterial,
                                  Material spawnPointMaterial) {
        this.cornerLocations = findCornerLocations(startLocation, endLocation, cornerMaterial);
        this.npcLocation = findNpcLocation(startLocation, endLocation, npcMaterial);
        this.spawnLocation = findSpawnPointLocation(startLocation, endLocation, spawnPointMaterial);
    }

    public List<Location> getCornerLocations() {
        return cornerLocations;
    }

    public Location getCorner1() {
        return cornerLocations.get(0);
    }

    public Location getCorner2() {
        return cornerLocations.get(1);
    }

    public Location getNpcLocation() {
        return npcLocation;
    }
}
