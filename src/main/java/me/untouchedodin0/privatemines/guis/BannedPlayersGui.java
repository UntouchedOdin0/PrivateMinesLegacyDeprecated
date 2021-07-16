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

public class BannedPlayersGui {

    File userFile;
    YamlConfiguration mineConfig;
    List<UUID> bannedPlayers = new ArrayList<>();
    List<String> bannedPlayersNames = new ArrayList<>();

    private static final String MINE_DIRECTORY = "plugins/PrivateMinesRewrite/mines/";

    public void openBannedPlayersMenu(Player player) {
        userFile = new File(MINE_DIRECTORY + player.getUniqueId() + ".yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);

        if (mineConfig.getList("whitelistedPlayers").isEmpty()) {
            player.sendMessage(ChatColor.RED + "There were no whitelisted players!");
        } else {
            for (String id : mineConfig.getStringList("whitelistedPlayers")) {
                bannedPlayers.add(UUID.fromString(id));
                bannedPlayersNames.add(Bukkit.getPlayer(UUID.fromString(id)).getName());
            }
        }

        for (String str : bannedPlayersNames) {
            TextComponent message = new TextComponent("- " + str);
            message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org"));
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Visit the Spigot website!")));
            player.spigot().sendMessage(message);
        }
    }
}
