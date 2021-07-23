package me.untouchedodin0.privatemines.utils.mine.loop;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class MineLoopUtil {

    World world;
    List<Location> cornerLocations = new ArrayList<>();
    Location spawnLocation;
    Location npcLocation;

    public List<Location> findCornerLocations(Location startLocation, Location endLocation, Material cornerMaterial) {
        List<Location> cornerLocation = new ArrayList<>();
        world = startLocation.getWorld();
        for (int x = startLocation.getBlockX(); x <= endLocation.getBlockX(); x++) {
            for (int y = startLocation.getBlockY(); y <= endLocation.getBlockY(); y++) {
                for (int z = startLocation.getBlockZ(); z <= endLocation.getBlockZ(); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType() == cornerMaterial) {
                        cornerLocation.add(block.getLocation());
                        block.setType(Material.AIR);
                    }
                }
            }
        }
        return cornerLocation;
    }

    public Location findSpawnPointLocation(Location startLocation, Location endLocation, Material spawnPointMaterial) {
        Location spawnLoc = null;
        for (int x = startLocation.getBlockX(); x <= endLocation.getBlockX(); x++) {
            for (int y = startLocation.getBlockY(); y <= endLocation.getBlockY(); y++) {
                for (int z = startLocation.getBlockZ(); z <= endLocation.getBlockZ(); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType() == spawnPointMaterial && spawnLoc == null) {
                        spawnLoc = block.getLocation();
                        block.setType(Material.AIR);
                    }
                }
            }
        }
        return spawnLoc;
    }

    public Location findNpcLocation(Location startLocation, Location endLocation, Material npcMaterial) {
        Location npcLoc = null;
        for (int x = startLocation.getBlockX(); x <= endLocation.getBlockX(); x++) {
            for (int y = startLocation.getBlockY(); y <= endLocation.getBlockY(); y++) {
                for (int z = startLocation.getBlockZ(); z <= endLocation.getBlockZ(); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType() == npcMaterial && npcLoc == null) {
                        npcLoc = block.getLocation();
                        block.setType(Material.AIR);
                    }
                }
            }
        }
        return npcLoc;
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

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public Location getNpcLocation() {
        return npcLocation;
    }
}
