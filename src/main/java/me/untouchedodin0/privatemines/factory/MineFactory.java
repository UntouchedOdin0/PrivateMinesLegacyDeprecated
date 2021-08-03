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

import com.cryptomorin.xseries.XMaterial;
import me.untouchedodin0.privatemines.PrivateMines;
import me.untouchedodin0.privatemines.structure.StructureLoader;
import me.untouchedodin0.privatemines.utils.Util;
import me.untouchedodin0.privatemines.utils.filling.MineFillManager;
import me.untouchedodin0.privatemines.utils.mine.PrivateMineLocations;
import me.untouchedodin0.privatemines.utils.mine.PrivateMineResetUtil;
import me.untouchedodin0.privatemines.utils.mine.PrivateMineUtil;
import me.untouchedodin0.privatemines.utils.mine.loop.MineLoopUtil;
import me.untouchedodin0.privatemines.utils.storage.MineStorage;
import me.untouchedodin0.privatemines.world.MineWorldManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.codemc.worldguardwrapper.WorldGuardWrapper;
import org.codemc.worldguardwrapper.region.IWrappedRegion;
import redempt.redlib.commandmanager.Messages;
import redempt.redlib.multiblock.MultiBlockStructure;
import redempt.redlib.multiblock.Structure;
import redempt.redlib.region.CuboidRegion;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MineFactory {

    private static final String UTIL_DIRECTORY = "plugins/PrivateMinesRewrite/util/";
    private static final String MINE_DIRECTORY = "plugins/PrivateMinesRewrite/mines/";
    private static final String CORNER_1_STRING = "Corner1";
    private static final String CORNER_2_STRING = "Corner2";
    private static final String SPAWN_LOCATION_STRING = "spawnLocation";
    private static final String NPC_LOCATION_STRING = "npcLocation";
    private static final String PLACE_LOCATION_STRING = "placeLocation";
    private static final String BLOCKS_STRING = "blocks";
    private static final String WHITELISTED_PLAYERS = "whitelistedPlayers";
    private static final String BANNED_PLAYERS = "bannedPlayers";
    private static final String PRIORITY_PLAYERS = "priorityPlayers";
    private static final String CO_OWNER = "coowner";
    private static final String MINE_SIZE = "mineSize";
    private static final String TAX = "tax";
    private static final String OPEN = "status";

    PrivateMines privateMines;
    MineStorage mineStorage;
    MultiBlockStructure multiBlockStructure;
    Structure old;

    World world;
    CuboidRegion cuboidRegion;
    CuboidRegion miningRegion;
    CuboidRegion expandRegion;
    CuboidRegion expandRegionBedrock;

    Location start;
    Location end;
    Location corner1;
    Location corner2;
    Location spawnLocation;
    Location npcLocation;
    Location nextLocation;
    Block startBlock;
    Block endBlock;
    MineWorldManager mineWorldManager;
    MineFillManager fillManager;
    File userFile;
    File locationsFile;
    String playerID;
    String mineRegionString;
    YamlConfiguration mineConfig;
    YamlConfiguration locationConfig;

    List<ItemStack> mineBlocks = new ArrayList<>();
    List<Location> cornerBlocks = new ArrayList<>();
    List<UUID> whitelistedPlayers = new ArrayList<>();
    List<UUID> bannedPlayers = new ArrayList<>();
    List<UUID> priorityPlayers = new ArrayList<>();
    UUID coowner = null;

    String fileName;
    File mineFile;

    PrivateMineUtil privateMineUtil;
    PrivateMineLocations privateMineLocations;
    PrivateMineResetUtil resetUtil;
    MineLoopUtil mineLoopUtil;
    Util util;
    StructureLoader structureLoader;
    int mineSize = 0;
    int tax = 5;

    Material cornerMaterial = XMaterial.POWERED_RAIL.parseMaterial();
    Material npcMaterial = XMaterial.WHITE_WOOL.parseMaterial();
    Material spawnMaterial = XMaterial.CHEST.parseMaterial();
    Material expandMaterial = XMaterial.SPONGE.parseMaterial();

    WorldGuardWrapper wgWrapper;
    IWrappedRegion iWrappedRegion;

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
    }

    public void createMine(Player player, Location location) {

        fileName = privateMines.getConfig().getString("structureFile");
        mineFile = new File("plugins/PrivateMinesRewrite/schematics/" + fileName + ".dat");
        userFile = new File(MINE_DIRECTORY + player.getUniqueId() + ".yml");
        locationsFile = new File(UTIL_DIRECTORY, "locations.yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
        locationConfig = YamlConfiguration.loadConfiguration(locationsFile);

        playerID = player.getUniqueId().toString();
        mineRegionString = "mine-" + playerID;
        multiBlockStructure = privateMines.getStructureLoader().getBlockStructure();

        if (mineStorage.hasMine(player)) {
            Bukkit.getLogger().warning("Couldn't give mine, due to player already having a mine!");
        } else {
            if (multiBlockStructure == null) {
                Bukkit.getLogger().info("The structure was null, likely meaning it couldn't find the file or" +
                        "there was a problem loading the structure! Feel free to make a ticket in the discord to" +
                        "get help.");
            }

            world = location.getWorld();
            cuboidRegion = multiBlockStructure.getRegion(location);
            start = cuboidRegion.getStart().clone();
            end = cuboidRegion.getEnd().clone();

            multiBlockStructure.build(location);
            mineLoopUtil.setBlockLocations(start, end, cornerMaterial, expandMaterial, npcMaterial, spawnMaterial);
            corner1 = mineLoopUtil.getCorner1();
            corner2 = mineLoopUtil.getCorner2();

            miningRegion = new CuboidRegion(corner1, corner2)
                    .expand(1, 0, 1, 0, 1, 0);
            mineSize = miningRegion.getBlockDimensions()[0];
            cornerBlocks.add(corner1);
            cornerBlocks.add(corner2);
            if (mineBlocks.isEmpty() && XMaterial.STONE.parseMaterial() != null) {
                mineBlocks.add(new ItemStack(XMaterial.STONE.parseMaterial()));
            }
        }

        Location miningRegionStart = miningRegion.getStart();
        Location miningRegionEnd = miningRegion.getEnd();
        startBlock = miningRegionStart.getBlock();
        endBlock = miningRegionEnd.getBlock();
        privateMineUtil = new PrivateMineUtil(player, mineFile, mineBlocks, whitelistedPlayers, bannedPlayers, priorityPlayers, coowner);
        privateMineLocations = new PrivateMineLocations(player, nextLocation, spawnLocation, npcLocation, corner1, corner2);
        wgWrapper = WorldGuardWrapper.getInstance();
        mineConfig.set(CORNER_1_STRING, mineLoopUtil.getCorner1());
        mineConfig.set(CORNER_2_STRING, mineLoopUtil.getCorner2());
        mineConfig.set(SPAWN_LOCATION_STRING, mineLoopUtil.getSpawnLocation());
        mineConfig.set(NPC_LOCATION_STRING, mineLoopUtil.getNpcLocation());
        mineConfig.set(PLACE_LOCATION_STRING, privateMineLocations.getMineLocation());
        mineConfig.set(BLOCKS_STRING, privateMineUtil.getMineBlocks());
        mineConfig.set(MINE_SIZE, mineSize);
        mineConfig.set(TAX, tax);
        mineConfig.set(OPEN, false);
        mineConfig.set(WHITELISTED_PLAYERS, privateMineUtil.getWhitelistedPlayers());
        mineConfig.set(BANNED_PLAYERS, privateMineUtil.getBannedPlayers());
        mineConfig.set(PRIORITY_PLAYERS, privateMineUtil.getPriorityPlayers());
        mineConfig.set(CO_OWNER, privateMineUtil.getCoOwner());
        try {
            mineConfig.save(userFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        iWrappedRegion = WorldGuardWrapper.getInstance().addCuboidRegion(
                "mine-" + player.getUniqueId().toString(),
                mineLoopUtil.getCorner1(),
                mineLoopUtil.getCorner2())
                .orElseThrow(() -> new RuntimeException("Could not create Main WorldGuard region"));
        util.setMainFlags(iWrappedRegion);

        resetUtil.startResetTask(player.getUniqueId(), privateMines.getConfig().getInt("resetDelay"));
        cornerBlocks = new ArrayList<>();
        Messages.msg("recievedMine");
        player.teleport(mineLoopUtil.getSpawnLocation());
        Messages.msg("teleportedToMine");
        npcLocation = null;
        corner1 = null;
        corner2 = null;
        start = null;
        end = null;
        mineSize = 0;
    }

    public void deleteMine(Player player) {
        userFile = new File(MINE_DIRECTORY + player.getUniqueId() + ".yml");
        locationsFile = new File(UTIL_DIRECTORY, "locations.yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
        locationConfig = YamlConfiguration.loadConfiguration(locationsFile);

        old = multiBlockStructure.assumeAt(mineConfig.getSerializable(SPAWN_LOCATION_STRING, Location.class));
        if (old != null) {
            old.getRegion().forEachBlock(block -> {
                block.setType(Material.AIR);
            });
        }
    }

    public void expandMine(Player player) {
        userFile = new File(MINE_DIRECTORY + player.getUniqueId() + ".yml");

        if (!userFile.exists()) {
            Bukkit.getLogger().info("Failed to upgrade " + player.getName() + "'s mine due to them not owning one");
            return;
        }
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
        start = mineConfig.getSerializable(CORNER_1_STRING, Location.class);
        start = mineConfig.getSerializable(CORNER_2_STRING, Location.class);
//        start = mineConfig.getLocation(CORNER_1_STRING);
//        end = mineConfig.getLocation(CORNER_2_STRING);
        expandRegion = new CuboidRegion(start, end);
        expandRegionBedrock = new CuboidRegion(start, end);

        expandRegion.expand(1, 1, 0, 0, 1, 1);
        expandRegionBedrock.expand(3, 2, 0, 2, 3, 2);

        expandRegion.stream().forEach(block -> {
            block.setType(Material.EMERALD_BLOCK);
        });

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


