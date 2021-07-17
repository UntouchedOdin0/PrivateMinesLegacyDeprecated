package me.untouchedodin0.privatemines.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import dev.triumphteam.gui.guis.Gui;
import me.untouchedodin0.privatemines.PrivateMines;
import me.untouchedodin0.privatemines.factory.MineFactory;
import me.untouchedodin0.privatemines.guis.MainMenuGui;
import me.untouchedodin0.privatemines.utils.Util;
import me.untouchedodin0.privatemines.utils.filling.MineFillManager;
import me.untouchedodin0.privatemines.utils.queue.MineQueueSystem;
import me.untouchedodin0.privatemines.utils.storage.MineStorage;
import me.untouchedodin0.privatemines.world.MineWorldManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.blockdata.BlockDataManager;
import redempt.redlib.blockdata.DataBlock;
import redempt.redlib.misc.WeightedRandom;
import redempt.redlib.multiblock.MultiBlockStructure;
import redempt.redlib.multiblock.Rotator;
import redempt.redlib.multiblock.Structure;
import redempt.redlib.region.CuboidRegion;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@CommandAlias("privatemines|privatemine|pm|pmine")
public class PrivateMinesCommand extends BaseCommand {

    private static final String UTIL_DIRECTORY = "plugins/PrivateMinesRewrite/util/";
    private static final String MINE_DIRECTORY = "plugins/PrivateMinesRewrite/mines/";
    private static final String CORNER_1_STRING = "Corner1";
    private static final String CORNER_2_STRING = "Corner2";
    private static final String SPAWN_LOCATION_STRING = "spawnLocation";
    private static final String NPC_LOCATION_STRING = "npcLocation";
    private static final String PLACE_LOCATION_STRING = "placeLocation";
    private static final String BLOCKS_STRING = "blocks";

    Util util;
    Location to;
    Location position1;
    Location position2;
    Location start;
    Location startFix;
    Location end;
    Location endFix;
    Location corner1;
    Location corner2;
    Location spawnLocation;
    Location npcLocation;
    Location placeLocation;
    Location nextLocation;
    Location teleportLocation;
    World world;
    Structure structure;
    MultiBlockStructure multiBlockStructure;
    Rotator rotator;
    InputStream inputStream;
    CuboidRegion cuboidRegion;
    CuboidRegion miningRegion;
    ItemStack cornerMaterial = new ItemStack(Material.POWERED_RAIL);
    List<ItemStack> mineBlocks = new ArrayList<>();
    List<Location> cornerBlocks = new ArrayList<>();
    WeightedRandom<Material> weightedRandom = new WeightedRandom<>();
    MineFillManager fillManager;
    PrivateMines privateMines;
    BlockDataManager manager;
    MineStorage mineStorage;
    MineFactory mineFactory;
    File userFile;
    File locationsFile;
    YamlConfiguration mineConfig;
    YamlConfiguration locationConfig;
    String coords1 = "";
    String coords2 = "";
    String x1 = "";
    String y1 = "";
    String z1 = "";
    String x2 = "";
    String y2 = "";
    String z2 = "";
    String playerID;
    Block startBlock;
    Block endBlock;
    DataBlock startDataBlock;
    DataBlock endDataBlock;
    Gui gui;
    MainMenuGui mainMenuGui;
    MineWorldManager mineWorldManager;
    MineQueueSystem mineQueueSystem;
    int queueSlot;

    public PrivateMinesCommand(Util util,
                               MineFillManager fillManager,
                               PrivateMines privateMines,
                               MineStorage mineStorage,
                               MineFactory mineFactory,
                               MineWorldManager mineWorldManager) {
        this.util = util;
        this.fillManager = fillManager;
        this.privateMines = privateMines;
        this.mineStorage = mineStorage;
        this.mineFactory = mineFactory;
        this.mainMenuGui = new MainMenuGui(fillManager);
        this.mineWorldManager = new MineWorldManager();
        this.mineQueueSystem = new MineQueueSystem();
    }

