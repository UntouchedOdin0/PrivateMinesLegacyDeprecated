package me.untouchedodin0.privatemines.worldedit.factory;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;
import me.untouchedodin0.privatemines.worldedit.WEHook;
import me.untouchedodin0.privatemines.worldedit.WorldEditRegion;
import me.untouchedodin0.privatemines.worldedit.WorldEditVector;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Iterator;
import java.util.List;

public class IterationFactory {

    World theWorld;
    BukkitWorld weWorld;
    WorldEditRegion region;
    List<Location> locations;

    public List<Location> findCornerBlocks(WorldEditVector one, WorldEditVector two, World world) {
        theWorld = world;
        weWorld = new BukkitWorld(theWorld);

        if (theWorld == null) {
            Bukkit.getLogger().warning("The world was null in the InterationFactory");
        } else if (one == null) {
            Bukkit.getLogger().warning("The WorldEditVector one was null!");
        } else if (two == null) {
            Bukkit.getLogger().warning("The WorldEditVector two was null!");
        }

        if (one != null && two != null) {
            Bukkit.broadcastMessage("one " + one.toString());

            region = new WorldEditRegion(one, two, theWorld);
            Bukkit.broadcastMessage("region " + region.toString());
            for (WorldEditVector pt : loop(region)) {
                final Block blockAt = theWorld.getBlockAt((int) pt.getX(), (int) pt.getY(), (int) pt.getZ());
                Material type = blockAt.getType();
                if (type == Material.AIR) continue;

                if (type == Material.POWERED_RAIL) {
                    Bukkit.broadcastMessage("Found powered rail at: " + blockAt.getLocation());
                    locations.add(blockAt.getLocation());
                    Bukkit.broadcastMessage(String.valueOf(locations));
                }
            }
        }
        return locations;
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