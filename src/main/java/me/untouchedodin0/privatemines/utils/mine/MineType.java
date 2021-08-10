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

package me.untouchedodin0.privatemines.utils.mine;

import me.untouchedodin0.privatemines.PrivateMines;
import me.untouchedodin0.privatemines.utils.mine.loop.MineLoopUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import redempt.redlib.multiblock.MultiBlockStructure;
import redempt.redlib.multiblock.Structure;

import java.util.UUID;

public class MineType {

    private final String mineTypeName;
    private final MultiBlockStructure multiBlockStructure;
    private final PrivateMines privateMines;
    private final MineLoopUtil mineLoopUtil;

    private Structure structure;
    private final int[][] cornerLocations;
    private final int[] spawnLocation;
    private final int[] npcLocation;

    Mine mine;

    public MineType(String typeString, MultiBlockStructure multiStructure, PrivateMines privateMines) {
        this.mineTypeName = typeString;
        this.multiBlockStructure = multiStructure;
        this.mineLoopUtil = new MineLoopUtil();
        this.privateMines = privateMines;
        this.cornerLocations = mineLoopUtil.findCornerLocations(multiBlockStructure, Material.POWERED_RAIL);
        this.spawnLocation = mineLoopUtil.findLocation(multiBlockStructure, Material.CHEST);
        this.npcLocation = mineLoopUtil.findLocation(multiBlockStructure, Material.WOOL);
    }

    public MultiBlockStructure getMultiBlockStructure() {
        return this.multiBlockStructure;
    }

    public MineType getMineType() {
        return this;
    }

    public String getStructureName() {
        return multiBlockStructure.getName();
    }

    @SuppressWarnings("UnusedReturnValue")
    public Mine build(Location location, UUID owner) {
        long startTime = System.currentTimeMillis();
        this.structure = multiBlockStructure.build(location);
        mine = new Mine(structure, this);
        mine.setMineLocation(location);
        mine.setMineOwner(owner);
        mine.setSpawnLocation(mine.getRelative(spawnLocation));

//        mine.setSpawnLocation(mine.getRelative(mine.getSpawnLocation()));
//        mine.setNpcLocation(mine.getRelative(mine.getNPCLocationRelative()));

//        mine.setSpawnLocation(mine.getSpawnLocation());
//        mine.setNpcLocation(npcLocation);
//        Bukkit.broadcastMessage("cornerLocations: " + cornerLocations);
//        Bukkit.broadcastMessage("spawnLocation: " + spawnLocation);
//        Bukkit.broadcastMessage("npcLocation: " + npcLocation);
//        mine.setSpawnLocation(mine.getSpawnLocation());
//        mine.setNpcLocation(mine.getNpcLocation());
        Bukkit.broadcastMessage("spawnLocation: " + mine.getSpawnLocationRelative());
        Bukkit.broadcastMessage("spawnLocation 2: " + mine.getRelative(mine.getSpawnLocationRelative()));

//        Bukkit.broadcastMessage("npcLocation: " + mine.getNPCLocationRelative());
        multiBlockStructure.getAt(mine.getRelative(mine.getSpawnLocationRelative()));

        long endTime = System.currentTimeMillis();
        String timeString = String.valueOf(endTime - startTime);
        String loadingTime = "That took {} milliseconds to create the mine".replace("{}", timeString);
        Bukkit.getLogger().info(loadingTime);
//        Bukkit.getLogger().info("That took " + (endTime - startTime) + " milliseconds to create the mine");
        return mine;
    }

    @SuppressWarnings("unused")
    public String getMineTypeName() {
        return mineTypeName;
    }

    public PrivateMines getPrivateMines() {
        return privateMines;
    }

    @SuppressWarnings("unused")
    public MineLoopUtil getMineLoopUtil() {
        return mineLoopUtil;
    }

    public Structure getStructure() {
        return this.structure;
    }

    public int[][] getCornerLocations() {
        return cornerLocations;
    }

    public int[] getNpcLocation() {
        return npcLocation;
    }

    public int[] getSpawnLocation() {
        return spawnLocation;
    }
}



