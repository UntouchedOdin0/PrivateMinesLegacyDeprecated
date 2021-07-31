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

package me.untouchedodin0.privatemines.guis;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PriorityPlayersGui {

    File userFile;
    YamlConfiguration mineConfig;
    List<UUID> prioritylayers = new ArrayList<>();
    List<String> priorityPlayersNames = new ArrayList<>();
    List<?> priorityList = new ArrayList<>();

    private static final String MINE_DIRECTORY = "plugins/PrivateMinesRewrite/mines/";

    public void openPriorityPlayersMenu(Player player) {
        userFile = new File(MINE_DIRECTORY + player.getUniqueId() + ".yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
        priorityList = mineConfig.getList("priorityPlayers");

        if (priorityList != null) {
            if (priorityList.isEmpty()) {
                player.sendMessage(ChatColor.RED + "There were no priority players!");
            } else {
                for (String id : mineConfig.getStringList("priorityPlayers")) {
                    prioritylayers.add(UUID.fromString(id));
                    priorityPlayersNames.add(Objects.requireNonNull(Bukkit.getPlayer(UUID.fromString(id))).getName());
                }
            }
        }

        for (String str : priorityPlayersNames) {
            TextComponent message = new TextComponent(ChatColor.GRAY + "- " + ChatColor.GOLD + str);
//            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.AQUA + "Click to unprioritize " + str)));
            message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pmine priority " + str));
            player.spigot().sendMessage(message);
        }
    }
}
