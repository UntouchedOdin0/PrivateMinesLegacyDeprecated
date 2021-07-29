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

package me.untouchedodin0.privatemines.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import me.untouchedodin0.privatemines.PrivateMines;
import me.untouchedodin0.privatemines.factory.MineFactory;
import me.untouchedodin0.privatemines.guis.MainMenuGui;
import me.untouchedodin0.privatemines.utils.Util;
import me.untouchedodin0.privatemines.utils.filling.MineFillManager;
import me.untouchedodin0.privatemines.utils.mine.ExpandingMineUtil;
import me.untouchedodin0.privatemines.utils.mine.MineUpgradeUtil;
import me.untouchedodin0.privatemines.utils.queue.MineQueueSystem;
import me.untouchedodin0.privatemines.utils.storage.MineStorage;
import me.untouchedodin0.privatemines.world.MineWorldManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@CommandAlias("privatemines|privatemine|pm|pmine")
public class PrivateMinesCommand extends BaseCommand {

    private static final String UTIL_DIRECTORY = "plugins/PrivateMinesRewrite/util/";
    private static final String MINE_DIRECTORY = "plugins/PrivateMinesRewrite/mines/";
    private static final String SPAWN_LOCATION_STRING = "spawnLocation";
    private static final String WHITELISTED_PLAYERS_STRING = "whitelistedPlayers";
    private static final String BANNED_PLAYERS_STRING = "bannedPlayers";
    private static final String PRIORITY_PLAYERS_STRING = "priorityPlayers";
    private static final String COOWNER_STRING = "coowner";
    private static final String BLOCKS_STRING = "blocks";

    Util util;

    // currently using these two to get the mine corners, need to set and get elsewhere.
    Location corner1;
    Location corner2;

    // The next place location for a mine, likely can do better.
    Location teleportLocation;

    // The file stream for the mine file
    InputStream inputStream;

    // The mine blocks :)
    List<ItemStack> mineBlocks = new ArrayList<>();

    // This is what is used to fill the mines.
    MineFillManager fillManager;

    PrivateMines privateMines;
    MineStorage mineStorage;
    MineFactory mineFactory;
    MineUpgradeUtil mineUpgradeUtil;
    ExpandingMineUtil expandingMineUtil;

    File userFile;
    File locationsFile;
    YamlConfiguration mineConfig;
    YamlConfiguration locationConfig;
    String playerID;
    MainMenuGui mainMenuGui;
    MineWorldManager mineWorldManager;
    MineQueueSystem mineQueueSystem;
    int queueSlot;

    public PrivateMinesCommand(Util util,
                               MineFillManager fillManager,
                               PrivateMines privateMines,
                               MineStorage mineStorage,
                               MineFactory mineFactory) {
        this.util = util;
        this.fillManager = fillManager;
        this.privateMines = privateMines;
        this.mineStorage = mineStorage;
        this.mineFactory = mineFactory;
        this.mainMenuGui = new MainMenuGui(fillManager);
        this.mineWorldManager = new MineWorldManager();
        this.mineUpgradeUtil = new MineUpgradeUtil();
        this.expandingMineUtil = new ExpandingMineUtil();
        this.mineQueueSystem = new MineQueueSystem();
    }

    @Default
    @Description("Manage privatemines")
    public void main(Player p) {
        if (p.hasPermission("privatemines.owner")) {
            userFile = new File(MINE_DIRECTORY + p.getUniqueId() + ".yml");
            mineConfig = YamlConfiguration.loadConfiguration(userFile);
            this.teleportLocation = mineConfig.getLocation(SPAWN_LOCATION_STRING);

            /*
                Add gui menu with following items
                BED: Teleports to mine?
                ENDER_PEARL: resets the mine
             */

            mainMenuGui.openMainMenuGui(p);
        }
    }

