package me.untouchedodin0.privatemines.guis;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PriorityPlayersGui {

    File userFile;
    YamlConfiguration mineConfig;
    List<UUID> prioritylayers = new ArrayList<>();
    List<String> priorityPlayersNames = new ArrayList<>();

    private static final String MINE_DIRECTORY = "plugins/PrivateMinesRewrite/mines/";

    public void openPriorityPlayersMenu(Player player) {
        userFile = new File(MINE_DIRECTORY + player.getUniqueId() + ".yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);

        if (mineConfig.getList("priorityPlayers").isEmpty()) {
            player.sendMessage(ChatColor.RED + "There were no priority players!");
        } else {
            for (String id : mineConfig.getStringList("priorityPlayers")) {
                prioritylayers.add(UUID.fromString(id));
                priorityPlayersNames.add(Bukkit.getPlayer(UUID.fromString(id)).getName());
            }
        }

        for (String str : priorityPlayersNames) {
            TextComponent message = new TextComponent(ChatColor.GRAY + "- " + ChatColor.GOLD + str);
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.AQUA + "Click to unprioritize " + str)));
            message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pmine priority " + str));
            player.spigot().sendMessage(message);
        }
    }
}
