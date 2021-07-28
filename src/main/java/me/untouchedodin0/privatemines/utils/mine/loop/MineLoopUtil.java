/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021 Kyle Hicks
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.untouchedodin0.privatemines.utils.mine.loop;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import redempt.redlib.multiblock.MultiBlockStructure;

import java.util.ArrayList;
import java.util.List;

public class MineLoopUtil {

    World world;
    List<Location> cornerLocations = new ArrayList<>();
    List<Location> expandCornerLocations = new ArrayList<>();
    Location spawnLocation;
    Location npcLocation;

    int[][] cornerLocations1;

    public int[][] findCornerLocations(MultiBlockStructure structure, Material cornerMaterial) {

        int[] dimensions = structure.getDimensions();
        int dimX = dimensions[0];
        int dimY = dimensions[1];
        int dimZ = dimensions[2];

        int[][] locations = new int[2][];
        int corners = 0;

        for (int x = 0; x < dimX; x++) {
            for (int y = 0; y < dimY; y++) {
                for (int z = 0; z < dimZ; z++) {
                    if (structure.getType(x, y, z) != cornerMaterial) {
                        continue;
                    }
                    locations[corners] = new int[] {x, y, z};
                    corners++;
                    if (corners >= 2) break;
                }
            }
        }
        return locations;
    }

    public List<Location> findCornerLocations(Location startLocation, Location endLocation, Material
            cornerMaterial) {
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

    public List<Location> findExpandCornerLocations(Location startLocation, Location endLocation, Material
            expandCornerMaterial) {
        List<Location> expandCorners = new ArrayList<>();
        world = startLocation.getWorld();
        for (int x = startLocation.getBlockX(); x <= endLocation.getBlockX(); x++) {
            for (int y = startLocation.getBlockY(); y <= endLocation.getBlockY(); y++) {
                for (int z = startLocation.getBlockZ(); z <= endLocation.getBlockZ(); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType() == expandCornerMaterial) {
                        expandCorners.add(block.getLocation());
                        block.setType(Material.AIR);
                    }
                }
            }
        }
        return expandCorners;
    }

    public Location findSpawnPointLocation(Location startLocation, Location endLocation, Material
            spawnPointMaterial) {
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
                                  Material expandMaterial,
                                  Material npcMaterial,
                                  Material spawnPointMaterial) {
        this.cornerLocations = findCornerLocations(startLocation, endLocation, cornerMaterial);
        this.expandCornerLocations = findExpandCornerLocations(startLocation, endLocation, expandMaterial);
        this.npcLocation = findNpcLocation(startLocation, endLocation, npcMaterial);
        this.spawnLocation = findSpawnPointLocation(startLocation, endLocation, spawnPointMaterial);
    }

    @SuppressWarnings("unused")
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
