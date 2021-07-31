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
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
//import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class WhitelistedPlayersGui {

    private static final String MINE_DIRECTORY = "plugins/PrivateMinesRewrite/mines/";
    File userFile;
    YamlConfiguration mineConfig;
    List<?> whitelistedList = new ArrayList<>();
    List<UUID> whitelistedPlayers = new ArrayList<>();
    List<String> whitelistedPlayersNames = new ArrayList<>();

    public void openWhitelistedPlayersMenu(Player player) {
        userFile = new File(MINE_DIRECTORY + player.getUniqueId() + ".yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
        whitelistedList = mineConfig.getList("whitelistedPlayers");

        if (whitelistedPlayers != null) {
            if (whitelistedPlayers.isEmpty()) {
                player.sendMessage(ChatColor.RED + "There were no whitelisted players!");
            } else {
                for (String id : mineConfig.getStringList("whitelistedPlayers")) {
                    whitelistedPlayers.add(UUID.fromString(id));
                    whitelistedPlayersNames.add(Objects.requireNonNull(Bukkit.getPlayer(UUID.fromString(id))).getName());
                }
            }
        }

        for (String str : whitelistedPlayersNames) {
            TextComponent message = new TextComponent(ChatColor.GRAY + "- " + ChatColor.GOLD + str);
//            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.AQUA + "Click to un-whitelist " + str)));
            message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pmine unwhitelist " + str));
            player.spigot().sendMessage(message);
        }
    }
}
