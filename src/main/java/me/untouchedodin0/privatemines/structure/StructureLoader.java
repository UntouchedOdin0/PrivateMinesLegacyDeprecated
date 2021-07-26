package me.untouchedodin0.privatemines.structure;

import me.untouchedodin0.privatemines.utils.mine.loop.MineLoopUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import redempt.redlib.multiblock.MultiBlockStructure;

public class StructureLoader {

    private int[][] cornerLocations;
    private int[] dimensions;

    private Location startLocation;
    private Location endLocation;
    private Location spawnLocation;
    private Location npcLocation;
    MineLoopUtil mineLoopUtil;
    MultiBlockStructure multiBlockStructure;

    public StructureLoader(MineLoopUtil mineLoopUtil) {
        this.mineLoopUtil = mineLoopUtil;
    }

    public void loadStructure(MultiBlockStructure multiBlockStructure, Material spawnMaterial, Material npcMaterial, Material cornerMaterial) {
        setDimensions(multiBlockStructure.getDimensions());

        setSpawnLocation(mineLoopUtil.findSpawnPointLocation(getStart(), getEnd(), spawnMaterial));
        setNpcLocation(mineLoopUtil.findNpcLocation(getStart(), getEnd(), npcMaterial));
        multiBlockStructure.getDimensions();
        setCornerLocations(mineLoopUtil.findCornerLocations(multiBlockStructure, cornerMaterial));
    }

    public int[] getDimensions() {
        return dimensions;
    }

    public void setDimensions(int[] dimensions) {
        this.dimensions = dimensions;
    }

    public Location getStart() {
        return startLocation;
    }

    public void setStart(Location startLocation) {
        this.startLocation = startLocation;
    }

    public Location getEnd() {
        return endLocation;
    }

    public void setEnd(Location endLocation) {
        this.endLocation = endLocation;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public Location getNpcLocation() {
        return npcLocation;
    }

    public void setNpcLocation(Location npcLocation) {
        this.npcLocation = npcLocation;
    }

    public int[][] getCornerLocations() {
        return cornerLocations;
    }

    public void setCornerLocations(int[][] cornerLocations) {
        this.cornerLocations = cornerLocations;
    }
}
