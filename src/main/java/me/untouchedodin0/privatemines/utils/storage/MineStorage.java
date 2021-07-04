package me.untouchedodin0.privatemines.utils.storage;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MineStorage {

    private final List<UUID> mines = new ArrayList<>();

    public void addMine(UUID playerId) {
        if (mines.contains(playerId)) {
            Bukkit.broadcastMessage("Failed to add player due to them already being in storage");
        } else {
            Bukkit.broadcastMessage("Player wasn't in the storage, adding them now!");
            mines.add(playerId);
        }
        Bukkit.broadcastMessage("mines: " + mines);
    }

    public void removeMine(UUID playerId) {
        if (mines.contains(playerId)) {
            Bukkit.broadcastMessage("Removing player from the storage!");
            mines.remove(playerId);
        } else {
            Bukkit.broadcastMessage("Failed to remove player due to them not being in storage");
        }
        Bukkit.broadcastMessage("mines: " + mines);
    }

    public List<UUID> getMines() {
        return mines;
    }
}
