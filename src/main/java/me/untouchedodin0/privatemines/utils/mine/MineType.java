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
import org.bukkit.Location;
import redempt.redlib.multiblock.MultiBlockStructure;
import redempt.redlib.region.CuboidRegion;

import java.util.UUID;

public class MineType {

    private String mineType;
    private final MultiBlockStructure structure;
    private MineType type;
    private CuboidRegion cuboidRegion;
    private int[][] cornerLocations;
    private int[] spawnLocation;
    private int[] npcLocation;
    private PrivateMines privateMines;

    private MineLoopUtil mineLoopUtil;
    Mine mine;
    String structureName;

    public MineType(String mineType, MultiBlockStructure multiBlockStructure, PrivateMines privateMines) {
        this.mineType = mineType;
        this.structure = multiBlockStructure;
        this.mineLoopUtil = new MineLoopUtil();
        this.privateMines = privateMines;
        this.structureName = structure.getName();
    }

    public MineType setType(MineType mineType) {
        type = mineType;
        return this;
    }

    //TODO Setup this cuboid region
    public MineType setCuboidRegion(CuboidRegion cube) {
        cuboidRegion = cube;
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public MineType setCornerLocations(int[][] locations) {
        cornerLocations = locations;
        return this;
    }

    public MineType setSpawnLocation(int[] spawnLoc) {
        spawnLocation = spawnLoc;
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public MineType setNpcLocation(int[] npcLoc) {
        npcLocation = npcLoc;
        return this;
    }

    public MultiBlockStructure getStructure() {
        return structure;
    }

    public MineType getMineType() {
        return type;
    }

    public int[][] getCornerLocations() {
        return cornerLocations;
    }

    public int[] getSpawnLocation() {
        return spawnLocation;
    }

    public int[] getNpcLocation() {
        return npcLocation;
    }

    public String getStructureName() {
        return structureName;
    }

    @SuppressWarnings("UnusedReturnValue")
    public Mine build(Location location, UUID owner) {
        mine = new Mine(type);
        mine.setMineLocation(location);
        mine.setMineOwner(owner);
        return mine;
    }
}

    /*
    public MultiBlockStructure getMultiBlockStructure() {
        return multiBlockStructure;
    }

    public void setMultiBlockStructure(MultiBlockStructure multiBlockStructure) {
        this.multiBlockStructure = multiBlockStructure;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getMineType() {
        return type;
    }

    public Map<Material, Double> getMaterials() {
        return materials;
    }

    public void setMaterials(Map<Material, Double> materials) {
        this.materials = materials;
    }

    public List<Location> getCornerLocations() {
        return cornerLocations;
    }

    public void setCornerLocations(List<Location> cornerLocations) {
        this.cornerLocations = cornerLocations;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }
     */


