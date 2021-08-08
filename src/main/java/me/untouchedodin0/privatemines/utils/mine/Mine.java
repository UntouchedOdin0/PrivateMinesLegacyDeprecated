package me.untouchedodin0.privatemines.utils.mine;

import com.cryptomorin.xseries.XMaterial;
import me.untouchedodin0.privatemines.utils.mine.loop.MineLoopUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import redempt.redlib.multiblock.Structure;

import java.util.UUID;

public class Mine {

    Location mineLocation;
    UUID mineOwner;
    MineType type;

    Structure structure;
    MineLoopUtil mineLoopUtil;

    private final Material cornerMaterial = XMaterial.POWERED_RAIL.parseMaterial();
    private final Material npcMaterial = XMaterial.WHITE_WOOL.parseMaterial();
    private final Material spawnMaterial = XMaterial.CHEST.parseMaterial();
    private final Material expandMaterial = XMaterial.SPONGE.parseMaterial();

    int[][] cornerLocations;
    int[] spawnLocation;
    int[] npcLocation;

    public Mine(MineType mineType) {
        this.mineLoopUtil = new MineLoopUtil();
        this.type = mineType;
        this.structure = type.getStructure();

        /*
        this.cornerLocations = mineLoopUtil.findCornerLocations(structure, cornerMaterial);
        this.spawnLocation = mineLoopUtil.findSpawnPointLocation(structure, spawnMaterial);
        this.npcLocation = mineLoopUtil.findNpcLocation(structure, npcMaterial);
         */
    }

    public void setMineLocation(Location mineLocation) {
        this.mineLocation = mineLocation;
    }

    public Location getMineLocation() {
        return this.mineLocation;
    }

    public void setSpawnLocation(int[] spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public int[] getSpawnLocation() {
        return spawnLocation;
    }

    public void setNpcLocation(int[] npcLocation) {
        this.npcLocation = npcLocation;
    }

    public int[] getNpcLocation() {
        return npcLocation;
    }

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
}
