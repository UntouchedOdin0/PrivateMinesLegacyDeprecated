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

package me.untouchedodin0.privatemines.utils.filling;

import com.cryptomorin.xseries.XMaterial;
import me.byteful.lib.blockedit.BlockEditAPI;
import me.untouchedodin0.privatemines.PrivateMines;
import org.apache.commons.lang.math.IntRange;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import redempt.redlib.misc.WeightedRandom;
import redempt.redlib.region.CuboidRegion;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MineFillManager {

    CuboidRegion cuboidRegion;
    WeightedRandom<Material> weightedRandom = new WeightedRandom<>();
    File userFile;
    YamlConfiguration mineConfig;
    Location corner1;
    Location corner2;
    List<ItemStack> mineBlocks = new ArrayList<>();
    PrivateMines privateMines;
    String spawnLocation = "spawnLocation";
    String corner1String = "Corner1";
    String corner2String = "Corner2";
    String minesPath = "plugins/PrivateMinesRewrite/mines/";

    public MineFillManager(PrivateMines privateMines) {
        this.privateMines = privateMines;
    }

    public void fillMineMultiple(Location location1, Location location2, List<ItemStack> items) {
        cuboidRegion = new CuboidRegion(location1, location2)
                .expand(1, 0, 1, 0, 1, 0);
        for (ItemStack item : items) {
            weightedRandom.set(item.getType(), 1);
        }
        cuboidRegion.forEachBlock(block -> {
            MaterialData data = block.getState().getData();
            Material toSet = weightedRandom.roll();
            Material xMaterial = XMaterial.matchXMaterial(toSet).parseMaterial();
            if (xMaterial != null) {
                BlockEditAPI.setBlock(block, xMaterial, data, false);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public void fillPlayerMine(UUID uuid) {
        userFile = new File(minesPath + uuid + ".yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
        corner1 = mineConfig.getSerializable(corner1String, Location.class);
        corner2 = mineConfig.getSerializable(corner2String, Location.class);
        mineBlocks = (List<ItemStack>) mineConfig.getList("blocks");
        if (corner1 != null && corner2 != null && mineBlocks != null) {
            Bukkit.getScheduler().runTaskLater(privateMines, ()
                    -> {
                if (inMine(Bukkit.getPlayer(uuid))) {
                    Bukkit.getPlayer(uuid).teleport(mineConfig.getSerializable(spawnLocation, Location.class));
                }
                fillMineMultiple(corner1, corner2, mineBlocks);
            }, 20L);
        }
    }

    public boolean inMine(Player player) {
        userFile = new File(minesPath + player.getUniqueId() + ".yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
        corner1 = mineConfig.getSerializable(corner1String, Location.class);
        corner2 = mineConfig.getSerializable(corner2String, Location.class);
        double playerX = player.getEyeLocation().getX();
        double playerY = player.getEyeLocation().getY();
        double playerZ = player.getEyeLocation().getZ();
        return new IntRange(corner1.getX(), corner2.getX()).containsDouble(playerX)
                && new IntRange(corner1.getY(), corner2.getY()).containsDouble(playerY)
                && new IntRange(corner1.getZ(), corner2.getZ()).containsDouble(playerZ);
    }
}
