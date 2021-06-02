package me.untouchedodin0.privatemines.worldedit.factory;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import me.untouchedodin0.privatemines.worldedit.WEHook;
import me.untouchedodin0.privatemines.worldedit.WorldEditRegion;
import me.untouchedodin0.privatemines.worldedit.WorldEditVector;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class MineFactory {

    World world;
    BukkitWorld weWorld;
    Clipboard clipboard;
    ClipboardFormat clipboardFormat;
    BlockVector3 blockVector3;
    WorldEditRegion region;
    Location spawnLocation;
    WorldEditVector minimum;
    WorldEditVector maximum;
    WorldEditRegion worldEditRegion;
    Operation operation;
    Location minimumLocation;
    Location maximumLocation;
    IterationFactory iterationFactory = new IterationFactory();

    public WorldEditRegion pasteSchematic(File file, Location location) throws IOException {

        world = location.getWorld();
        weWorld = new BukkitWorld(location.getWorld());
        clipboardFormat = ClipboardFormats.findByFile(file);
        blockVector3 = BlockVector3.at(location.getX(), location.getY(), location.getZ());

        if (clipboardFormat != null) {
            try (ClipboardReader clipboardReader = clipboardFormat.getReader(new FileInputStream(file))) {
                clipboard = clipboardReader.read();

                if (clipboard != null) {
                    try (EditSession editSession = WorldEdit.getInstance().newEditSession(weWorld)) {
                        operation = new ClipboardHolder(clipboard)
                                .createPaste(editSession)
                                .to(blockVector3)
                                .ignoreAirBlocks(true)
                                // configure here
                                .build();
                        Region clipboardRegion = clipboard.getRegion();

                        minimum = new WorldEditVector(
                                clipboardRegion.getMinimumPoint().getX(),
                                clipboardRegion.getMinimumPoint().getY(),
                                clipboardRegion.getMinimumPoint().getZ());

                        maximum = new WorldEditVector(
                                clipboardRegion.getMaximumPoint().getX(),
                                clipboardRegion.getMaximumPoint().getY(),
                                clipboardRegion.getMaximumPoint().getZ()
                        );

                        worldEditRegion = new WorldEditRegion(minimum, maximum, world);
                        this.region = worldEditRegion;

                        List<Location> cornerLocations = iterationFactory.findCornerBlocks(minimum, maximum, region.getWorld());

                        Bukkit.broadcastMessage("corner blocks pasteSchematic: " + cornerLocations);
                        for (WorldEditVector pt : loop(worldEditRegion)) {
                            final Block blockAt = world.getBlockAt((int) pt.getX(), (int) pt.getY(), (int) pt.getZ());
                            Material type = blockAt.getType();
                            if (type.isAir() || type.name().equals("LEGACY_AIR")) continue;
                            if (spawnLocation == null && type == Material.CHEST) {
                                spawnLocation = blockAt.getLocation();
                                Bukkit.broadcastMessage("spawnLocation: " + spawnLocation.toString());
                            } else {
                                if (type == Material.POWERED_RAIL) {
                                    if (minimum == null) {
                                        minimum = pt.copy();
                                        Bukkit.broadcastMessage("min: " + minimum.toString());
                                    }
                                    if (maximum == null) {
                                        maximum = pt.copy();
                                        Bukkit.broadcastMessage("max: " + maximum.toString());
                                    }
                                }
                            }
                        }
                        Operations.complete(operation);
                    } catch (WorldEditException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return worldEditRegion;
    }

    public void createPrivateMine(Player player, File file, Location location) {
        if (player == null) {
            Bukkit.getLogger().info("Couldn't create a private mine, because the player was null.");
        } else if (file == null) {
            Bukkit.getLogger().info("Couldn't create a private mine, because the file was null.");
        } else if (location == null) {
            Bukkit.getLogger().info("Couldn't create a private mine, because the location was null.");
        } else {
            try {
                region = pasteSchematic(file, location);
                if (region == null) {
                    Bukkit.broadcastMessage("The region was somehow null.");
                }
                minimumLocation = region.getMinimumLocation();
                maximumLocation = region.getMaximumLocation();
                /*
                BlockVector3 one = BlockVector3.at(
                        minimumLocation.getBlockX(),
                        minimumLocation.getBlockY(),
                        minimumLocation.getBlockZ());
                BlockVector3 two = BlockVector3.at(
                        maximumLocation.getBlockX(),
                        maximumLocation.getBlockY(),
                        maximumLocation.getBlockZ());
                 */
                List<Location> cornerLocations = iterationFactory.findCornerBlocks(minimum, maximum, region.getWorld());

                Bukkit.broadcastMessage("Corner Locations: " + cornerLocations.toString());

//                CuboidRegion cuboidRegion = new CuboidRegion(one, two);

//                for (WorldEditVector pt : loop(region)) {
//                    final Block blockAt = world.getBlockAt((int) pt.getX(), (int) pt.getY(), (int) pt.getZ());
//                    Material type = blockAt.getType();
//                    if (type == Material.CHEST) {
//                        Bukkit.broadcastMessage("Found chest at: " + blockAt.getLocation());
//                    }
//
//                    for (Iterator<BlockVector3> it = cuboidRegion.iterator(); it.hasNext(); ) {
//                        Location check = new Location(region.getWorld(), it.next().getBlockX(), it.next().getBlockY(), it.next().getBlockZ());
//                        Material material = check.getBlock().getType();
//                        if (material.isAir())
//                            return;
//                        if (material == Material.CHEST) {
//                            Bukkit.broadcastMessage("Found a chest at " + location);
//                        } else {
//                            if (material == Material.WHITE_WOOL) {
//                                Bukkit.broadcastMessage("Found wool at " + location);
//                            } else {
//                                if (material == Material.POWERED_RAIL) {
//                                    Bukkit.broadcastMessage("Found powered rail at " + location);
//                                }
//                            }
//                        }
//                    }
//                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Iterable<WorldEditVector> loop(WorldEditRegion region) {

        final Iterator<BlockVector3> vectors = WEHook.transform(region).iterator();
        final Iterator<WorldEditVector> weVecIterator = new Iterator<>() {
            @Override
            public boolean hasNext() {
                return vectors.hasNext();
            }

            @Override
            public WorldEditVector next() {
                return WEHook.transform(vectors.next());
            }
        }; //if only we had map() for iterators :(

        return () -> weVecIterator;
    }
}
