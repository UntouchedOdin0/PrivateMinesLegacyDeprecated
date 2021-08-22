package me.untouchedodin0.privatemines.utils.mine;

import me.byteful.lib.blockedit.BlockEditAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import redempt.redlib.misc.WeightedRandom;
import redempt.redlib.multiblock.Structure;
import redempt.redlib.region.CuboidRegion;

import java.util.UUID;

public class Mine {

    Location mineLocation;
    Location spawnLoc;
    Location npcLoc;
    CuboidRegion cuboidRegion;
    CuboidRegion structureCuboid;
    UUID mineOwner;
    MineType type;
    Structure structure;
    int[][] cornerLocations;
    int[] spawnLocation;
    int[] npcLocation;
    Location corner1;
    Location corner2;
    private WeightedRandom<Material> weightedRandom;

    public Mine(Structure structure, MineType mineType) {
        this.structure = structure;
        this.type = mineType;
        this.structureCuboid = structure.getRegion();
        this.spawnLocation = type.getSpawnLocation();
        this.npcLocation = type.getNpcLocation();
        this.cornerLocations = type.getCornerLocations();
        this.corner1 = getRelative(getCornerLocations()[0]);
        this.corner2 = getRelative(getCornerLocations()[1]);
        this.cuboidRegion = new CuboidRegion(corner1, corner2);
        this.cuboidRegion.expand(1, 0, 1, 0, 1, 0);
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

    public void setNpcLocation(Location npcLocation) {
        npcLocation.getBlock().setType(Material.AIR);
        this.npcLoc = npcLocation;
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

    public CuboidRegion getCuboidRegion() {
        return cuboidRegion;
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
        cuboidRegion.forEachBlock(block -> {
            BlockState state = block.getState();
            BlockEditAPI.setBlock(block, getWeightedRandom().roll(), state.getData(), false);
        });
    }

    public void upgradeMine() {

//        int current = type.getMineOrder();
//        Bukkit.broadcastMessage("current: " + current);
//        current++;
//        Bukkit.broadcastMessage("new current: " + current);
    }

    public void setMineType(MineType mineType) {
        if (this.type == mineType) {
            this.type = getType();
//            Bukkit.broadcastMessage("Can't set minetype to the same type!");
            throw new IllegalStateException("Can't set minetype to the same type!");
        }
        this.type = mineType;
    }

    public void createNPC(Player player, String name) {
        if (npcLocation != null) {
            String playerName = player.getName();
            String loggermessage = String.format("Creating a npc named %s for playing %s", name, playerName);
            Bukkit.getLogger().info(loggermessage);
        }
    }

//    public int getMineTier() {
//        return type.getMineOrder();
//    }
}
