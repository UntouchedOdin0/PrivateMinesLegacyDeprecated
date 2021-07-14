package me.untouchedodin0.privatemines.utils.mine;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class PrivateMineUtil extends Event implements Cancellable {

    private final Player player;
    private final File mineFile;
    private boolean cancelled;
    private List<ItemStack> mineBlocks;
    private List<UUID> whitelistedPlayers;
    private List<UUID> bannedPlayers;
    private List<UUID> priorityPlayers;
    private UUID coowner;

    public static final HandlerList handlers = new HandlerList();

    public Player getPlayer() {
        return player;
    }

    public File getMineFile() {
        return mineFile;
    }

    public void setCancelled(boolean paramCancelled) {
        this.cancelled = paramCancelled;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public List<ItemStack> getMineBlocks() {
        return mineBlocks;
    }

    public List<UUID> getWhitelistedPlayers() {
        return whitelistedPlayers;
    }

    public List<UUID> getBannedPlayers() {
        return bannedPlayers;
    }

    public List<UUID> getPriorityPlayers() {
        return priorityPlayers;
    }

    public UUID getCoOwner() {
        return coowner;
    }

    public PrivateMineUtil(Player player,
                       File file,
//                       Location mineLocation,
//                       Location spawnLocation,
//                       Location npcLocation,
//                       Location corner1,
//                       Location corner2,
                       List<ItemStack> mineBlocks,
                       List<UUID> whitelistedPlayers,
                       List<UUID> bannedPlayers,
                       List<UUID> priorityPlayers,
                       UUID coowner) {
        this.player = player;
        this.mineFile = file;
//        this.mineLocation = mineLocation;
//        this.spawnLocation = spawnLocation;
//        this.npcLocation = npcLocation;
//        this.corner1 = corner1;
//        this.corner2 = corner2;
        this.mineBlocks = mineBlocks;
        this.whitelistedPlayers = whitelistedPlayers;
        this.bannedPlayers = bannedPlayers;
        this.priorityPlayers = priorityPlayers;
        this.coowner = coowner;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
