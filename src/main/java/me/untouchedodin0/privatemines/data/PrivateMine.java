package me.untouchedodin0.privatemines.data;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;

public class PrivateMine {

    private Player owner;
    private Location mineLocation;
    private List<Location> cornerBlocks;
    private File mineFile;
    private List<ItemStack> blocks;

    public PrivateMine() {
    }

    public PrivateMine(Player owner, Location mineLocation, List<Location> cornerBlocks, File mineFile, List<ItemStack> blocks) {
    }

    public Player getOwner() {
        return owner;
    }

    public PrivateMine setOwner(Player owner) {
        this.owner = owner;
        return this;
    }

    public Location getMineLocation() {
        return mineLocation;
    }

    public PrivateMine setLocation(Location location) {
        this.mineLocation = location;
        return this;
    }

    public List<Location> getCornerBlocks() {
        return cornerBlocks;
    }

    public PrivateMine setCornerBlocks(List<Location> cornerBlocks) {
        this.cornerBlocks = cornerBlocks;
        return this;
    }

    public File getMineFile() {
        return mineFile;
    }

    public PrivateMine setFile(File file) {
        this.mineFile = file;
        return this;
    }

    public List<ItemStack> getBlocks() {
        return blocks;
    }

    public PrivateMine setBlocks(List<ItemStack> blocks) {
        this.blocks = blocks;
        return this;
    }

    public PrivateMine build() {
        return new PrivateMine(owner, mineLocation, cornerBlocks, mineFile, blocks);
    }
}
