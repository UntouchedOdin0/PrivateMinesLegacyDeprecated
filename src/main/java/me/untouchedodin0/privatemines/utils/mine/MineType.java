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

import org.bukkit.Location;
import org.bukkit.Material;
import redempt.redlib.multiblock.MultiBlockStructure;

import java.io.File;
import java.util.List;

public class MineType {

    private MultiBlockStructure structure;
    private File file;
    private String type;
    private Material material;
    private List<Location> cornerLocations;
    private Location spawnLocation;

    public MineType(String mineType) {
        this.type = mineType;
    }

    public MineType setStructure(MultiBlockStructure multiBlockStructure) {
        structure = multiBlockStructure;
        return this;
    }

    public MineType setFile(File mineFile) {
        file = mineFile;
        return this;
    }

    public MineType setType(String mineType) {
        type = mineType;
        return this;
    }

    public MineType setMaterial(Material mineMaterial) {
        material = mineMaterial;
        return this;
    }

    public MineType setCornerLocations(List<Location> locations) {
        cornerLocations = locations;
        return this;
    }

    public MineType setSpawnLocation(Location spawnLocation1) {
        spawnLocation = spawnLocation1;
        return this;
    }

    public MultiBlockStructure getStructure() {
        return structure;
    }

    public File getFile() {
        return file;
    }

    public String getMineType() {
        return type;
    }

    public Material getMaterial() {
        return material;
    }

    public List<Location> getCornerLocations() {
        return cornerLocations;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
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
}
