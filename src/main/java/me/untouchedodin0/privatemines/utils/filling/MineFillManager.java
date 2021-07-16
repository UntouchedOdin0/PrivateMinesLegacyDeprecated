package me.untouchedodin0.privatemines.utils.filling;

import me.untouchedodin0.privatemines.PrivateMines;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.misc.WeightedRandom;
import redempt.redlib.region.CuboidRegion;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MineFillManager {

    CuboidRegion cuboidRegion;
    WeightedRandom<Material> weightedRandom = new WeightedRandom<>();
    File userFile;
    YamlConfiguration mineConfig;
    Location corner1;
    Location corner2;
    List<ItemStack> mineBlocks = new ArrayList<>();
    PrivateMines privateMines;

    public MineFillManager(PrivateMines privateMines) {
        this.privateMines = privateMines;
    }

    public void fillMineMultiple(Location location1, Location location2, List<ItemStack> items) {
        cuboidRegion = new CuboidRegion(location1, location2)
                .expand(1, 0, 1, 0, 1, 0);
        for (ItemStack item : items) {
            weightedRandom.set(item.getType(), 1);
        }
        cuboidRegion.forEachBlock(block -> {
            Material toSet = weightedRandom.roll();
            block.setType(toSet);
        });
    }

    @SuppressWarnings("unchecked")
    public void fillPlayerMine(Player player) {
        userFile = new File("plugins/PrivateMinesRewrite/mines/" + player.getUniqueId() + ".yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
        corner1 = mineConfig.getLocation("Corner1");
        corner2 = mineConfig.getLocation("Corner2");
        mineBlocks = (List<ItemStack>) mineConfig.getList("blocks");
        if (corner1 != null && corner2 != null && mineBlocks != null) {
            Bukkit.getScheduler().runTaskLater(privateMines, ()
                    -> fillMineMultiple(corner1, corner2, mineBlocks), 20L);
        }
    }

    @SuppressWarnings("unchecked")
    public void fillPlayerMine(UUID uuid) {
        userFile = new File("plugins/PrivateMinesRewrite/mines/" + uuid + ".yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
        corner1 = mineConfig.getLocation("Corner1");
        corner2 = mineConfig.getLocation("Corner2");
        mineBlocks = (List<ItemStack>) mineConfig.getList("blocks");
        if (corner1 != null && corner2 != null && mineBlocks != null) {
            Bukkit.getScheduler().runTaskLater(privateMines, ()
                    -> fillMineMultiple(corner1, corner2, mineBlocks), 20L);
        }
    }
}
