package me.untouchedodin0.privatemines.utils.mine;

import me.untouchedodin0.privatemines.PrivateMines;
import me.untouchedodin0.privatemines.utils.filling.MineFillManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import redempt.redlib.misc.Task;

import java.io.File;
import java.util.UUID;

public class PrivateMineResetUtil {

    MineFillManager mineFillManager;
    UUID playerId;
    File userFile;
    YamlConfiguration mineConfig;
    Player player;

    public PrivateMineResetUtil(PrivateMines privateMines) {
        mineFillManager = new MineFillManager(privateMines);
    }

    public void startResetTask(UUID uuid, int minutesDelay) {
        this.playerId = uuid;

        Task.syncRepeating(new Runnable() {

            /**
             * When an object implementing interface <code>Runnable</code> is used
             * to create a thread, starting the thread causes the object's
             * <code>run</code> method to be called in that separately executing
             * thread.
             * <p>
             * The general contract of the method <code>run</code> is that it may
             * take any action whatsoever.
             *
             * @see Thread#run()
             */

            @Override
            public void run() {
                player = Bukkit.getPlayer(playerId);
                if (player != null) {
                    player.sendMessage(ChatColor.GREEN + "Resetting your private mine!");
                }
                userFile = new File("plugins/PrivateMinesRewrite/mines/" + playerId + ".yml");
                mineConfig = YamlConfiguration.loadConfiguration(userFile);
                mineFillManager.fillPlayerMine(playerId);
            }
        }, 0L, 20L * 60 * minutesDelay);
    }
}
