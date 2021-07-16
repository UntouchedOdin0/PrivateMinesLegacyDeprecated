package me.untouchedodin0.privatemines.utils.storage;

import org.bukkit.entity.Player;

import java.io.File;

public class MineStorage {

    File userFile;
    File[] mineFiles = new File("plugins/PrivateMinesRewrite/mines").listFiles();

    public boolean hasMine(Player player) {
        userFile = new File("plugins/PrivateMinesRewrite/mines/" + player.getUniqueId() + ".yml");
        return userFile.exists();
    }

    public File[] getMineFiles() {
        return mineFiles;
    }
}
