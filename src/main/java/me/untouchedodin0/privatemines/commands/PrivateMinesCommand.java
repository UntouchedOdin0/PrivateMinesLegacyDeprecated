package me.untouchedodin0.privatemines.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.untouchedodin0.privatemines.PrivateMines;
import me.untouchedodin0.privatemines.utils.Util;
import me.untouchedodin0.privatemines.utils.filling.MineFillManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.blockdata.BlockDataManager;
import redempt.redlib.blockdata.DataBlock;
import redempt.redlib.misc.WeightedRandom;
import redempt.redlib.multiblock.MultiBlockStructure;
import redempt.redlib.multiblock.Rotator;
import redempt.redlib.multiblock.Structure;
import redempt.redlib.region.CuboidRegion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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

    String coords1 = "";
    String coords2 = "";

    String x1 = "";
    String y1 = "";
    String z1 = "";

    String x2 = "";
    String y2 = "";
    String z2 = "";

    Block startBlock;
    Block endBlock;
    DataBlock startDataBlock;
    DataBlock endDataBlock;

    public PrivateMinesCommand(Util util,
                               MineFillManager fillManager,
                               PrivateMines privateMines) {
        this.util = util;
        this.fillManager = fillManager;
        this.privateMines = privateMines;
    }

    @Default
    @Description("Manage privatemines")
    public void main(Player p) {
        if (p.hasPermission("privatemines.owner")) {
            p.sendMessage("opening gui.");
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

        if (p != null) {
            to = p.getLocation();
        }

        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

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
                    } else if (block.getType() == Material.WHITE_WOOL && npcLocation == null) {
                        npcLocation = block.getLocation();
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

        Location miningRegionStart = miningRegion.getStart();
        Location miningRegionEnd = miningRegion.getEnd();

        Bukkit.broadcastMessage("corner blocks: " + cornerBlocks);
        startBlock = miningRegionStart.getBlock();
        endBlock = miningRegionEnd.getBlock();

        if (mineBlocks.toArray().length >= 2) {
            Bukkit.getScheduler().runTaskLater(privateMines, ()
                    -> fillManager.fillMineMultiple(corner1, corner2, mineBlocks), 20L);
        } else {
            fillManager.fillMine(corner1, corner2, mineBlocks.get(0));
            corner1 = null;
            corner2 = null;
            start = null;
            end = null;
        }

        Bukkit.broadcastMessage("cornerBlocks debug: ");
        Bukkit.broadcastMessage("count: " + cornerBlocks.stream().count());
        cornerBlocks = new ArrayList<>();
        Bukkit.broadcastMessage("count after iterator: " + cornerBlocks.stream().count());
        if (p != null) {
            p.teleport(spawnLocation);
            p.sendMessage(ChatColor.GREEN + "You've been teleported to your mine!");
            spawnLocation = null;
            npcLocation = null;
        }
    }
}