    @Default
    @Description("Manage privatemines")
    public void main(Player p) {
        if (p.hasPermission("privatemines.owner")) {
            p.sendMessage("opening gui.");

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

    @Subcommand("setpos1")
    @Description("Sets the first position")
    public void setpos1(Player player) {
        if (position1 == null) {
            position1 = player.getLocation();
        }
    }

    @Subcommand("setpos2")
    @Description("Sets the second position")
    public void setpos2(Player player) {
        if (position2 == null) {
            position2 = player.getLocation();
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
        placeLocation = mineWorldManager.nextFreeLocation();

        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (mineStorage.hasMine(p)) {
            p.sendMessage(ChatColor.RED + "You already have a mine!");
        } else {
            mineFactory.createMine(p, placeLocation);
//
//            if (mineQueueSystem.queueContainsPlayer(p)) {
//                p.sendMessage("You're already in the queue #"
//                        + mineQueueSystem.getQueueSlot(p.getUniqueId()));
//            } else {
////                mineQueueSystem.queueMineCreation(p);
////                p.sendMessage("Queue Size: " + mineQueueSystem.getQueueSize());
//            }
        }
    }

    @Subcommand("give")
    @Description("Gives a privatemines to a player (only pastes at the moment)")
    @CommandPermission("privatemines.give")
    @CommandCompletion("@players")
    public void give(Player p, OnlinePlayer target) {
        placeLocation = mineWorldManager.nextFreeLocation();

        if (mineStorage.hasMine(target.player)) {
            target.player.sendMessage(ChatColor.RED + "An error occurred while giving you a mine" +
                    " please inform a operator!");
            Bukkit.getLogger().warning(target.player.getName() + " already has a mine, not able to give another one!");
        } else {
            mineFactory.createMine(target.player, placeLocation);
        }


//        if (mineStorage.hasMine(p)) {
//            p.sendMessage(ChatColor.RED + "Er, you do know you already have a mine. Right?");
//        } else if (inputStream != null) {
//            multiBlockStructure = MultiBlockStructure
//                    .create(inputStream,
//                            "mine",
//                            false,
//                            false);
//        }
//        world = placeLocation.getWorld();
//        cuboidRegion = multiBlockStructure.getRegion(placeLocation);
//        start = cuboidRegion.getStart().clone();
//        end = cuboidRegion.getEnd().clone();
//
//        multiBlockStructure.build(placeLocation);
//
//        if (start == null || end == null) {
//            Bukkit.getLogger().info("Failed to create the mine due to either");
//            Bukkit.getLogger().info("the start of the end being null");
//            Bukkit.broadcastMessage("The main cause of this is because the location is");
//            Bukkit.broadcastMessage("either to high or to low.");
//        } else {
//            for (int x = start.getBlockX(); x <= end.getBlockX(); x++) {
//                for (int y = start.getBlockY(); y <= end.getBlockY(); y++) {
//                    for (int z = start.getBlockZ(); z <= end.getBlockZ(); z++) {
//                        Block block = world.getBlockAt(x, y, z);
//                        if (block.getType() == Material.POWERED_RAIL) {
//                            if (corner1 == null) {
//                                corner1 = block.getLocation();
//                            } else if (corner2 == null) {
//                                corner2 = block.getLocation();
//                            }
//                        } else if (block.getType() == Material.CHEST && spawnLocation == null) {
//                            spawnLocation = block.getLocation();
//                            if (block.getState().getData() instanceof Directional) {
//                                spawnLocation.setYaw(Util.getYaw((((Directional) block.getState().getData()).getFacing())));
//                            }
//                            spawnLocation.getBlock().setType(Material.AIR);
//                        } else if (block.getType() == Material.WHITE_WOOL && npcLocation == null) {
//                            npcLocation = block.getLocation();
//                            npcLocation.getBlock().setType(Material.OAK_SIGN);
//                        } else if (block.getType() == Material.SPONGE && placeLocation == null) {
//                            placeLocation = block.getLocation();
//                            placeLocation.getBlock().setType(Material.AIR);
//                            Bukkit.broadcastMessage("placeLocation after setair: " + placeLocation);
//                            Bukkit.broadcastMessage("nextLocation after add: " + nextLocation);
//                            Bukkit.broadcastMessage("placeLocation = next: " + placeLocation);
//                        }
//                    }
//                }
//            }
//            miningRegion = new CuboidRegion(corner1, corner2)
//                    .expand(1, 0, 1, 0, 1, 0);
//            cornerBlocks.add(corner1);
//            cornerBlocks.add(corner2);
//            if (mineBlocks.isEmpty()) {
//                mineBlocks.add(new ItemStack(Material.STONE));
//            }
//            Sign s = (Sign) world.getBlockAt(npcLocation).getState();
//            s.setLine(0, "I'm an NPC");
//            s.setLine(1, "I should be fixed.");
//            s.update();
//            Location miningRegionStart = miningRegion.getStart();
//            Location miningRegionEnd = miningRegion.getEnd();
//            // Bukkit.broadcastMessage("corner blocks: " + cornerBlocks);
//            startBlock = miningRegionStart.getBlock();
//            endBlock = miningRegionEnd.getBlock();
//            Bukkit.getLogger().info("Creating the event...");
//            privateMine = new PrivateMine(
//                    p,
//                    file,
//                    placeLocation,
//                    spawnLocation,
//                    npcLocation,
//                    corner1,
//                    corner2);
//            Bukkit.getLogger().info("Calling the event...");
//            Bukkit.getPluginManager().callEvent(privateMine);
//            Bukkit.getLogger().info("Event Details:");
//            Bukkit.getLogger().info(privateMine.getEventName());
//            Bukkit.broadcastMessage("event details: " + privateMine);
//            mineConfig.set(CORNER_1_STRING, corner1);
//            mineConfig.set(CORNER_2_STRING, corner2);
//            mineConfig.set(SPAWN_LOCATION_STRING, spawnLocation);
//            mineConfig.set(NPC_LOCATION_STRING, npcLocation);
//            mineConfig.set(PLACE_LOCATION_STRING, placeLocation);
//            mineConfig.set(BLOCKS_STRING, mineBlocks);
//            try {
//                mineConfig.save(userFile);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            if (mineBlocks.toArray().length >= 2) {
//                Task.syncRepeating(() -> fillManager.fillPlayerMine(p), 0L, 20L);
//            } else {
//                Task.syncRepeating(() -> fillManager.fillPlayerMine(p), 0L, 2 * 20 * 60L);
//                fillManager.fillMine(corner1, corner2, mineBlocks.get(0));
//            }
//            Bukkit.broadcastMessage("cornerBlocks debug: ");
//            Bukkit.broadcastMessage("size: " + cornerBlocks.size());
//            cornerBlocks = new ArrayList<>();
//            p.teleport(spawnLocation);
//            p.sendMessage(ChatColor.GREEN + "You've been teleported to your mine!");
//            npcLocation = null;
//            corner1 = null;
//            corner2 = null;
//            placeLocation = null;
//        }
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
        File file = new File("plugins/PrivateMinesRewrite/schematics/structure.dat");
        p.sendMessage(ChatColor.GREEN + "Attempting to upgrade your mine. (no function here yet)");
        mineFactory.upgradeMine(p, file, p.getLocation());
    }

    @Subcommand("whitelist")
    @Description("Whitelist a player at your mine")
    @CommandPermission("privatemine.whitelist")
    @CommandCompletion("@players")
    public void whitelist(Player player, OnlinePlayer target) {
        userFile = new File(MINE_DIRECTORY + target.player.getUniqueId() + ".yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
        List<String> whitelistedPlayers = mineConfig.getStringList("whitelistedPlayers");

        if (whitelistedPlayers.contains(target.player.getUniqueId().toString())) {
            player.sendMessage(ChatColor.RED + "Player was already whitelisted!");
        } else {
            player.sendMessage("Whitelisting " + target.player.getName());
            whitelistedPlayers.add(target.player.getUniqueId().toString());
        }
        mineConfig.set("whitelistedPlayers", whitelistedPlayers);
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
        List<String> whitelistedPlayers = mineConfig.getStringList("whitelistedPlayers");

        if (!whitelistedPlayers.contains(target.player.getUniqueId().toString())) {
            player.sendMessage(ChatColor.RED + "Player wasn't whitelisted!");
        } else {
            player.sendMessage("Removing " + target.player.getName() + " from your whitelist!");
            whitelistedPlayers.remove(target.player.getUniqueId().toString());
        }
        mineConfig.set("whitelistedPlayers", whitelistedPlayers);
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
        List<String> bannedPlayers = mineConfig.getStringList("bannedPlayers");

        if (bannedPlayers.contains(target.player.getUniqueId().toString())) {
            player.sendMessage(ChatColor.RED + "Player was already banned!");
        } else {
            player.sendMessage(ChatColor.GREEN + "Banning player " + target.player.getName());
            bannedPlayers.add(target.player.getUniqueId().toString());
        }
        mineConfig.set("bannedPlayers", bannedPlayers);
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
        List<String> bannedPlayers = mineConfig.getStringList("bannedPlayers");

        if (!bannedPlayers.contains(target.player.getUniqueId().toString())) {
            player.sendMessage(ChatColor.RED + "Player wasn't banned!");
        } else {
            player.sendMessage(ChatColor.GREEN + "Unbanning player " + target.player.getName());
            bannedPlayers.remove(target.player.getUniqueId().toString());
        }
        mineConfig.set("bannedPlayers", bannedPlayers);
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
        List<String> priorityPlayers = mineConfig.getStringList("priorityPlayers");

        if (!priorityPlayers.contains(target.player.getUniqueId().toString())) {
            player.sendMessage(ChatColor.GREEN + "Adding " + target.player.getName() + " to the priority list!");
            priorityPlayers.add(target.player.getUniqueId().toString());
        } else {
            player.sendMessage(ChatColor.RED + "Removing " + target.player.getName() + " from the priority list!");
            priorityPlayers.remove(target.player.getUniqueId().toString());
        }
        mineConfig.set("priorityPlayers", priorityPlayers);
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
        mineConfig.set("coowner", target.player.getUniqueId());
        try {
            mineConfig.save(userFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



