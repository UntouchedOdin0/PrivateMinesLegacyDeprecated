package me.untouchedodin0.privatemines.guis;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WhitelistedPlayersGui {

    File userFile;
    YamlConfiguration mineConfig;
    List<UUID> whitelistedPlayers = new ArrayList<>();

    private static final String MINE_DIRECTORY = "plugins/PrivateMinesRewrite/mines/";

    public void openWhitelistedPlayersMenu(Player player) {
        userFile = new File(MINE_DIRECTORY + player.getUniqueId() + ".yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);

        if (mineConfig.getList("whitelistedPlayers").isEmpty()) {
            player.sendMessage(ChatColor.RED + "There were no whitelisted players!");
        } else {
            for (String id : mineConfig.getStringList("whitelistedPlayers")) {
                whitelistedPlayers.add(UUID.fromString(id));
            }
        }

        player.sendMessage(whitelistedPlayers.toString());
    }
}
