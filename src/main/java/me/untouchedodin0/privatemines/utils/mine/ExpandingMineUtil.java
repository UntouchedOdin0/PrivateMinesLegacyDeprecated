package me.untouchedodin0.privatemines.utils.mine;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import redempt.redlib.region.CuboidRegion;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

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
    CuboidRegion cuboidRegion;

    BlockFace[] faces = {
            BlockFace.DOWN,
            BlockFace.NORTH,
            BlockFace.EAST,
            BlockFace.SOUTH,
            BlockFace.WEST
    };

    int currentSize = 1;

    public void generateBedrock(CuboidRegion cuboidRegion, int amount) {
        currentSize = currentSize + amount;
        cuboidRegion = cuboidRegion.clone().expand(amount, amount, 0, amount, amount, amount);
        for (BlockFace face : faces) {
            cuboidRegion.getFace(face).forEachBlock(block -> {
                block.setType(Material.WOOL, false);
            });
        }
    }

    public void fillCuboid(CuboidRegion cuboidRegion, Material material) {
        int xMin = Integer.min(cuboidRegion.getStart().getBlockX(), cuboidRegion.getEnd().getBlockX());
        int xMax = Integer.max(cuboidRegion.getStart().getBlockX(), cuboidRegion.getEnd().getBlockX());
        int yMin = Integer.min(cuboidRegion.getStart().getBlockY(), cuboidRegion.getEnd().getBlockY());
        int yMax = Integer.max(cuboidRegion.getStart().getBlockY(), cuboidRegion.getEnd().getBlockY());
        int zMin = Integer.min(cuboidRegion.getStart().getBlockZ(), cuboidRegion.getEnd().getBlockZ());
        int zMax = Integer.max(cuboidRegion.getStart().getBlockZ(), cuboidRegion.getEnd().getBlockZ());
        World world = cuboidRegion.getWorld();

        for (int x = xMin; x <= xMax; x++) {
            for (int y = yMin; x <= yMax; y++) {
                for (int z = zMin; x <= zMax; z++) {
                    world.getBlockAt(x, y, z).setType(material);
                }
            }
        }
    }

    public void expandMine(Player player) {
        userFile = new File(MINE_DIRECTORY + player.getUniqueId() + ".yml");

        if (!userFile.exists()) {
            Bukkit.getLogger().info("Failed to upgrade " + player.getName() + "'s mine due to them not owning one");
            return;
        }
        mineConfig = YamlConfiguration.loadConfiguration(userFile);

        start = mineConfig.getSerializable(CORNER_1_STRING, Location.class);
        start = mineConfig.getSerializable(CORNER_2_STRING, Location.class);

        expandRegion = new CuboidRegion(start, end);
        expandRegion.expand(5, 5, 0, 5, 5, 5);

        expandRegionBedrock.stream().forEach(block -> {
            if (block.isEmpty()) {
                block.setType(Material.BEDROCK);
            }
        });

        expandRegion.stream().forEach(block -> block.setType(Material.EMERALD_BLOCK));

        mineConfig.set(CORNER_1_STRING, expandRegion.getStart());
        mineConfig.set(CORNER_2_STRING, expandRegion.getEnd());
        try {
            mineConfig.save(userFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void expandMine(Player player, int amount) {
        userFile = new File(MINE_DIRECTORY + player.getUniqueId() + ".yml");

        if (!userFile.exists()) {
            Bukkit.getLogger().info("Failed to upgrade " + player.getName() + "'s mine due to them not owning one");
            return;
        }
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
        start = mineConfig.getSerializable(CORNER_1_STRING, Location.class);
        end = mineConfig.getSerializable(CORNER_2_STRING, Location.class);

        expandRegion = new CuboidRegion(start, end);
        expandRegion.expand(amount, amount, 0, 0, amount, amount);
        expandRegion.forEachBlock(block -> block.setType(Material.STONE));
        Stream.of(
                BlockFace.NORTH,
                BlockFace.EAST,
                BlockFace.SOUTH,
                BlockFace.WEST)
                .forEach(blockFace -> expandRegion.getFace(blockFace).forEachBlock(block -> block.setType(Material.BEDROCK)));
        expandRegion.getFace(BlockFace.UP).forEachBlock(block -> block.setType(Material.AIR));
        expandRegion.getFace(BlockFace.DOWN).forEachBlock(block -> block.setType(Material.BEDROCK));


        mineConfig.set(CORNER_1_STRING, expandRegion.getStart());
        mineConfig.set(CORNER_2_STRING, expandRegion.getEnd());
        try {
            mineConfig.save(userFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