    @Subcommand("give")
    @Description("Gives a privatemines to a player (only pastes at the moment)")
    @CommandPermission("privatemines.give")
    @CommandCompletion("@players")
    public void give(Player p) {
        File file = new File("plugins/PrivateMinesRewrite/schematics/structure.dat");
        userFile = new File(MINE_DIRECTORY + p.getUniqueId() + ".yml");
        locationsFile = new File(UTIL_DIRECTORY, "locations.yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
        locationConfig = YamlConfiguration.loadConfiguration(locationsFile);
        playerID = p.getUniqueId().toString();

        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (mineStorage.hasMine(p)) {
            p.sendMessage(ChatColor.RED + "You already have a mine!");
        } else {
            mineFactory.createMine(p, mineWorldManager.nextFreeLocation());
        }
    }

    @Subcommand("give")
    @Description("Gives a privatemines to a player (only pastes at the moment)")
    @CommandPermission("privatemines.give")
    @CommandCompletion("@players")
    public void give(Player p, OnlinePlayer target) {
        if (mineStorage.hasMine(target.player)) {
            target.player.sendMessage(ChatColor.RED + "An error occurred while giving you a mine" +
                    " please inform a operator!");
            Bukkit.getLogger().warning(target.player.getName() + " already has a mine, not able to give another one!");
        } else {
            mineFactory.createMine(target.player, mineWorldManager.nextFreeLocation());
        }
    }

    @Subcommand("delete")
    @Description("Deletes a mine")
    @CommandPermission("privatemines.delete")
    @CommandCompletion("@players")
    public void delete(Player p) {
        mineFactory.deleteMine(p);
    }

    /*
        Lets a player reset their mine
     */

    @Subcommand("reset")
    @Description("Resets your mine")
    @CommandPermission("privatemines.reset")
    public void reset(Player p) {
        if (p != null) {
            userFile = new File(MINE_DIRECTORY + p.getUniqueId() + ".yml");
            mineConfig = YamlConfiguration.loadConfiguration(userFile);
            corner1 = mineConfig.getLocation("Corner1");
            corner2 = mineConfig.getLocation("Corner2");
            mineBlocks = (List<ItemStack>) mineConfig.getList(BLOCKS_STRING);

            if (corner1 != null && corner2 != null && mineBlocks != null) {
                Bukkit.getScheduler().runTaskLater(privateMines, ()
                        -> fillManager.fillMineMultiple(corner1, corner2, mineBlocks), 20L);
            }
            p.sendMessage(ChatColor.GREEN + "Your mine has been reset!");
        }
    }

    /*
        Lets operators or administrators reset another players mine
     */

    @Subcommand("reset")
    @Description("Lets operators or administrators reset another players mine")
    @CommandPermission("privatemines.reset.other")
    @CommandCompletion("@players")
    public void reset(Player p, OnlinePlayer target) {
        userFile = new File(MINE_DIRECTORY + target.player.getUniqueId() + ".yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
        corner1 = mineConfig.getLocation("corner1");
        corner2 = mineConfig.getLocation("corner2");

        mineBlocks = (List<ItemStack>) mineConfig.getList(BLOCKS_STRING);

        if (corner1 != null && corner2 != null && mineBlocks != null) {
            Bukkit.getScheduler().runTaskLater(privateMines, ()
                    -> fillManager.fillMineMultiple(corner1, corner2, mineBlocks), 20L);
        }
        target.getPlayer().sendMessage(ChatColor.GREEN + "Your mine has been reset!");
    }

    /*
        Sets / Gets the tax for a mine
     */

    @Subcommand("tax")
    @Description("Let's players either check or set their mine tax.")
    @CommandPermission("privatemines.tax")
    public void tax(Player p, @Optional @Conditions("limits:min=0,max=100") Double taxPercentage) {
        if (taxPercentage == null) {
            p.sendMessage(ChatColor.YELLOW + "Your mine tax is {tax}!");
        } else {
            if (taxPercentage < 1 || taxPercentage > 100) {
                p.sendMessage(ChatColor.RED + "Invalid tax percentage! (1 -> 100)");
                return;
            }
            p.sendMessage(ChatColor.GREEN + "Setting your tax to " + taxPercentage + "%");
        }
    }

    public Location getTeleportLocation() {
        return teleportLocation;
    }

    @Subcommand("upgrade")
    @Description("Force upgrades a player mine")
    @CommandPermission("privatemines.upgrade")
    @CommandCompletion("@players")
    public void upgrade(Player p) {
        userFile = new File(MINE_DIRECTORY + p.getUniqueId() + ".yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
//        mineFactory.expandMine(p);
        expandingMineUtil.expandMine(p);
//        Location location = mineConfig.getLocation("placeLocation");
//
//        File file = new File("plugins/PrivateMinesRewrite/schematics/structure.dat");
//        p.sendMessage(ChatColor.GREEN + "Attempting to upgrade your mine. (no function here yet)");
//        mineUpgradeUtil.upgradeMine(p, file, location, util);
    }

    @Subcommand("whitelist")
    @Description("Whitelist a player at your mine")
    @CommandPermission("privatemine.whitelist")
    @CommandCompletion("@players")
    public void whitelist(Player player, OnlinePlayer target) {
        userFile = new File(MINE_DIRECTORY + target.player.getUniqueId() + ".yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
        List<String> whitelistedPlayers = mineConfig.getStringList(WHITELISTED_PLAYERS_STRING);

        if (whitelistedPlayers.contains(target.player.getUniqueId().toString())) {
            player.sendMessage(ChatColor.RED + "Player was already whitelisted!");
            return;
        } else {
            player.sendMessage("Whitelisting " + target.player.getName());
            whitelistedPlayers.add(target.player.getUniqueId().toString());
        }
        mineConfig.set(WHITELISTED_PLAYERS_STRING, whitelistedPlayers);
        try {
            mineConfig.save(userFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subcommand("unwhitelist")
    @Description("Removes a player from your whitelist")
    @CommandPermission("privatemine.whitelist")
    @CommandCompletion("@players")
    public void unwhitelist(Player player, OnlinePlayer target) {
        userFile = new File(MINE_DIRECTORY + target.player.getUniqueId() + ".yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
        List<String> whitelistedPlayers = mineConfig.getStringList(WHITELISTED_PLAYERS_STRING);

        if (!whitelistedPlayers.contains(target.player.getUniqueId().toString())) {
            player.sendMessage(ChatColor.RED + "Player wasn't whitelisted!");
        } else {
            player.sendMessage("Removing " + target.player.getName() + " from your whitelist!");
            whitelistedPlayers.remove(target.player.getUniqueId().toString());
        }
        mineConfig.set(WHITELISTED_PLAYERS_STRING, whitelistedPlayers);
        try {
            mineConfig.save(userFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subcommand("ban")
    @Description("Bans a player from your mine")
    @CommandPermission("privatemine.ban")
    @CommandCompletion("@players")
    public void ban(Player player, OnlinePlayer target) {
        userFile = new File(MINE_DIRECTORY + target.player.getUniqueId() + ".yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
        List<String> bannedPlayers = mineConfig.getStringList(BANNED_PLAYERS_STRING);

        if (bannedPlayers.contains(target.player.getUniqueId().toString())) {
            player.sendMessage(ChatColor.RED + "Player was already banned!");
            return;
        } else {
            player.sendMessage(ChatColor.GREEN + "Banning player " + target.player.getName());
            bannedPlayers.add(target.player.getUniqueId().toString());
        }
        mineConfig.set(BANNED_PLAYERS_STRING, bannedPlayers);
        try {
            mineConfig.save(userFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.sendMessage(ChatColor.GREEN + target.player.getName() + " has been banned from your mine!");
    }

    @Subcommand("unban")
    @Description("Unbans a player from your mine")
    @CommandPermission("privatemine.unban")
    @CommandCompletion("@players")
    public void unban(Player player, OnlinePlayer target) {
        userFile = new File(MINE_DIRECTORY + target.player.getUniqueId() + ".yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
        List<String> bannedPlayers = mineConfig.getStringList(BANNED_PLAYERS_STRING);

        if (!bannedPlayers.contains(target.player.getUniqueId().toString())) {
            player.sendMessage(ChatColor.RED + "Player wasn't banned!");
        } else {
            player.sendMessage(ChatColor.GREEN + "Unbanning player " + target.player.getName());
            bannedPlayers.remove(target.player.getUniqueId().toString());
        }
        mineConfig.set(BANNED_PLAYERS_STRING, bannedPlayers);
        try {
            mineConfig.save(userFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.sendMessage(ChatColor.GREEN + target.player.getName() + " has been unbanned from your mine!");
    }

    @Subcommand("priority")
    @Description("Toggles priority for a player")
    @CommandPermission("privatemine.priority")
    @CommandCompletion("@players")
    public void priority(Player player, OnlinePlayer target) {
        userFile = new File(MINE_DIRECTORY + target.player.getUniqueId() + ".yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
        List<String> priorityPlayers = mineConfig.getStringList(PRIORITY_PLAYERS_STRING);

        if (!priorityPlayers.contains(target.player.getUniqueId().toString())) {
            player.sendMessage(ChatColor.GREEN + "Adding " + target.player.getName() + " to the priority list!");
            priorityPlayers.add(target.player.getUniqueId().toString());
        } else {
            player.sendMessage(ChatColor.RED + "Removing " + target.player.getName() + " from the priority list!");
            priorityPlayers.remove(target.player.getUniqueId().toString());
        }
        mineConfig.set(PRIORITY_PLAYERS_STRING, priorityPlayers);
        try {
            mineConfig.save(userFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subcommand("coowner")
    @Description("Sets the co-owner for the mine")
    @CommandPermission("privatemine.setcoowner")
    @CommandCompletion("@players")
    public void coowner(Player player, OnlinePlayer target) {
        userFile = new File(MINE_DIRECTORY + target.player.getUniqueId() + ".yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
        mineConfig.set(COOWNER_STRING, target.player.getUniqueId());
        try {
            mineConfig.save(userFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}



