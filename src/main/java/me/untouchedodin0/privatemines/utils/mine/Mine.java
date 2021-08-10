package me.untouchedodin0.privatemines.utils.mine;

import com.cryptomorin.xseries.XMaterial;
import me.untouchedodin0.privatemines.utils.mine.loop.MineLoopUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import redempt.redlib.misc.WeightedRandom;
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

    private WeightedRandom<Material> weightedRandom;

    int[][] cornerLocations;
    int[] spawnLocation;
    int[] npcLocation;

    public Mine(Structure structure, MineType mineType) {
        this.mineLoopUtil = new MineLoopUtil();
        this.structure = structure;
        this.type = mineType;

        this.spawnLocation = mineLoopUtil.findLocation(type.getMultiBlockStructure(), spawnMaterial);
        this.npcLocation = mineLoopUtil.findLocation(type.getMultiBlockStructure(), npcMaterial);
        this.cornerLocations = mineLoopUtil.findCornerLocations(type.getMultiBlockStructure(), cornerMaterial);
//
//        this.spawnLoc = getSpawnLocation();
//        this.npcLoc = getRelative(mineType.getNpcLocation());
    }

    public void setMineLocation(Location mineLocation) {
        this.mineLocation = mineLocation;
    }

    public Location getMineLocation() {
        return this.mineLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        spawnLocation.getBlock().setType(Material.AIR);
        this.spawnLoc = spawnLocation;
    }

    public Location getSpawnLocation() {
        return spawnLoc;
    }

    public void setWeightedRandomMaterials(WeightedRandom<Material> weightedRandom) {
        this.weightedRandom = weightedRandom;
    }

    public WeightedRandom<Material> getWeightedRandom() {
        return weightedRandom;
    }

    public void setNpcLocation(Location npcLocation) {
        this.npcLoc = npcLocation;
    }

    public Location getNpcLocation() {
        return npcLoc;
    }

    public int[] getNpcLoc() {
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

    public void teleportToMine(Player player) {
        if (spawnLocation != null) {
            player.teleport(spawnLoc);
        }
    }

    public void resetMine() {

    }
}
