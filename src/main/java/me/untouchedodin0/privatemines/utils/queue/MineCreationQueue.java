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

import me.untouchedodin0.privatemines.factory.MineFactory;
import me.untouchedodin0.privatemines.world.MineWorldManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class MineCreationQueue extends BukkitRunnable {

    Plugin plugin;
    private BukkitTask bukkitTask;
    private MineQueueSystem mineQueueSystem;
    private MineWorldManager mineWorldManager;
    private MineFactory mineFactory;
    private Location nextLocation;
    UUID nextMine;

    public MineCreationQueue(Plugin plugin,
                             MineQueueSystem queueSystem,
                             MineFactory mineFactory,
                             MineWorldManager mineWorldManager) {
        this.plugin = plugin;
        this.mineQueueSystem = queueSystem;
        this.mineFactory = mineFactory;
        this.mineWorldManager = mineWorldManager;
    }

    public void start() {
        if (bukkitTask == null) {
            Bukkit.getLogger().info("Starting the MineCreationQueue bukkitTask.");
            bukkitTask = this.runTaskTimer(plugin, 0L, 5 * 20L); // Should fix the lag?
            Bukkit.getLogger().info("Started the task successfully!");
        }
    }

    @Override
    public synchronized void cancel() {
        if (bukkitTask != null && !bukkitTask.isCancelled()) {
            Bukkit.getLogger().info("Cancelling the MineCreationQueue bukkitTask.");
            bukkitTask.cancel();
        }
    }

    @Override
    public void run() {
        Bukkit.getLogger().info("Running the MineCreationQueue bukkitTask.");
        Bukkit.broadcastMessage(mineQueueSystem.mineQueue.toString());
        if (mineQueueSystem.getNextMine() != null) {
            Bukkit.broadcastMessage(nextMine.toString());
            nextLocation = mineWorldManager.nextFreeLocation();
            mineFactory.createMine(Bukkit.getPlayer(mineQueueSystem.getNextMine()));
        }
    }
}

