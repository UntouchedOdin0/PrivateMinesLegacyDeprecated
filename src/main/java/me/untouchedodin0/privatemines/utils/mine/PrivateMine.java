package me.untouchedodin0.privatemines.utils.mine;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.io.File;

public class PrivateMine extends Event implements Cancellable {

    private final Player player;
    private final File mineFile;
    private final Location mineLocation;
    private final Location spawnLocation;
    private final Location npcLocation;
    private final Location corner1;
    private final Location corner2;
    private boolean cancelled;
    public static final HandlerList handlers = new HandlerList();

    public Player getPlayer() {
        return player;
    }

    public File getMineFile() {
        return mineFile;
    }

    public Location getMineLocation() {
        return mineLocation;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public Location getNpcLocation() {
        return npcLocation;
    }

    public Location getCorner1() {
        return corner1;
    }

    public Location getCorner2() {
        return corner2;
    }

    public void setCancelled(boolean paramCancelled) {
        this.cancelled = paramCancelled;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public PrivateMine(Player player,
                       File file,
                       Location mineLocation,
                       Location spawnLocation,
                       Location npcLocation,
                       Location corner1,
                       Location corner2) {
        this.player = player;
        this.mineFile = file;
        this.mineLocation = mineLocation;
        this.spawnLocation = spawnLocation;
        this.npcLocation = npcLocation;
        this.corner1 = corner1;
        this.corner2 = corner2;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
