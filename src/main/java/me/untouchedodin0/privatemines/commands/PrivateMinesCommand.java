package me.untouchedodin0.privatemines.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.untouchedodin0.privatemines.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import redempt.redlib.multiblock.MultiBlockStructure;
import redempt.redlib.region.CuboidRegion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SuppressWarnings("unused")
@CommandAlias("privatemines|privatemine|pm|pmine")
public class PrivateMinesCommand extends BaseCommand {

    Util util;
    Location to;
    Location position1;
    Location position2;
    Location start;
    Location end;
    Location corner1;
    Location corner2;
    World world;
    MultiBlockStructure multiBlockStructure;
    InputStream inputStream;
    CuboidRegion cuboidRegion;

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

        if (inputStream != null) {
            multiBlockStructure = MultiBlockStructure
                    .create(inputStream,
                            "mine",
                            false,
                            false);
        }
        world = to.getWorld();
        cuboidRegion = multiBlockStructure.getRegion(to);
        start = cuboidRegion.getStart();
        end = cuboidRegion.getEnd();

        Bukkit.broadcastMessage("start: " + start + " end:  " + end);

        for (int x = start.getBlockX(); x <= end.getBlockX(); x++) {
            for (int y = start.getBlockY(); y <= end.getBlockY(); y++) {
                for (int z = start.getBlockZ(); z <= end.getBlockZ(); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType() == Material.POWERED_RAIL) {
                        Bukkit.broadcastMessage("Powered rail at += " + block.getLocation());
                        if (corner1 == null) {
                            corner1 = block.getLocation();
                        } else if (corner2 == null) {
                            corner2 = block.getLocation();
                        }
                    }
                }
            }
        }
        multiBlockStructure.build(to);
    }
}
