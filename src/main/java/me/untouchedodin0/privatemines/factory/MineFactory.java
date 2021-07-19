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
import me.untouchedodin0.privatemines.utils.Util;
import me.untouchedodin0.privatemines.utils.filling.MineFillManager;
import me.untouchedodin0.privatemines.utils.mine.PrivateMineLocations;
import me.untouchedodin0.privatemines.utils.mine.PrivateMineResetUtil;
import me.untouchedodin0.privatemines.utils.mine.PrivateMineUtil;
import me.untouchedodin0.privatemines.utils.storage.MineStorage;
import me.untouchedodin0.privatemines.world.MineWorldManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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

    PrivateMines privateMines;
    MineStorage mineStorage;
    MultiBlockStructure multiBlockStructure;
    World world;
    CuboidRegion cuboidRegion;
    CuboidRegion miningRegion;
    Location start;
    Location end;
    Location corner1;
    Location corner2;
    Location spawnLocation;
    Location npcLocation;
    Location nextLocation;
    Location placeLocation;
    Block startBlock;
    Block endBlock;
    MineWorldManager mineWorldManager;
    MineFillManager fillManager;
    File userFile;
    File locationsFile;
    String playerID;
    YamlConfiguration mineConfig;
    YamlConfiguration locationConfig;

    List<ItemStack> mineBlocks = new ArrayList<>();
    List<Location> cornerBlocks = new ArrayList<>();
    List<UUID> whitelistedPlayers = new ArrayList<>();
    List<UUID> bannedPlayers = new ArrayList<>();
    List<UUID> priorityPlayers = new ArrayList<>();
    UUID coowner = null;

    PrivateMineUtil privateMineUtil;
    PrivateMineLocations privateMineLocations;
    PrivateMineResetUtil resetUtil;
    Util util;
    Structure currentStructure;
    Structure upgradeStructure;
    int mineSize = 0;

    public MineFactory(PrivateMines privateMines,
                       MineStorage storage,
                       MineWorldManager mineWorldManager,
                       MineFillManager fillManager,
                       PrivateMineResetUtil resetUtil,
                       Util util) {
        this.privateMines = privateMines;
        this.mineStorage = storage;
        this.mineWorldManager = mineWorldManager;
        this.fillManager = fillManager;
        this.resetUtil = resetUtil;
        this.util = util;
    }

    public void createMine(Player player, Location location) {

        File file = new File("plugins/PrivateMinesRewrite/schematics/structure.dat");
        userFile = new File(MINE_DIRECTORY + player.getUniqueId() + ".yml");
        locationsFile = new File(UTIL_DIRECTORY, "locations.yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
        locationConfig = YamlConfiguration.loadConfiguration(locationsFile);

        playerID = player.getUniqueId().toString();
        multiBlockStructure = util.getMultiBlockStructure();

        if (mineStorage.hasMine(player)) {
            Bukkit.getLogger().warning("Couldn't give mine, due to player already having a mine!");
            player.sendMessage(ChatColor.RED + "Er, you do know you already have a mine. Right?");
        } else {
            world = location.getWorld();
            cuboidRegion = multiBlockStructure.getRegion(location);
            start = cuboidRegion.getStart().clone();
            end = cuboidRegion.getEnd().clone();

            multiBlockStructure.build(location);

            for (int x = start.getBlockX(); x <= end.getBlockX(); x++) {
                for (int y = start.getBlockY(); y <= end.getBlockY(); y++) {
                    for (int z = start.getBlockZ(); z <= end.getBlockZ(); z++) {
                        Block block = world.getBlockAt(x, y, z);
                        if (block.getType() == Material.POWERED_RAIL) {
                            if (corner1 == null) {
                                corner1 = block.getLocation();
                            } else if (corner2 == null) {
                                corner2 = block.getLocation();
                            }
                        } else if (block.getType() == Material.CHEST && spawnLocation == null) {
                            spawnLocation = block.getLocation();
                            if (block.getState().getData() instanceof Directional) {
                                spawnLocation.setYaw(Util.getYaw((((Directional) block.getState().getData()).getFacing())));
                            }
                            spawnLocation.getBlock().setType(Material.AIR);
                        } else if (block.getType() == Material.WHITE_WOOL && npcLocation == null) {
                            npcLocation = block.getLocation();
                            npcLocation.getBlock().setType(Material.AIR);
                            //TODO Create a npc here!
                        } else if (block.getType() == Material.SPONGE && placeLocation == null) {
                            placeLocation = block.getLocation();
                            placeLocation.getBlock().setType(Material.AIR);
                        }
                    }
                }
            }
            miningRegion = new CuboidRegion(corner1, corner2)
                    .expand(1, 0, 1, 0, 1, 0);
            mineSize = miningRegion.getBlockDimensions()[0];
            cornerBlocks.add(corner1);
            cornerBlocks.add(corner2);
            if (mineBlocks.isEmpty()) {
                mineBlocks.add(new ItemStack(Material.STONE));
            }

            Location miningRegionStart = miningRegion.getStart();
            Location miningRegionEnd = miningRegion.getEnd();
            startBlock = miningRegionStart.getBlock();
            endBlock = miningRegionEnd.getBlock();
            privateMineUtil = new PrivateMineUtil(player, file, mineBlocks, whitelistedPlayers, bannedPlayers, priorityPlayers, coowner);
            privateMineLocations = new PrivateMineLocations(player, nextLocation, spawnLocation, npcLocation, corner1, corner2);

            mineConfig.set(CORNER_1_STRING, privateMineLocations.getCorner1());
            mineConfig.set(CORNER_2_STRING, privateMineLocations.getCorner2());
            mineConfig.set(SPAWN_LOCATION_STRING, privateMineLocations.getSpawnLocation());
            mineConfig.set(NPC_LOCATION_STRING, privateMineLocations.getNpcLocation());
            mineConfig.set(PLACE_LOCATION_STRING, privateMineLocations.getMineLocation());
            mineConfig.set(BLOCKS_STRING, privateMineUtil.getMineBlocks());
            mineConfig.set(MINE_SIZE, mineSize);
            mineConfig.set(WHITELISTED_PLAYERS, privateMineUtil.getWhitelistedPlayers());
            mineConfig.set(BANNED_PLAYERS, privateMineUtil.getBannedPlayers());
            mineConfig.set(PRIORITY_PLAYERS, privateMineUtil.getPriorityPlayers());
            mineConfig.set(CO_OWNER, privateMineUtil.getCoOwner());
            try {
                mineConfig.save(userFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            resetUtil.startResetTask(player.getUniqueId(), privateMines.getConfig().getInt("resetDelay"));
            cornerBlocks = new ArrayList<>();
            Messages.msg("recievedMine");
            player.teleport(spawnLocation);
            Messages.msg("teleportedToMine");
            npcLocation = null;
            corner1 = null;
            corner2 = null;
            start = null;
            end = null;
            mineSize = 0;
        }
    }

    //TODO Implement a working version of this.
    public void upgradeMine(Player player, File newMineFile, Location location) {
        player.sendMessage(newMineFile.toString());
        currentStructure = multiBlockStructure.assumeAt(location);
        if (currentStructure != null) {
            player.sendMessage("Found a structure: " + currentStructure);
            player.sendMessage("Structure Location: " + currentStructure.getLocation());
        }
    }
}


