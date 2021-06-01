package me.untouchedodin0.privatemines.worldedit;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.function.pattern.RandomPattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class WEHook implements WorldEditHook {

    World world;
    BukkitWorld weWorld;
    final RandomPattern pattern = new RandomPattern();

    public static Region transform(WorldEditRegion region) {
        return new CuboidRegion(
                BukkitAdapter.adapt(region.getWorld()),
                transform(region.getMinimumPoint()),
                transform(region.getMaximumPoint())
        );
    }

    public static BlockVector3 transform(WorldEditVector vector) {
        return BlockVector3.at(vector.getX(), vector.getY(), vector.getZ());
    }

    public static WorldEditVector transform(BlockVector3 vector) {
        return new WorldEditVector(vector.getX(),vector.getY(),vector.getZ());
    }

    @Override
    public void fill(WorldEditRegion region, List<ItemStack> blocks) {
        world = region.getWorld();
        weWorld = new BukkitWorld(world);

        try (EditSession editSession = WorldEdit.getInstance().newEditSession(weWorld)) {
            final Region weRegion = transform(region);
            for (ItemStack itemStack : blocks) {
                Pattern pat = BukkitAdapter.adapt(itemStack.getType().createBlockData());
                pattern.add(pat, 1.0);
            }
            editSession.setBlocks(weRegion, pattern);
        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
        }
    }
}