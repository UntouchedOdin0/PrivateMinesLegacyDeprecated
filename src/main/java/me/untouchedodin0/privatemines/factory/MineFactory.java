package me.untouchedodin0.privatemines.factory;

import me.untouchedodin0.privatemines.utils.Util;
import me.untouchedodin0.privatemines.utils.filling.MineFillManager;
import me.untouchedodin0.privatemines.utils.storage.MineStorage;
import me.untouchedodin0.privatemines.world.MineWorldManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.misc.Task;
import redempt.redlib.multiblock.MultiBlockStructure;
import redempt.redlib.region.CuboidRegion;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MineFactory {

    MineStorage mineStorage;
    InputStream inputStream;
    MultiBlockStructure multiBlockStructure;
    World world;
    CuboidRegion cuboidRegion;
    CuboidRegion miningRegion;
    Location to;
    Location start;
    Location end;
    Location corner1;
    Location corner2;
    Location spawnLocation;
    Location npcLocation;
    Location nextLocation;

    MineWorldManager mineWorldManager;
    MineFillManager fillManager;

    File userFile;
    File locationsFile;
    String playerID;


    private static final String utilDirectory = "plugins/PrivateMinesRewrite/util/";
    private static final String minesDirectory = "plugins/PrivateMinesRewrite/mines/";

    YamlConfiguration mineConfig;
    YamlConfiguration locationConfig;

    List<ItemStack> mineBlocks = new ArrayList<>();
    List<Location> cornerBlocks = new ArrayList<>();

    public MineFactory(MineStorage storage,
                       MineWorldManager mineWorldManager,
                       MineFillManager fillManager) {
        this.mineStorage = storage;
        this.mineWorldManager = mineWorldManager;
        this.fillManager = fillManager;
    }

    public void createMine(Player player, Location location, InputStream stream) {

        File file = new File("plugins/PrivateMinesRewrite/schematics/structure.dat");
        userFile = new File(minesDirectory + player.getUniqueId() + ".yml");
        locationsFile = new File(utilDirectory, "locations.yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
        locationConfig = YamlConfiguration.loadConfiguration(locationsFile);

        to = location;
        playerID = player.getUniqueId().toString();

        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        if (mineStorage.hasMine(p)) {
//            p.sendMessage(ChatColor.RED + "Er, you do know you already have a mine. Right?");
//        } else {
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
                        if (block.getState().getData() instanceof Directional) {
                            spawnLocation.setYaw(Util.getYaw((((Directional) block.getState().getData()).getFacing())));
                        }
                        spawnLocation.getBlock().setType(Material.AIR);
                    } else if (block.getType() == Material.WHITE_WOOL && npcLocation == null) {
                        npcLocation = block.getLocation();
                        npcLocation.getBlock().setType(Material.OAK_SIGN);
                    } else if (block.getType() == Material.SPONGE && to == null) {
                        to = block.getLocation();
                        to.getBlock().setType(Material.AIR);
                        Bukkit.broadcastMessage("placeLocation after setair: " + to);
                        nextLocation = to.clone();
                        Bukkit.broadcastMessage("nextLocation after clone: " + nextLocation);
                        nextLocation = nextLocation.add(10, 0, 0);
                        Bukkit.broadcastMessage("nextLocation after add: " + nextLocation);
                        to = nextLocation;
                        Bukkit.broadcastMessage("placeLocation = next: " + to);
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

        Bukkit.broadcastMessage("corner blocks: " + cornerBlocks);

//            Bukkit.broadcastMessage("privateMine builder owner = " + privateMine.getOwner());
//            Bukkit.broadcastMessage("privateMine builder mineLocation = " + privateMine.getMineLocation());
//            Bukkit.broadcastMessage("privateMine builder cornerblocks = " + privateMine.getCornerBlocks());
//            Bukkit.broadcastMessage("privateMine builder mineFile = " + privateMine.getMineFile());
//            Bukkit.broadcastMessage("privateMine builder blocks = " + privateMine.getBlocks());

        mineConfig.set("corner1", corner1);
        mineConfig.set("corner2", corner2);
        mineConfig.set("spawnLocation", spawnLocation);
        mineConfig.set("npcLocation", npcLocation);
        mineConfig.set("placeLocation", to);
        mineConfig.set("blocks", mineBlocks);
        try {
            mineConfig.save(userFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mineBlocks.toArray().length >= 2) {
            Task.syncRepeating(() -> fillManager.fillPlayerMine(player), 0L, 20L);
        } else {
            Task.syncRepeating(() -> fillManager.fillPlayerMine(player), 0L, 2 * 20 * 60L);
            fillManager.fillMine(corner1, corner2, mineBlocks.get(0));
        }

        Bukkit.broadcastMessage("cornerBlocks debug: ");
        Bukkit.broadcastMessage("count: " + cornerBlocks.stream().count());
        cornerBlocks = new ArrayList<>();
        player.teleport(spawnLocation);
        player.sendMessage(ChatColor.GREEN + "You've been teleported to your mine!");
        npcLocation = null;
        corner1 = null;
        corner2 = null;
    }


//        this.inputStream = stream;
//        if (mineStorage.hasMine(player)) {
//            Bukkit.getLogger().info("Player was already in the storage, no need to give another mine!");
//        } else if (inputStream != null) {
//            player.sendMessage("Creating mine from storage");
//            to = location;
//            multiBlockStructure = MultiBlockStructure
//                    .create(inputStream,
//                            "mine",
//                            false,
//                            false);
//            world = mineWorldManager.getMinesWorld();
//            multiBlockStructure.build(to);
//            cuboidRegion = multiBlockStructure.getRegion(to);
//            start = cuboidRegion.getStart().clone();
//            end = cuboidRegion.getEnd().clone();
//            cornerBlocks = findCornerBlocks(start, end);
//            spawnLocation = findSpawnLocation(start, end);
//            npcLocation = findNPCLocation(start, end);
//
//            miningRegion = new CuboidRegion(cornerBlocks.get(0), cornerBlocks.get(1))
//                    .expand(1, 0, 1, 0, 1, 0);
//        }
//        player.teleport(spawnLocation);
//        player.sendMessage(ChatColor.GREEN + "You've been given a mine!");


    public List<Location> findCornerBlocks(Location start, Location end) {
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
                    }
                }
            }
        }
        cornerBlocks.add(corner1);
        cornerBlocks.add(corner2);
        return cornerBlocks;
    }

    public Location findSpawnLocation(Location start, Location end) {
        for (int x = start.getBlockX(); x <= end.getBlockX(); x++) {
            for (int y = start.getBlockY(); y <= end.getBlockY(); y++) {
                for (int z = start.getBlockZ(); z <= end.getBlockZ(); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType() == Material.POWERED_RAIL) {
                        spawnLocation = block.getLocation();
                    }
                }
            }
        }
        return spawnLocation;
    }

    public Location findNPCLocation(Location start, Location end) {
        for (int x = start.getBlockX(); x <= end.getBlockX(); x++) {
            for (int y = start.getBlockY(); y <= end.getBlockY(); y++) {
                for (int z = start.getBlockZ(); z <= end.getBlockZ(); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType() == Material.WHITE_WOOL) {
                        npcLocation = block.getLocation();
                    }
                }
            }
        }
        return npcLocation;
    }
}
