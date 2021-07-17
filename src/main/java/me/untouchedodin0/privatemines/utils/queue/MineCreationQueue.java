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
            mineFactory.createMine(Bukkit.getPlayer(mineQueueSystem.getNextMine()), nextLocation);
        }
    }
}

