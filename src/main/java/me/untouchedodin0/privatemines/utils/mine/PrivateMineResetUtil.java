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

package me.untouchedodin0.privatemines.utils.mine;

import me.untouchedodin0.privatemines.PrivateMines;
import me.untouchedodin0.privatemines.utils.filling.MineFillManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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
    Location teleportLocation;

    public PrivateMineResetUtil(PrivateMines privateMines) {
        mineFillManager = new MineFillManager(privateMines);
    }

    public void startResetTask(UUID uuid, int minutesDelay) {
        this.playerId = uuid;
        userFile = new File("plugins/PrivateMinesRewrite/mines/" + playerId + ".yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
        teleportLocation = mineConfig.getLocation("spawnLocation");

        Task.syncRepeating(() -> {
            if (Bukkit.getPlayer(uuid) == null) {
                Bukkit.getLogger().info("Couldn't start task due to the player not being online yet, trying again in 60s!");
                Task.syncDelayed((() ->
                        startResetTask(uuid, minutesDelay)), 60 * 20L);
            } else {
                player.sendMessage(ChatColor.GREEN + "Resetting your private mine!");
                mineFillManager.fillPlayerMine(playerId);
                if (teleportLocation != null) {
                    player.teleport(teleportLocation);
                    player.sendMessage(ChatColor.GREEN + "You've been teleported to your mine!");
                }
            }
        }, 0L, 20L * 60 * minutesDelay);
    }
}
