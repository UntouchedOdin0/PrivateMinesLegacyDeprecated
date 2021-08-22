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
import org.jetbrains.annotations.Nullable;
import redempt.redlib.multiblock.Structure;
import redempt.redlib.region.CuboidRegion;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class MineFactory {

    private static final String UTIL_DIRECTORY = "plugins/PrivateMinesRewrite/util/";
    private static final String MINE_DIRECTORY = "plugins/PrivateMinesRewrite/mines/";
    private static final String CORNER_1_STRING = "Corner1";
    private static final String CORNER_2_STRING = "Corner2";
    private static final String SPAWN_LOCATION_STRING = "spawnLocation";
    private static final String LOCATION_STRING = "mine.location";

    PrivateMines privateMines;
    MineStorage mineStorage;
    Structure old;

    CuboidRegion expandRegion;
    CuboidRegion expandRegionBedrock;

    Location start;
    Location end;
    Location nextLocation;
    MineWorldManager mineWorldManager;
    MineFillManager fillManager;
    Path userFilePath;
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
        if (!mineStorage.hasMine(player)) {
            Bukkit.getLogger().warning("Couldn't delete mine, due to player not owning mine!");
        } else {
            mine = mineStorage.getMine(player);
            old = mine.getStructure();
            userFile = new File(MINE_DIRECTORY + player.getUniqueId() + ".yml");
            userFilePath = userFile.toPath();
            if (userFile.exists() && old != null) {
                old.getRegion().forEachBlock(block -> block.setType(Material.AIR));
                boolean deleted = false;
                try {
                    deleted = Files.deleteIfExists(userFilePath);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                if (deleted) {
                    mineStorage.deleteMine(player.getUniqueId());
                    Bukkit.getLogger().info("Successfully deleted " + player.getName() + "'s Private Mine!");
                }
            }
        }
    }

    // Credit goes to Lars for helping with this

    public void upgradeMine(Player player) {

        if (!mineStorage.hasMine(player)) {
            return;
        }

        // get current mine from player
        Mine currentMine = mineStorage.getMine(player);
        Location currentLocation = currentMine.getMineLocation();
        UUID playerUUID = player.getUniqueId();

        // get next mineType
        MineType currentMineType = currentMine.getType();
        MineType nextMineType = getNextMineType(currentMineType);

        if (nextMineType == null) {
            Bukkit.getLogger().info("You're at the highest mine type already!");
        }

        // delete current mine
        util.setAirBlocks(currentMine.getCuboidRegion());

        // set the new mine type

        if (nextMineType != null) {
            currentMine.setMineType(nextMineType);
            // build mine with the next MineType
            nextMineType.build(currentLocation, playerUUID);
            currentMine.resetMine();
        }
    }


    // Credit goes to Larsk for helping with this

    @Nullable
    public MineType getNextMineType(MineType current) {
        Iterator<Map.Entry<String, MineType>> iterator = this.mineTypes.entrySet().iterator();

        MineType next = null;
        while (iterator.hasNext() && next != current) {
            next = iterator.next().getValue();
        }
        if (!iterator.hasNext()) {
            Bukkit.getLogger().info("Failed to find a mine type after the current, setting type back to "
                    + current.getMineTypeName());
            return current;
        }
        return iterator.next().getValue();
    }
}
