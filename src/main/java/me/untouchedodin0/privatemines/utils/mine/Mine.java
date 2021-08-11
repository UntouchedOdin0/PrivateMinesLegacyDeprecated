package me.untouchedodin0.privatemines.utils.mine;

import com.cryptomorin.xseries.XMaterial;
import me.untouchedodin0.privatemines.utils.mine.loop.MineLoopUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import redempt.redlib.misc.WeightedRandom;
import redempt.redlib.multiblock.Structure;
import redempt.redlib.region.CuboidRegion;

import java.util.UUID;

public class Mine {

    private final Material cornerMaterial = XMaterial.POWERED_RAIL.parseMaterial();
    private final Material npcMaterial = XMaterial.WHITE_WOOL.parseMaterial();
    private final Material spawnMaterial = XMaterial.CHEST.parseMaterial();
    private final Material expandMaterial = XMaterial.SPONGE.parseMaterial();
    Location mineLocation;
    Location spawnLoc;
    Location npcLoc;
    Location[][] corners;
    CuboidRegion cuboidRegion;
    UUID mineOwner;
    MineType type;
    Structure structure;
    MineLoopUtil mineLoopUtil;
    int[][] cornerLocations;
    int[] spawnLocation;
    int[] npcLocation;
    Location corner1;
    Location corner2;
    private WeightedRandom<Material> weightedRandom;

    public Mine(Structure structure, MineType mineType) {
        this.mineLoopUtil = new MineLoopUtil();
        this.structure = structure;
        this.type = mineType;

        this.spawnLocation = mineLoopUtil.findLocation(type.getMultiBlockStructure(), spawnMaterial);
        this.npcLocation = mineLoopUtil.findLocation(type.getMultiBlockStructure(), npcMaterial);
        this.cornerLocations = mineLoopUtil.findCornerLocations(type.getMultiBlockStructure(), cornerMaterial);
        this.corner1 = getRelative(getCornerLocations()[0]);
        this.corner2 = getRelative(getCornerLocations()[1]);
        this.cuboidRegion = new CuboidRegion(corner1, corner2);
        this.cuboidRegion.expand(1, 0, 1, 0, 1, 0);

//        this.spawnLoc = getSpawnLocation();
//        this.npcLoc = getRelative(mineType.getNpcLocation());
    }

    public Location getMineLocation() {
        return this.mineLocation;
    }

    public void setMineLocation(Location mineLocation) {
        this.mineLocation = mineLocation;
    }

    public Location getSpawnLocation() {
        return spawnLoc;
    }

    public void setSpawnLocation(Location spawnLocation) {
        spawnLocation.getBlock().setType(Material.AIR);
        this.spawnLoc = spawnLocation;
    }

    public Location getNpcLocation() {
        return npcLoc;
    }

    public void setNpcLocation(Location npcLocation) {
        npcLocation.getBlock().setType(Material.AIR);
        this.npcLoc = npcLocation;
    }

    public void setNpcLocation(int[] npcLocation) {
        this.npcLocation = npcLocation;
    }

    public int[] getNpcLoc() {
        return spawnLocation;
    }

    public void setWeightedRandomMaterials(WeightedRandom<Material> weightedRandom) {
        this.weightedRandom = weightedRandom;
    }

    public WeightedRandom<Material> getWeightedRandom() {
        return weightedRandom;
    }

    public int[][] getCornerLocations() {
        return cornerLocations;
    }

    public Location[][] getCorners() {
        return corners;
    }

    public void setCorners(Location[][] corners) {
        this.corners = corners;
    }

    public UUID getMineOwner() {
        return mineOwner;
    }

    public void setMineOwner(UUID mineOwner) {
        this.mineOwner = mineOwner;
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
        cuboidRegion.forEachBlock(block -> block.setType(getWeightedRandom().roll()));
    }
}
