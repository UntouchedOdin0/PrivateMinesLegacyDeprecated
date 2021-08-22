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
import me.untouchedodin0.privatemines.utils.mine.Mine;
import me.untouchedodin0.privatemines.utils.mine.MineType;
import me.untouchedodin0.privatemines.utils.mine.util.ExpandingMineUtil;
import me.untouchedodin0.privatemines.utils.mine.util.MineUpgradeUtil;
import me.untouchedodin0.privatemines.utils.storage.MineStorage;
import me.untouchedodin0.privatemines.world.MineWorldManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@CommandAlias("privatemines|privatemine|pm|pmine")
public class PrivateMinesCommand extends BaseCommand {

    private static final String MINE_DIRECTORY = "plugins/PrivateMinesRewrite/mines/";
    private static final String WHITELISTED_PLAYERS_STRING = "whitelistedPlayers";
    private static final String BANNED_PLAYERS_STRING = "bannedPlayers";
    private static final String PRIORITY_PLAYERS_STRING = "priorityPlayers";
    private static final String COOWNER_STRING = "coowner";

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
    int queueSlot;
    Map<String, MineType> mineTypeMap;
    Mine mine;

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
        this.mainMenuGui = new MainMenuGui(fillManager, mineStorage);
        this.mineWorldManager = new MineWorldManager();
        this.mineUpgradeUtil = new MineUpgradeUtil();
        this.expandingMineUtil = new ExpandingMineUtil();
    }

    @Default
    @Description("Manage privatemines")
    public void main(Player player) {
        if (player.hasPermission("privatemines.owner")) {

            if (!mineStorage.hasMine(player)) {
                player.sendMessage(ChatColor.RED + "You currently don't have a mine!");
                return;
            }
            userFile = new File(MINE_DIRECTORY + player.getUniqueId() + ".yml");
            mineConfig = YamlConfiguration.loadConfiguration(userFile);
            mainMenuGui.openMainMenuGui(player);
        }
    }

    @Subcommand("give")
    @Description("Gives a privatemine to a player")
    @CommandPermission("privatemines.give")
    @CommandCompletion("@players")
    public void give(Player p) {
        if (mineStorage.hasMine(p)) {
            p.sendMessage(ChatColor.RED + "You already have a mine!");
        } else {
            mineFactory.createMine(p);
        }
    }

    @Subcommand("give")
    @Description("Gives a privatemine to a player")
    @CommandPermission("privatemines.give")
    @CommandCompletion("@players")
    public void give(Player p, OnlinePlayer target) {
        if (mineStorage.hasMine(target.player)) {
            target.player.sendMessage(ChatColor.RED + "An error occurred while giving you a mine" +
                    " please inform a operator!");
            Bukkit.getLogger().warning(target.player.getName() + " already has a mine, not able to give another one!");
        } else {
            mineFactory.createMine(target.player);
        }
    }

    @Subcommand("give")
    @Description("Gives a privatemine to a player")
    @CommandPermission("privatemines.give")
    @CommandCompletion("@players")
    public void give(CommandSender commandSender, OnlinePlayer target) {
        if (mineStorage.hasMine(target.player)) {
            target.player.sendMessage(ChatColor.RED + "An error occurred while giving you a mine" +
                    " please inform a operator!");
            Bukkit.getLogger().warning(target.player.getName() + " already has a mine, not able to give another one!");
        } else {
            mineFactory.createMine(target.player);
            commandSender.sendMessage(ChatColor.GREEN + "Given " + target.player.getName() + " a private mine!");
        }
    }

    @Subcommand("delete")
    @Description("Deletes a mine")
    @CommandPermission("privatemines.delete")
    @CommandCompletion("@players")
    public void delete(Player p) {
        mineFactory.deleteMine(p);
    }

    @Subcommand("delete")
    @Description("Deletes a mine")
    @CommandPermission("privatemines.delete")
    @CommandCompletion("@players")
    public void delete(CommandSender commandSender, OnlinePlayer target) {
        if (mineStorage.hasMine(target.player)) {
            Bukkit.getLogger().warning(target.player.getName() + " doesn't have a mine, can't delete it!");
        } else {
            mineFactory.deleteMine(target.player);
            commandSender.sendMessage(ChatColor.GREEN + "Deleted " + target.player.getName() + "'s PrivateMine");
        }
    }

    /*
        Lets a player reset their mine
     */

    @Subcommand("reset")
    @Description("Resets your mine")
    @CommandPermission("privatemines.reset")
    public void reset(Player p) {
        if (p != null && mineStorage.getMines().containsKey(p.getUniqueId())) {
            this.mine = mineStorage.getMines().get(p.getUniqueId());
            mine.resetMine();
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
        Player targetPlayer = target.player;
        if (targetPlayer != null) {
            this.mine = mineStorage.getMines().get(targetPlayer.getUniqueId());
            mine.resetMine();
            targetPlayer.sendMessage(ChatColor.GREEN + "Your mine has been reset!");
        }
    }

    /*
        Sets / Gets the tax for a mine
     */

    @Subcommand("tax")
    @Description("Let's players either check or set their mine tax.")
    @CommandPermission("privatemines.tax")
    public void tax(Player p, @Optional @Conditions("limits:min=0,max=100") Double taxPercentage) {
        userFile = new File(MINE_DIRECTORY + p.getUniqueId() + ".yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
        int tax = mineConfig.getInt("tax");

        if (taxPercentage == null) {
            p.sendMessage(ChatColor.YELLOW + "Your mine tax is {tax}!".replace("{tax}", String.valueOf(tax)));
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

    @Subcommand("open")
    @Description("Opens your mine")
    @CommandPermission("privatemines.open")
    public void open(Player p) {
        userFile = new File(MINE_DIRECTORY + p.getUniqueId() + ".yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
        mineConfig.set("status", true);
        try {
            mineConfig.save(userFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subcommand("close")
    @Description("Closes your mine")
    @CommandPermission("privatemines.close")
    public void close(Player p) {
        userFile = new File(MINE_DIRECTORY + p.getUniqueId() + ".yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
        mineConfig.set("status", false);
        try {
            mineConfig.save(userFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subcommand("upgrade")
    @Description("Force upgrades a player mine")
    @CommandPermission("privatemines.upgrade")
    @CommandCompletion("@players")
    public void upgrade(Player p) {
        userFile = new File(MINE_DIRECTORY + p.getUniqueId() + ".yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
        expandingMineUtil.expandMine(p, 1);
    }

    @Subcommand("upgrade")
    @Description("Force upgrades a player mine")
    @CommandPermission("privatemines.upgrade")
    @CommandCompletion("@players")
    public void upgrade(Player p, OnlinePlayer target) {
        if (target == null) {
            p.sendMessage(ChatColor.RED + "The target was offline!");
        } else {
            userFile = new File(MINE_DIRECTORY + p.getUniqueId() + ".yml");
            mineConfig = YamlConfiguration.loadConfiguration(userFile);

            if (!userFile.exists()) {
                p.sendMessage(ChatColor.RED + target.getPlayer().getName() + " doesn't own a private mine!");
            } else {
                expandingMineUtil.expandMine(p, 1);
            }
        }
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
        } else if (target.getPlayer().getName().equals(player.getName())) {
            player.sendMessage(ChatColor.RED + "You can't ban yourself!");
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

    @Subcommand("dev/minetype/map")
    @Description("Dev command test")
    @CommandPermission("privatemine.minetype")
    public void mineType(Player player) {
        mineTypeMap = privateMines.getMineTypeMap();
        player.sendMessage("Mine type map: " + mineTypeMap);

        mineTypeMap.forEach((name, mineType) -> {
            player.sendMessage("Name: " + name);
            player.sendMessage("MineType: " + mineType);
        });
    }

    @Subcommand("dev/minetype/map/find")
    @Description("Dev command test")
    @CommandPermission("privatemine.minetype.type")
    public void findMineType(Player player, String name) {
        mineTypeMap = privateMines.getMineTypeMap();

        if (name == null) {
            player.sendMessage("You didn't specify a type.");
        } else {
            for (Map.Entry<String, MineType> entry : mineTypeMap.entrySet()) {
                if (entry.getValue().getMineTypeName().equals(name)) {
                    player.sendMessage("Found an entry: " + entry);
                } else {
                    player.sendMessage("Sorry, I couldn't find a entry for " + name);
                    return;
                }
            }
        }
    }
}



