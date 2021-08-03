package me.untouchedodin0.privatemines.structure;

import me.untouchedodin0.privatemines.utils.mine.loop.MineLoopUtil;
import org.bukkit.Location;
import redempt.redlib.multiblock.MultiBlockStructure;
import redempt.redlib.multiblock.Structure;
import redempt.redlib.region.CuboidRegion;

public class StructureLoader {

    MineLoopUtil mineLoopUtil;
    MultiBlockStructure blockStructure;
    private int[][] cornerLocations;
    private int[] dimensions;
    private Location startLocation;
    private Location endLocation;
    private Location spawnLocation;
    private Location npcLocation;

    public StructureLoader(MineLoopUtil mineLoopUtil) {
        this.mineLoopUtil = mineLoopUtil;
    }

    public void loadStructure(MultiBlockStructure multiBlockStructure) {
        this.blockStructure = multiBlockStructure;
//        this.cuboidRegion = blockStructure.getRegion(getStart());
//        setSpawnLocation(mineLoopUtil.findSpawnPointLocation(cuboidRegion.getStart(), cuboidRegion.getEnd(), spawnMaterial));
//        setNpcLocation(mineLoopUtil.findNpcLocation(cuboidRegion.getStart(), cuboidRegion.getEnd(), npcMaterial));
//        setCornerLocations(mineLoopUtil.findCornerLocations(blockStructure, cornerMaterial));
    }
}
