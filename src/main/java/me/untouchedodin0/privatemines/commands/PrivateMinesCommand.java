package me.untouchedodin0.privatemines.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.untouchedodin0.privatemines.utils.Util;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.multiblock.MultiBlockStructure;
import redempt.redlib.region.CuboidRegion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
    World world;
    MultiBlockStructure multiBlockStructure;
    InputStream inputStream;
    CuboidRegion cuboidRegion;
    ItemStack cornerMaterial = new ItemStack(Material.POWERED_RAIL);

    public PrivateMinesCommand(Util util) {
        this.util = util;
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

        world = to.getWorld();
        if (inputStream != null) {
            multiBlockStructure = util.createStructure(inputStream);
        }
        cuboidRegion = multiBlockStructure.getRegion(to);

        start = cuboidRegion.getStart();
        end = cuboidRegion.getEnd();

        List<Location> locations = util.findCornerBlocks(world, cuboidRegion);
        p.sendMessage("locations: " + locations);

        startFix = new Location(world,
                start.getBlockX() + 1,
                start.getBlockY(),
                start.getBlockZ());
        endFix = new Location(world,
                end.getBlockX() + 1,
                end.getBlockY(),
                end.getBlockZ());

        for (int x = start.getBlockX(); x <= end.getBlockX(); x++) {
            for (int y = start.getBlockY(); y <= end.getBlockY(); y++) {
                for (int z = start.getBlockZ(); z <= end.getBlockZ(); z++) {
                    final Block blockAt = world.getBlockAt(x, y, z);
                    Material type = blockAt.getType();

                    if (type == Material.AIR || type.name().equals("LEGACY_AIR")) continue;
                    if (type == cornerMaterial.getType()) {
                        Block block = world.getBlockAt(x, y, z);
                        if (block.getType() == cornerMaterial.getType()) {
                            Bukkit.broadcastMessage("Powered rail at += " + block.getLocation());

                            if (corner1 == null) {
                                corner1 = block.getLocation().clone();
                                Bukkit.broadcastMessage(ChatColor.YELLOW + "Start: "
                                        + corner1.getBlockX()
                                        + " "
                                        + corner1.getBlockY()
                                        + " "
                                        + corner1.getBlockZ());
                            }
                            if (corner2 == null) {
                                corner2 = block.getLocation().clone();
                                Bukkit.broadcastMessage(ChatColor.YELLOW + "Finish: "
                                        + corner2.getBlockX()
                                        + " "
                                        + corner2.getBlockY()
                                        + " "
                                        + corner2.getBlockZ());
                                return;
                            }
                        }
                    }
                }
            }
            multiBlockStructure.build(to);
        }
    }
}
