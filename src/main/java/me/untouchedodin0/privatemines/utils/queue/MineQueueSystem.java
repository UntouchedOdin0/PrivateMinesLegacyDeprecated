/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021 Kyle Hicks
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
        mineQueue.add(player.getUniqueId());
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
