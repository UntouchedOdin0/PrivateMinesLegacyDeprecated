package me.untouchedodin0.privatemines.utils.mine;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import redempt.redlib.region.CuboidRegion;

import java.io.File;
import java.io.IOException;

public class ExpandingMineUtil {

    private static final String MINE_DIRECTORY = "plugins/PrivateMinesRewrite/mines/";
    private static final String CORNER_1_STRING = "Corner1";
    private static final String CORNER_2_STRING = "Corner2";

    File userFile;
    YamlConfiguration mineConfig;

    Location start;
    Location end;

    CuboidRegion expandRegion;
    CuboidRegion expandRegionBedrock;

    /*
    public void expandMine(CuboidRegion region, double expand) {
        this.cuboidRegion = region;
        this.amount = expand;

        Bukkit.broadcastMessage("Before expanding start: " + cuboidRegion.getStart());
        Bukkit.broadcastMessage("Before expanding end: " + cuboidRegion.getEnd());

        cuboidRegion.expand(amount, 0, amount, 0, amount, 0);

        Bukkit.broadcastMessage("After expanding start: " + cuboidRegion.getStart());
        Bukkit.broadcastMessage("After expanding end: " + cuboidRegion.getEnd());
    }
     */

    public void expandMine(Player player) {
        userFile = new File(MINE_DIRECTORY + player.getUniqueId() + ".yml");

        if (!userFile.exists()) {
            Bukkit.getLogger().info("Failed to upgrade " + player.getName() + "'s mine due to them not owning one");
            return;
        }
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
        start = mineConfig.getLocation(CORNER_1_STRING);
        end = mineConfig.getLocation(CORNER_2_STRING);
        expandRegion = new CuboidRegion(start, end);
        expandRegionBedrock = new CuboidRegion(start, end);

        expandRegion.expand(1, 1, 0, 0, 1, 1);
        expandRegionBedrock.expand(3, 2, 0, 2, 0, 2);

        expandRegion.move(0, 0, 0);
        expandRegionBedrock.move(0, 2, 0);

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
