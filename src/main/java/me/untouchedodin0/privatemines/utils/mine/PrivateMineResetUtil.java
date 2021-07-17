package me.untouchedodin0.privatemines.utils.mine;

import me.untouchedodin0.privatemines.PrivateMines;
import me.untouchedodin0.privatemines.utils.filling.MineFillManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.UUID;

public class PrivateMineResetUtil extends BukkitRunnable {

    private BukkitTask bukkitTask;
    MineFillManager mineFillManager;
    UUID playerId;
    File userFile;
    YamlConfiguration mineConfig;

    public PrivateMineResetUtil(PrivateMines privateMines) {
        mineFillManager = new MineFillManager(privateMines);
    }

    public void startResetTask(PrivateMines privateMines, UUID uuid, int minutesDelay) {
        this.playerId = uuid;

        if (bukkitTask == null && playerId != null) {
            Bukkit.getLogger().info("Starting reset task for UUID: " + playerId);
            bukkitTask = this.runTaskTimer(
                    privateMines,
                    0L,
                    minutesDelay * 60 * 20L); // Should fix the lag?
        }
    }

    @Override
    public synchronized void cancel() {
        if (bukkitTask != null) {
            bukkitTask.cancel();
        }
    }

    @Override
    public void run() {
        Bukkit.broadcastMessage("reset task, pog!");
        userFile = new File("plugins/PrivateMinesRewrite/mines/" + playerId + ".yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
        mineFillManager.fillPlayerMine(playerId);
    }
}
