package me.untouchedodin0.privatemines.utils.mine;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PrivateMineLocations extends Event implements Cancellable {

    private final Player player;
    private final Location mineLocation;
    private final Location spawnLocation;
    private final Location npcLocation;
    private final Location corner1;
    private final Location corner2;

    public PrivateMineLocations(
            Player player,
            Location mineLocation,
            Location spawnLocation,
            Location npcLocation,
            Location corner1,
            Location corner2) {
        this.player = player;
        this.mineLocation = mineLocation;
        this.spawnLocation = spawnLocation;
        this.npcLocation = npcLocation;
        this.corner1 = corner1;
        this.corner2 = corner2;
    }

    public Player getPlayer() {
        return player;
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

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    @Override
    public boolean isCancelled() {
        return false;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins.
     *
     * @param cancel true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {

    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return null;
    }
}
