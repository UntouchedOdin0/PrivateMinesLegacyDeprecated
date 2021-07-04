package me.untouchedodin0.privatemines.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.untouchedodin0.privatemines.PrivateMines;
import me.untouchedodin0.privatemines.guis.MainMenuGui;
import me.untouchedodin0.privatemines.utils.Util;
import me.untouchedodin0.privatemines.utils.filling.MineFillManager;
import me.untouchedodin0.privatemines.utils.storage.MineStorage;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import redempt.redlib.blockdata.BlockDataManager;
import redempt.redlib.blockdata.DataBlock;
import redempt.redlib.misc.Task;
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
    File userFile;
    YamlConfiguration mineConfig;

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

    public PrivateMinesCommand(Util util,
                               MineFillManager fillManager,
                               PrivateMines privateMines,
                               MineStorage mineStorage,
                               MainMenuGui mainMenuGui) {
        this.util = util;
        this.fillManager = fillManager;
        this.privateMines = privateMines;
        this.mineStorage = mineStorage;
        this.mainMenuGui = mainMenuGui;
    }

    @Default
    @Description("Manage privatemines")
    public void main(Player p) {
        if (p.hasPermission("privatemines.owner")) {
            p.sendMessage("opening gui.");

            userFile = new File("plugins/PrivateMinesRewrite/mines/" + p.getUniqueId() + ".yml");
            mineConfig = YamlConfiguration.loadConfiguration(userFile);
            this.teleportLocation = mineConfig.getLocation("spawnLocation");

            /*
                Add gui menu with following items
                BED: Teleports to mine?
                ENDER_PEARL: resets the mine
             */

            ItemStack bedStack = new ItemStack(Material.RED_BED);
            ItemMeta bedStackItemMeta = bedStack.getItemMeta();
            bedStackItemMeta.setDisplayName(ChatColor.GREEN + "Go to your mine");
            bedStack.setItemMeta(bedStackItemMeta);

            ItemStack status = new ItemStack(Material.RED_WOOL);
            ItemMeta statusItemMeta = bedStack.getItemMeta();
            statusItemMeta.setDisplayName(ChatColor.GREEN + "Status");
            status.setItemMeta(statusItemMeta);

            ItemStack setTax = new ItemStack(Material.OAK_SIGN);
            ItemMeta setTaxItemMeta = setTax.getItemMeta();
            setTaxItemMeta.setDisplayName(ChatColor.GREEN + "Set Tax");
            setTax.setItemMeta(setTaxItemMeta);

            ItemStack mineSize = new ItemStack(Material.LAVA_BUCKET);
            ItemMeta mineSizeItemMeta = mineSize.getItemMeta();
            mineSizeItemMeta.setDisplayName(ChatColor.GREEN + "Mine Size");
            setTax.setItemMeta(setTaxItemMeta);

            ItemStack resetMine = new ItemStack(Material.MINECART);
            ItemMeta resetMineItemMeta = resetMine.getItemMeta();
            resetMineItemMeta.setDisplayName(ChatColor.GREEN + "Reset Mine");
            resetMine.setItemMeta(resetMineItemMeta);

            ItemStack whitelistedMembers = new ItemStack(Material.WATER_BUCKET);
            ItemMeta whitelistedMembersItemMeta = whitelistedMembers.getItemMeta();
            whitelistedMembersItemMeta.setDisplayName(ChatColor.GREEN + "Whitelisted Members");
            whitelistedMembers.setItemMeta(whitelistedMembersItemMeta);

            ItemStack bannedMembers = new ItemStack(Material.LAVA_BUCKET);
            ItemMeta bannedMembersItemMeta = bannedMembers.getItemMeta();
            bannedMembersItemMeta.setDisplayName(ChatColor.GREEN + "Banned Members");
            bannedMembers.setItemMeta(bannedMembersItemMeta);

            ItemStack priorityMembers = new ItemStack(Material.LAVA_BUCKET);
            ItemMeta priorityMembersItemMeta = priorityMembers.getItemMeta();
            priorityMembersItemMeta.setDisplayName(ChatColor.GREEN + "Priority Members");
            priorityMembers.setItemMeta(priorityMembersItemMeta);

            ItemStack coowner = new ItemStack(Material.DRAGON_EGG);
            ItemMeta coownerItemMeta = coowner.getItemMeta();
            coownerItemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Co-Owner");
            coowner.setItemMeta(coownerItemMeta);

            gui = Gui.gui().title(Component.text("Private Mine")).rows(1).create();

            GuiItem teleportToMine = ItemBuilder.from(bedStack).asGuiItem(event -> {
                event.setCancelled(true);
                p.sendMessage(ChatColor.GREEN + "Teleporting to your mine!");
                p.teleport(teleportLocation);
            });

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
    public void give(Player p) {
        File file = new File("plugins/PrivateMinesRewrite/schematics/structure.dat");
        userFile = new File("plugins/PrivateMinesRewrite/mines/" + p.getUniqueId() + ".yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
        to = p.getLocation();
        playerID = p.getUniqueId().toString();

        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (mineStorage.getMines().contains(p.getUniqueId())) {
            p.sendMessage(ChatColor.RED + "Er, you do know you already have a mine. Right?");
        } else {
            if (inputStream != null) {
                multiBlockStructure = MultiBlockStructure
                        .create(inputStream,
                                "mine",
                                false,
                                false);
            }
            world = to.getWorld();
            cuboidRegion = multiBlockStructure.getRegion(to);
            start = cuboidRegion.getStart().clone();
            end = cuboidRegion.getEnd().clone();

            multiBlockStructure.build(to);

            if (start == null || end == null) {
                Bukkit.getLogger().info("Failed to create the mine due to either");
                Bukkit.getLogger().info("the start of the end being null");
                Bukkit.broadcastMessage("The main cause of this is because the location is");
                Bukkit.broadcastMessage("either to high or to low.");
                return;
            }

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
                            spawnLocation.getBlock().setType(Material.AIR);
                        } else if (block.getType() == Material.WHITE_WOOL && npcLocation == null) {
                            npcLocation = block.getLocation();
                            npcLocation.getBlock().setType(Material.OAK_SIGN);
                        }
                    }
                }
            }

            miningRegion = new CuboidRegion(corner1, corner2)
                    .expand(1, 0, 1, 0, 1, 0);

            cornerBlocks.add(corner1);
            cornerBlocks.add(corner2);

            if (mineBlocks.isEmpty()) {
                Bukkit.broadcastMessage(ChatColor.RED + "Failed to reset the mine due to no Materials being listed!");
                Bukkit.broadcastMessage(ChatColor.GREEN + "Adding Stone to the list and trying again!");
                mineBlocks.add(new ItemStack(Material.STONE));
            }

            Sign s = (Sign) world.getBlockAt(npcLocation).getState();
            s.setLine(0, "I'm an NPC");
            s.setLine(1, "I should be fixed.");
            s.update();

            Location miningRegionStart = miningRegion.getStart();
            Location miningRegionEnd = miningRegion.getEnd();

            Bukkit.broadcastMessage("corner blocks: " + cornerBlocks);
            startBlock = miningRegionStart.getBlock();
            endBlock = miningRegionEnd.getBlock();

            mineConfig.set("corner1", corner1);
            mineConfig.set("corner2", corner2);
            mineConfig.set("spawnLocation", spawnLocation);
            mineConfig.set("npcLocation", npcLocation);
            mineConfig.set("blocks", mineBlocks);
            try {
                mineConfig.save(userFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (mineBlocks.toArray().length >= 2) {
                Task.syncRepeating(() -> fillManager.fillPlayerMine(p), 0L, 20L);
//                Task.syncDelayed(()
//                        -> fillManager
//                        .fillMineMultiple(corner1, corner2, mineBlocks));
//
//                Bukkit.getScheduler().runTaskLater(privateMines, ()
//                        -> fillManager.fillMineMultiple(corner1, corner2, mineBlocks), 20L);
            } else {
                Task.syncRepeating(() -> fillManager.fillPlayerMine(p), 0L, 2 * 20 * 60L);
                fillManager.fillMine(corner1, corner2, mineBlocks.get(0));
            }

            Bukkit.broadcastMessage("cornerBlocks debug: ");
            Bukkit.broadcastMessage("count: " + cornerBlocks.stream().count());
            cornerBlocks = new ArrayList<>();
            p.teleport(spawnLocation);
            p.sendMessage(ChatColor.GREEN + "You've been teleported to your mine!");
            npcLocation = null;
            corner1 = null;
            corner2 = null;
        }
    }


    @Subcommand("teleport")
    @Description("Teleports you to your mine")
    @CommandPermission("privatemines.teleport")
    public void teleport(Player p) {
        if (p != null) {
            if (spawnLocation == null) {
                p.sendMessage(ChatColor.RED + "The spawn location was null!");
            } else {
                p.sendMessage(ChatColor.GREEN + "Teleporting you to your mine!");
                p.teleport(spawnLocation);
            }
        }
    }

    /*
        Lets a player reset their mine
     */

    @Subcommand("reset")
    @Description("Resets your mine")
    @CommandPermission("privatemines.reset")
    public void reset(Player p) {
        if (p != null) {
            userFile = new File("plugins/PrivateMinesRewrite/mines/" + p.getUniqueId() + ".yml");
            mineConfig = YamlConfiguration.loadConfiguration(userFile);
            corner1 = mineConfig.getLocation("corner1");
            corner2 = mineConfig.getLocation("corner2");
            mineBlocks = (List<ItemStack>) mineConfig.getList("blocks");

            if (corner1 != null && corner2 != null && mineBlocks != null) {
                Bukkit.getScheduler().runTaskLater(privateMines, ()
                        -> fillManager.fillMineMultiple(corner1, corner2, mineBlocks), 20L);
            }
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
        userFile = new File("plugins/PrivateMinesRewrite/mines/" + target.player.getUniqueId() + ".yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
        corner1 = mineConfig.getLocation("corner1");
        corner2 = mineConfig.getLocation("corner2");
        mineBlocks = (List<ItemStack>) mineConfig.getList("blocks");

        if (corner1 != null && corner2 != null && mineBlocks != null) {
            Bukkit.getScheduler().runTaskLater(privateMines, ()
                    -> fillManager.fillMineMultiple(corner1, corner2, mineBlocks), 20L);
        }
    }

    public Location getTeleportLocation() {
        return teleportLocation;
    }
}



