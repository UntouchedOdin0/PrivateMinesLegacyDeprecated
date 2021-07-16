package me.untouchedodin0.privatemines.utils.queue;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

public class MineQueueSystem {

    final Queue<UUID> mineQueue = new LinkedList<>();

    public void queueMineCreation(Player player) {
        mineQueue.add(player.getUniqueId());
    }

    public void removeFromQueue(Player player) {
        mineQueue.remove(player.getUniqueId());
    }

    public boolean queueContainsPlayer(Player player) {
        return mineQueue.contains(player.getUniqueId());
    }

    public Queue<UUID> getMine_queue() {
        return mineQueue;
    }

    public int getQueueSize() {
        return mineQueue.size();
    }

    /*
        Gets the current queue slot for the UUID specified.
     */

    public int getQueueSlot(UUID uuid) {
        int count = 0;
        for (UUID id : mineQueue) {
            count++;
            if (id == uuid) {
                Bukkit.broadcastMessage(String.format("%s is at position %d in the queue.",
                        Bukkit.getPlayer(uuid).getName(),
                        count));
                break;
            }
        }
        return count;
    }
}
