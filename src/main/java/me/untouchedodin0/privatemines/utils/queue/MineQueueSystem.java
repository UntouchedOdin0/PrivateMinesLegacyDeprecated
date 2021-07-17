package me.untouchedodin0.privatemines.utils.queue;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

public class MineQueueSystem {

    final Queue<UUID> mineQueue = new LinkedList<>();

    /*
        Adds a player into the mine queue because they decided to p2w and get a private mine
     */

    public void queueMineCreation(Player player) {
        mineQueue.offer(player.getUniqueId());
    }

    /*
        The plugin has finally created the mine, let's remove them from the list.
     */

    public void removeFromQueue(Player player) {
        mineQueue.remove(player.getUniqueId());
    }

    /*
        Let's check if a player is still in the queue for a mine, they'll likely have a shorter
        time to get a mine than to get a graphics card. FUCK YOU SCALPERS.
     */

    public boolean queueContainsPlayer(Player player) {
        return mineQueue.contains(player.getUniqueId());
    }

    /*
        This is a way to get the fucking mine queue... idk why it's here but fuck it.
     */

    public Queue<UUID> getMine_queue() {
        return mineQueue;
    }

    /*
        This returns how many people are waiting in the queue to get their fucking mines made
     */

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

    public UUID getNextMine() {
        return mineQueue.peek();
    }
}
