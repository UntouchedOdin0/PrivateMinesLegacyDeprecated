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

package me.untouchedodin0.privatemines.factory;

import me.untouchedodin0.privatemines.PrivateMines;
import me.untouchedodin0.privatemines.structure.StructureLoader;
import me.untouchedodin0.privatemines.utils.Util;
import me.untouchedodin0.privatemines.utils.filling.MineFillManager;
import me.untouchedodin0.privatemines.utils.mine.Mine;
import me.untouchedodin0.privatemines.utils.mine.MineType;
import me.untouchedodin0.privatemines.utils.mine.loop.MineLoopUtil;
import me.untouchedodin0.privatemines.utils.mine.util.PrivateMineResetUtil;
import me.untouchedodin0.privatemines.utils.storage.MineStorage;
import me.untouchedodin0.privatemines.world.MineWorldManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import redempt.redlib.multiblock.MultiBlockStructure;
import redempt.redlib.multiblock.Structure;
import redempt.redlib.region.CuboidRegion;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class MineFactory {

    private static final String UTIL_DIRECTORY = "plugins/PrivateMinesRewrite/util/";
    private static final String MINE_DIRECTORY = "plugins/PrivateMinesRewrite/mines/";
    private static final String CORNER_1_STRING = "Corner1";
    private static final String CORNER_2_STRING = "Corner2";
    private static final String SPAWN_LOCATION_STRING = "spawnLocation";
    private static final String LOCATION_STRING = "mine.location";

    PrivateMines privateMines;
    MineStorage mineStorage;
    MultiBlockStructure multiBlockStructure;
    Structure old;

    CuboidRegion expandRegion;
    CuboidRegion expandRegionBedrock;

    Location start;
    Location end;
    Location nextLocation;
    MineWorldManager mineWorldManager;
    MineFillManager fillManager;
    File userFile;
    File locationsFile;
    String playerID;
    String mineRegionString;
    YamlConfiguration mineConfig;
    YamlConfiguration locationConfig;

    Map<String, MineType> mineTypes;

    String fileName;
    File mineFile;

    PrivateMineResetUtil resetUtil;
    MineLoopUtil mineLoopUtil;
    Util util;
    StructureLoader structureLoader;

    Mine mine;
    MineType mineType;

    public MineFactory(PrivateMines privateMines,
                       MineStorage storage,
                       MineFillManager fillManager,
                       PrivateMineResetUtil resetUtil,
                       MineLoopUtil mineLoopUtil,
                       Util util,
                       StructureLoader structureLoader) {
        this.privateMines = privateMines;
        this.mineStorage = storage;
        this.mineWorldManager = privateMines.getMineWorldManagerManager();
        this.fillManager = fillManager;
        this.resetUtil = resetUtil;
        this.mineLoopUtil = mineLoopUtil;
        this.util = util;
        this.structureLoader = structureLoader;
        this.mineTypes = privateMines.getMineTypeMap();
    }

    public void createMine(Player player) {

        fileName = privateMines.getConfig().getString("structureFile");
        mineFile = new File("plugins/PrivateMinesRewrite/structures/" + fileName + ".dat");
        userFile = new File(MINE_DIRECTORY + player.getUniqueId() + ".yml");
        locationsFile = new File(UTIL_DIRECTORY, "locations.yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
        locationConfig = YamlConfiguration.loadConfiguration(locationsFile);

        playerID = player.getUniqueId().toString();
        mineRegionString = "mine-" + playerID;
        mineType = mineTypes.get("structure");
        nextLocation = mineWorldManager.nextFreeLocation();

        if (mineStorage.hasMine(player)) {
            Bukkit.getLogger().warning("Couldn't give mine, due to player already having a mine!");
        } else {
            if (nextLocation == null) {
                nextLocation = mineWorldManager.nextFreeLocation();
            }

            mine = mineType.build(nextLocation, player.getUniqueId());
            mine.teleportToMine(player);
            mine.resetMine();
            Bukkit.broadcastMessage("" + nextLocation);
            mineStorage.addMine(player.getUniqueId(), mine);
            mineConfig.set("mine.owner", mine.getMineOwner().toString());
            mineConfig.set(LOCATION_STRING, mine.getMineLocation());
            mineConfig.set(SPAWN_LOCATION_STRING, mine.getSpawnLocation());
            try {
                mineConfig.save(userFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteMine(Player player) {
        userFile = new File(MINE_DIRECTORY + player.getUniqueId() + ".yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);

        old = multiBlockStructure.assumeAt(mineConfig.getSerializable(LOCATION_STRING, Location.class));
        if (old != null) {
            old.getRegion().forEachBlock(block -> block.setType(Material.AIR));
        }
    }

    public void expandMine(Player player) {
        userFile = new File(MINE_DIRECTORY + player.getUniqueId() + ".yml");

        if (!userFile.exists()) {
            privateMines.getLogger().warning("Failed to upgrade " + player.getName() + "'s mine due to them not owning one");
            return;
        }
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
        start = mineConfig.getSerializable(CORNER_1_STRING, Location.class);
        end = mineConfig.getSerializable(CORNER_2_STRING, Location.class);

        expandRegion = new CuboidRegion(start, end);
        expandRegionBedrock = new CuboidRegion(start, end);

        expandRegion.expand(1, 1, 0, 0, 1, 1);
        expandRegionBedrock.expand(3, 2, 0, 2, 3, 2);

        expandRegion.stream().forEach(block -> block.setType(Material.EMERALD_BLOCK));

        expandRegionBedrock.stream().forEach(block -> {
            if (block.isEmpty()) {
                block.setType(Material.REDSTONE_BLOCK);
            }
        });

        mineConfig.set(CORNER_1_STRING, expandRegion.getStart());
        mineConfig.set(CORNER_2_STRING, expandRegion.getEnd());
        try {
            mineConfig.save(userFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

