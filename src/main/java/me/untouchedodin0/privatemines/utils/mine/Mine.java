package me.untouchedodin0.privatemines.utils.mine;

import com.cryptomorin.xseries.XMaterial;
import me.untouchedodin0.privatemines.utils.mine.loop.MineLoopUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import redempt.redlib.multiblock.MultiBlockStructure;
import redempt.redlib.multiblock.Structure;

import java.util.UUID;

public class Mine {

    Location mineLocation;
    Location spawnLoc;
    Location npcLoc;

    UUID mineOwner;
    MineType type;

    MultiBlockStructure multiBlockStructure;
    Structure structure;
    MineLoopUtil mineLoopUtil;

    private final Material cornerMaterial = XMaterial.POWERED_RAIL.parseMaterial();
    private final Material npcMaterial = XMaterial.WHITE_WOOL.parseMaterial();
    private final Material spawnMaterial = XMaterial.CHEST.parseMaterial();
    private final Material expandMaterial = XMaterial.SPONGE.parseMaterial();

    int[][] cornerLocations;
    int[] spawnLocation;
    int[] npcLocation;

    public Mine(Structure structure, MineType mineType) {
        this.mineLoopUtil = new MineLoopUtil();
        this.structure = structure;
        this.type = mineType;

        this.spawnLocation = mineLoopUtil.findLocation(type.getMultiBlockStructure(), spawnMaterial);
        this.npcLocation = mineLoopUtil.findLocation(type.getMultiBlockStructure(), npcMaterial);

        this.spawnLoc = getRelative(mineType.getSpawnLocation());
        this.npcLoc = getRelative(mineType.getNpcLocation());

//        this.cornerLocations = mineLoopUtil.findCornerLocations(structure, cornerMaterial);
//        this.npcLocation = mineLoopUtil.findNpcLocation(structure, npcMaterial);
    }

    public void setMineLocation(Location mineLocation) {
        this.mineLocation = mineLocation;
    }

    public Location getMineLocation() {
        return this.mineLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLoc = spawnLocation;
    }

//    public Location getSpawnLocation() {
//        return spawnLoc;
//    }

    public void setNpcLocation(Location npcLocation) {
        this.npcLoc = npcLocation;
    }

    public Location getNpcLocation() {
        return npcLoc;
    }

    public int[] getSpawnLocation() {
        return spawnLocation;
    }

    public void setNpcLocation(int[] npcLocation) {
        this.npcLocation = npcLocation;
    }

//    public int[] getNpcLocation() {
//        return npcLocation;
//    }

    public void setCornerLocations(int[][] cornerLocations) {
        this.cornerLocations = cornerLocations;
    }

    public int[][] getCornerLocations() {
        return cornerLocations;
    }

    public void setMineOwner(UUID mineOwner) {
        this.mineOwner = mineOwner;
    }

    public UUID getMineOwner() {
        return mineOwner;
    }

    public Structure getStructure() {
        return structure;
    }

    public MineType getType() {
        return type;
    }

    public Location getRelative(int[] relative) {
        return structure
                .getRelative(relative[0], relative[1], relative[2])
                .getBlock()
                .getLocation();
    }

    public int[] getSpawnLocationRelative() {
        return spawnLocation;
    }

    public int[] getNPCLocationRelative() {
        return npcLocation;
    }
}
