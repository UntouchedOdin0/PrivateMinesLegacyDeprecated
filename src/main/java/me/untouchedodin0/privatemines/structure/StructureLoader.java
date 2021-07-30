package me.untouchedodin0.privatemines.structure;

import me.untouchedodin0.privatemines.utils.mine.loop.MineLoopUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import redempt.redlib.multiblock.MultiBlockStructure;
import redempt.redlib.multiblock.Structure;
import redempt.redlib.region.CuboidRegion;

public class StructureLoader {

    private int[][] cornerLocations;
    private int[] dimensions;

    private Location startLocation;
    private Location endLocation;
    private Location spawnLocation;
    private Location npcLocation;
    MineLoopUtil mineLoopUtil;
    CuboidRegion cuboidRegion;
    MultiBlockStructure blockStructure;
    Structure structures;

    public StructureLoader(MineLoopUtil mineLoopUtil) {
        this.mineLoopUtil = mineLoopUtil;
    }

    public void loadStructure(MultiBlockStructure multiBlockStructure) {
        this.blockStructure = multiBlockStructure;
        this.dimensions = blockStructure.getDimensions();
//        this.cuboidRegion = blockStructure.getRegion(getStart());
//        setSpawnLocation(mineLoopUtil.findSpawnPointLocation(cuboidRegion.getStart(), cuboidRegion.getEnd(), spawnMaterial));
//        setNpcLocation(mineLoopUtil.findNpcLocation(cuboidRegion.getStart(), cuboidRegion.getEnd(), npcMaterial));
//        setCornerLocations(mineLoopUtil.findCornerLocations(blockStructure, cornerMaterial));
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

    public void setCornerLocations(int[][] cornerLocations) {
        this.cornerLocations = cornerLocations;
    }

    public int[][] getCornerLocations() {
        return cornerLocations;
    }

    public int[] getDimensions() {
        return dimensions;
    }

    public MultiBlockStructure getBlockStructure() {
        return blockStructure;
    }
}
