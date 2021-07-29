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
                block.setType(Material.ORANGE_WOOL, false);
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
        start = mineConfig.getLocation(CORNER_1_STRING);
        end = mineConfig.getLocation(CORNER_2_STRING);
        expandRegion = new CuboidRegion(start, end);
        expandRegionBedrock = new CuboidRegion(start, end);

        expandRegion.expand(1, 1, 0, 0, 1, 1);
        expandRegionBedrock.expand(0, 2, 0, 2, 0, 1);

        expandRegion.move(0, 0, 0);
//        expandRegionBedrock.move(0, 2, 0);

        expandRegion.stream().forEach(block -> {
            block.setType(Material.EMERALD_BLOCK);
        });

        expandRegionBedrock.stream().forEach(block -> {
            if (block.isEmpty()) {
                block.setType(Material.BEDROCK);
            }
        });

//        expandRegionBedrock.stream().forEach(block -> {
//            if (block.isEmpty()) {
//                block.setType(Material.REDSTONE_BLOCK);
//            }
//        });

        mineConfig.set(CORNER_1_STRING, expandRegion.getStart());
        mineConfig.set(CORNER_2_STRING, expandRegion.getEnd());
        try {
            mineConfig.save(userFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    public void expandMine(Player player) {
//        userFile = new File(MINE_DIRECTORY + player.getUniqueId() + ".yml");
//
//        if (!userFile.exists()) {
//            Bukkit.getLogger().info("Failed to upgrade " + player.getName() + "'s mine due to them not owning one");
//            return;
//        }
//        mineConfig = YamlConfiguration.loadConfiguration(userFile);
//        start = mineConfig.getLocation(CORNER_1_STRING);
//        end = mineConfig.getLocation(CORNER_2_STRING);
//        expandRegion = new CuboidRegion(start, end);
//        expandRegionBedrock = new CuboidRegion(start, end);
//
//        expandRegion.expand(1, 1, 0, 0, 1, 1);
//
//        expandRegionBedrock.forEachBlock(block -> {
//            block.setType(Material.AIR, false);
//        });
//
//        expandRegionBedrock.expand(2, 2, 0, 2, 0, 2); //posX before 3
//
//        expandRegion.move(0, 0, 0);
//        expandRegionBedrock.move(0, 2, 0);
//
//        fillCuboid(expandRegionBedrock, Material.EMERALD_BLOCK);
////        expandRegionBedrock.stream().forEach(north -> expandRegionBedrock.getFace(BlockFace.NORTH)
////                .stream()
////                .forEach(bedrock -> bedrock.setType(Material.EMERALD_BLOCK, false)));
////        expandRegionBedrock.stream().forEach(east -> expandRegionBedrock.getFace(BlockFace.EAST)
////                .stream()
////                .forEach(bedrock -> bedrock.setType(Material.BONE_BLOCK, false)));
////        expandRegionBedrock.stream().forEach(south -> expandRegionBedrock.getFace(BlockFace.SOUTH)
////                .stream()
////                .forEach(bedrock -> bedrock.setType(Material.DIAMOND_BLOCK, false)));
////        expandRegionBedrock.stream().forEach(west -> expandRegionBedrock.getFace(BlockFace.WEST)
////                .stream()
////                .forEach(bedrock -> bedrock.setType(Material.GOLD_BLOCK, false)));
////        expandRegionBedrock.stream().forEach(west -> expandRegionBedrock.getFace(BlockFace.DOWN)
////                .stream()
////                .forEach(bedrock -> bedrock.setType(Material.GREEN_WOOL, false)));
//
//        mineConfig.set(CORNER_1_STRING, expandRegion.getStart());
//        mineConfig.set(CORNER_2_STRING, expandRegion.getEnd());
//        try {
//            mineConfig.save(userFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    public void expandMine(Player player) {
//        userFile = new File(MINE_DIRECTORY + player.getUniqueId() + ".yml");
//
//        if (!userFile.exists()) {
//            Bukkit.getLogger().info("Failed to upgrade " + player.getName() + "'s mine due to them not owning one");
//            return;
//        }
//
//        mineConfig = YamlConfiguration.loadConfiguration(userFile);
//        start = mineConfig.getLocation(CORNER_1_STRING);
//        end = mineConfig.getLocation(CORNER_2_STRING);
//        expandRegion = new CuboidRegion(start, end);
//        expandRegionBedrock = new CuboidRegion(start, end);
//
//        expandRegion.expand(1, 1, 0, 0, 1, 1);
//        expandRegionBedrock.expand(3, 2, 0, 2, 0, 2);
//
//        expandRegion.move(0, 0, 0);
//        expandRegionBedrock.move(0, 2, 0);
//
//        expandRegion.stream().forEach(block -> {
//            block.setType(Material.EMERALD_BLOCK);
//        });
//
//        expandRegionBedrock.stream().forEach(block -> {
//            if (block.isEmpty()) {
//                block.setType(Material.REDSTONE_BLOCK);
//            }
//        });
//
//        mineConfig.set(CORNER_1_STRING, expandRegion.getStart());
//        mineConfig.set(CORNER_2_STRING, expandRegion.getEnd());
//        try {
//            mineConfig.save(userFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


//    public void expandMine(Player player) {
//        userFile = new File(MINE_DIRECTORY + player.getUniqueId() + ".yml");
//
//        if (!userFile.exists()) {
//            Bukkit.getLogger().info("Failed to upgrade " + player.getName() + "'s mine due to them not owning one");
//            return;
//        }
//        mineConfig = YamlConfiguration.loadConfiguration(userFile);
//        start = mineConfig.getLocation(CORNER_1_STRING);
//        end = mineConfig.getLocation(CORNER_2_STRING);
//        expandRegion = new CuboidRegion(start, end);
//        generateBedrock(expandRegion, 1);
//
//        Bukkit.broadcastMessage("start: " + start);
//        Bukkit.broadcastMessage("end: " + end);
//        Bukkit.broadcastMessage("expandRegion: " + expandRegion);
//
////        expandRegionBedrock = new CuboidRegion(start, end);
////        expandRegionBedrock.stream().forEach(block -> {
////            block.setType(Material.AIR);
////        });
////
////        expandRegion.expand(1, 1, 0, 0, 1, 1);
////        expandRegionBedrock.expand(2, 2, 0, 0, 2, 2);
////
////        expandRegion.move(0, 0, 0);
////        expandRegionBedrock.move(0, 0, 0);
//
////        for (BlockFace face : faces) {
////            expandRegionBedrock.stream().forEach(block ->
////                    expandRegionBedrock.getFace(face).stream().forEach(field ->
////                            field.setType(Material.BONE_BLOCK, false)));
////        }
//
//        /*
//        expandRegionBedrock.stream().forEach(north -> expandRegionBedrock.getFace(BlockFace.NORTH)
//                .stream()
//                .forEach(bedrock -> bedrock.setType(Material.EMERALD_BLOCK, false)));
//        expandRegionBedrock.stream().forEach(east -> expandRegionBedrock.getFace(BlockFace.EAST)
//                .stream()
//                .forEach(bedrock -> bedrock.setType(Material.BONE_BLOCK, false)));
//        expandRegionBedrock.stream().forEach(south -> expandRegionBedrock.getFace(BlockFace.SOUTH)
//                .stream()
//                .forEach(bedrock -> bedrock.setType(Material.DIAMOND_BLOCK, false)));
//        expandRegionBedrock.stream().forEach(west -> expandRegionBedrock.getFace(BlockFace.WEST)
//                .stream()
//                .forEach(bedrock -> bedrock.setType(Material.GOLD_BLOCK, false)));
//         */
//
////        expandRegionBedrock.stream().forEach(block -> {
////            block.setType(Material.RED_WOOL);
////        });
////
////        expandRegion.stream().forEach(block -> {
////            block.setType(Material.LIME_WOOL);
////        });
//
//        mineConfig.set(CORNER_1_STRING, expandRegion.getStart());
//        mineConfig.set(CORNER_2_STRING, expandRegion.getEnd());
//        try {
//            mineConfig.save(userFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
