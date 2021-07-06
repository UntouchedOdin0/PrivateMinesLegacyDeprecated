package me.untouchedodin0.privatemines.data;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;

public class PrivateMine {

    private final Player owner;
    private final Location mineLocation;
    private final List<Location> cornerBlocks;
    private final File mineFile;
    private final List<ItemStack> blocks;

    public PrivateMine(Player owner,
                       Location location,
                       List<Location> cornerBlocks,
                       File mineFile,
                       List<ItemStack> blocks) {
        this.owner = owner;
        this.mineLocation = location;
        this.cornerBlocks = cornerBlocks;
        this.mineFile = mineFile;
        this.blocks = blocks;
    }

    public PrivateMine getPrivateMine() {
        return new PrivateMine(this.owner, this.mineLocation, this.cornerBlocks, this.mineFile, this.blocks);
    }

    public Player getOwner() {
        return owner;
    }

    public Location getMineLocation() {
        return mineLocation;
    }

    public List<Location> getCornerBlocks() {
        return cornerBlocks;
    }

    public File getMineFile() {
        return mineFile;
    }

    public List<ItemStack> getBlocks() {
        return blocks;
    }
}
