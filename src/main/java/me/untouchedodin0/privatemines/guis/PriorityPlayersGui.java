package me.untouchedodin0.privatemines.guis;

import me.untouchedodin0.privatemines.utils.Util;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PriorityPlayersGui {

    File userFile;
    YamlConfiguration mineConfig;
    List<UUID> prioritylayers = new ArrayList<>();
    List<String> priorityPlayersNames = new ArrayList<>();
    ItemStack stack;
    Util util;

    private static final String MINE_DIRECTORY = "plugins/PrivateMinesRewrite/mines/";

    public void openPriorityPlayersMenu(Player player) {
        userFile = new File(MINE_DIRECTORY + player.getUniqueId() + ".yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);

        stack = Util.getPlayerSkull();

        if (mineConfig.getList("priorityPlayers").isEmpty()) {
            player.sendMessage(ChatColor.RED + "There were no priority players!");
        } else {
            for (String id : mineConfig.getStringList("priorityPlayers")) {
                prioritylayers.add(UUID.fromString(id));
                priorityPlayersNames.add(Bukkit.getPlayer(UUID.fromString(id)).getName());
            }
        }

        for (String str : priorityPlayersNames) {
            TextComponent message = new TextComponent("- " + str);
            message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org"));
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Visit the Spigot website!")));
            player.spigot().sendMessage(message);
        }
    }
}
