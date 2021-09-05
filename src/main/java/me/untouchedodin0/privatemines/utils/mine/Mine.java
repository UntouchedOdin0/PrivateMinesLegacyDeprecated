/*
 * MIT License
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
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.untouchedodin0.privatemines.utils.mine;

import com.cryptomorin.xseries.XMaterial;
import me.byteful.lib.blockedit.BlockEditAPI;
import me.byteful.lib.blockedit.data.BlockLocation;
import me.untouchedodin0.privatemines.utils.Util;
import me.untouchedodin0.privatemines.utils.mine.util.region.utils.WorldEditRegion;
import me.untouchedodin0.privatemines.utils.mine.util.region.utils.WorldEditVector;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.codemc.worldguardwrapper.region.IWrappedRegion;
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
    private Util util;

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
        this.util = new Util();
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

    public void setMineType(MineType mineType) {
        if (mineType == type) {
            Bukkit.getLogger().warning("Can't set minetype to the same type!");
        } else {
            this.type = mineType;
        }
    }

    public void createNPC(Player player, String name) {
        if (npcLocation != null) {
            String playerName = player.getName();
            String loggermessage = String.format("Creating a npc named %s for playing %s", name, playerName);
            Bukkit.getLogger().info(loggermessage);
        }
    }

    public void createWorldGuardRegions(Player player, Location corner1, Location corner2) {
        WorldEditVector vector1 = new WorldEditVector(corner1.getX(), corner1.getY(), corner1.getZ());
        WorldEditVector vector2 = new WorldEditVector(corner2.getX(), corner2.getY(), corner2.getZ());

        World world = corner1.getWorld();

        WorldEditRegion worldEditRegion = new WorldEditRegion(vector1, vector2, world);
        IWrappedRegion worldguardRegion = util.createMainWorldGuardRegion(player, worldEditRegion);
    }

    public void deleteMineStructure(Player player) {
        getStructure().getRegion().forEachBlock(block -> {
            BlockState state = block.getState();
            if (XMaterial.AIR.parseMaterial() != null) {
                BlockEditAPI.setBlock(block, XMaterial.AIR.parseMaterial(), state.getData(), false);
            }
        });
    }
}