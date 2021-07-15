package me.untouchedodin0.privatemines.factory;

import me.untouchedodin0.privatemines.utils.Util;
import me.untouchedodin0.privatemines.utils.filling.MineFillManager;
import me.untouchedodin0.privatemines.utils.mine.PrivateMineLocations;
import me.untouchedodin0.privatemines.utils.mine.PrivateMineUtil;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MineFactory {

    private static final String UTIL_DIRECTORY = "plugins/PrivateMinesRewrite/util/";
    private static final String MINE_DIRECTORY = "plugins/PrivateMinesRewrite/mines/";
    private static final String CORNER_1_STRING = "Corner1";
    private static final String CORNER_2_STRING = "Corner2";
    private static final String SPAWN_LOCATION_STRING = "spawnLocation";
    private static final String NPC_LOCATION_STRING = "npcLocation";
    private static final String PLACE_LOCATION_STRING = "placeLocation";
    private static final String BLOCKS_STRING = "blocks";
    private static final String WHITELISTED_PLAYERS = "whitelistedPlayers";
    private static final String BANNED_PLAYERS = "bannedPlayers";
    private static final String PRIORITY_PLAYERS = "priorityPlayers";
    private static final String CO_OWNER = "coowner";

    MineStorage mineStorage;
    MultiBlockStructure multiBlockStructure;
    World world;
    CuboidRegion cuboidRegion;
    CuboidRegion miningRegion;
    Location start;
    Location end;
    Location corner1;
    Location corner2;
    Location spawnLocation;
    Location npcLocation;
    Location nextLocation;
    Location placeLocation;
    Block startBlock;
    Block endBlock;
    MineWorldManager mineWorldManager;
    MineFillManager fillManager;
    File userFile;
    File locationsFile;
    String playerID;
    YamlConfiguration mineConfig;
    YamlConfiguration locationConfig;

    List<ItemStack> mineBlocks = new ArrayList<>();
    List<Location> cornerBlocks = new ArrayList<>();
    List<UUID> whitelistedPlayers = new ArrayList<>();
    List<UUID> bannedPlayers = new ArrayList<>();
    List<UUID> priorityPlayers = new ArrayList<>();
    UUID coowner = null;

    PrivateMineUtil privateMineUtil;
    PrivateMineLocations privateMineLocations;
    Util util;

    public MineFactory(MineStorage storage,
                       MineWorldManager mineWorldManager,
                       MineFillManager fillManager,
                       Util util) {
        this.mineStorage = storage;
        this.mineWorldManager = mineWorldManager;
        this.fillManager = fillManager;
        this.util = util;
    }

    public void createMine(Player player, Location location) {

        File file = new File("plugins/PrivateMinesRewrite/schematics/structure.dat");
        userFile = new File(MINE_DIRECTORY + player.getUniqueId() + ".yml");
        locationsFile = new File(UTIL_DIRECTORY, "locations.yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
        locationConfig = YamlConfiguration.loadConfiguration(locationsFile);

        playerID = player.getUniqueId().toString();
        multiBlockStructure = util.getMultiBlockStructure();

        if (mineStorage.hasMine(player)) {
            player.sendMessage(ChatColor.RED + "Er, you do know you already have a mine. Right?");
        } else {
            world = location.getWorld();
            cuboidRegion = multiBlockStructure.getRegion(location);
            start = cuboidRegion.getStart().clone();
            end = cuboidRegion.getEnd().clone();

            multiBlockStructure.build(location);

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
                        } else if (block.getType() == Material.SPONGE && placeLocation == null) {
                            placeLocation = block.getLocation();
                            placeLocation.getBlock().setType(Material.AIR);
                            Bukkit.broadcastMessage("placeLocation after setair: " + placeLocation);
                            Bukkit.broadcastMessage("nextLocation after add: " + nextLocation);
                            Bukkit.broadcastMessage("placeLocation = next: " + placeLocation);
                        }
                    }
                }
            }
            miningRegion = new CuboidRegion(corner1, corner2)
                    .expand(1, 0, 1, 0, 1, 0);
            cornerBlocks.add(corner1);
            cornerBlocks.add(corner2);
            if (mineBlocks.isEmpty()) {
                mineBlocks.add(new ItemStack(Material.STONE));
            }
            Sign s = (Sign) world.getBlockAt(npcLocation).getState();
            s.setLine(0, "I'm an NPC");
            s.setLine(1, "I should be fixed.");
            s.update();
            Location miningRegionStart = miningRegion.getStart();
            Location miningRegionEnd = miningRegion.getEnd();
            startBlock = miningRegionStart.getBlock();
            endBlock = miningRegionEnd.getBlock();
            Bukkit.getLogger().info("Creating the event...");
            privateMineUtil = new PrivateMineUtil(player, file, mineBlocks, whitelistedPlayers, bannedPlayers, priorityPlayers, coowner);
            privateMineLocations = new PrivateMineLocations(player, nextLocation, spawnLocation, npcLocation, corner1, corner2);

            Bukkit.getLogger().info("Calling the events...");
            Bukkit.getPluginManager().callEvent(privateMineUtil);
            Bukkit.getPluginManager().callEvent(privateMineLocations);

//            Bukkit.getPluginManager().callEvent(privateMine);
//            Bukkit.getLogger().info("Event Details:");
//            Bukkit.getLogger().info(privateMine.getEventName());
//            Bukkit.broadcastMessage("event details: " + privateMine);
//            mineConfig.set(CORNER_1_STRING, privateMine.getCorner1());
//            mineConfig.set(CORNER_2_STRING, privateMine.getCorner2());
//            mineConfig.set(SPAWN_LOCATION_STRING, privateMine.getSpawnLocation());
//            mineConfig.set(NPC_LOCATION_STRING, privateMine.getNpcLocation());
//            mineConfig.set(PLACE_LOCATION_STRING, privateMine.getMineLocation());
//            mineConfig.set(BLOCKS_STRING, privateMine.getMineBlocks());
//            mineConfig.set(WHITELISTED_PLAYERS, privateMine.getWhitelistedPlayers());
//            mineConfig.set(BANNED_PLAYERS, privateMine.getBannedPlayers());
//            mineConfig.set(PRIORITY_PLAYERS, privateMine.getPriorityPlayers());
//            mineConfig.set(CO_OWNER, privateMine.getCoOwner());

            try {
                mineConfig.save(userFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Task.syncRepeating(() -> fillManager.fillPlayerMine(player), 0L, 20L);
//
//            if (mineBlocks.toArray().length >= 2) {
//                Task.syncRepeating(() -> fillManager.fillPlayerMine(player), 0L, 20L);
//            } else {
//                Task.syncRepeating(() -> fillManager.fillPlayerMine(player), 0L, 2 * 20 * 60L);
//                fillManager.fillMine(privateMineLocations.getCorner1(), privateMineLocations.getCorner2(), mineBlocks.get(0));
//            }

            Bukkit.broadcastMessage("cornerBlocks debug: ");
            Bukkit.broadcastMessage("count: " + cornerBlocks.stream().count());
            cornerBlocks = new ArrayList<>();
            player.teleport(spawnLocation);
            player.sendMessage(ChatColor.GREEN + "You've been teleported to your mine!");
            npcLocation = null;
            corner1 = null;
            corner2 = null;

            Bukkit.broadcastMessage("start: " + start);
            Bukkit.broadcastMessage("end: " + end);
        }
    }

    public void upgradeMine(Player player, File newMineFile) {

    }
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

