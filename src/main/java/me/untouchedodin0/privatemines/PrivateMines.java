package me.untouchedodin0.privatemines;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.PaperCommandManager;
import me.untouchedodin0.privatemines.commands.PrivateMinesCommand;
import me.untouchedodin0.privatemines.utils.Util;
import me.untouchedodin0.privatemines.utils.filling.MineFillManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import redempt.redlib.blockdata.BlockDataManager;

import java.io.File;
import java.io.IOException;

public class PrivateMines extends JavaPlugin{

    public static final String SCHEMATICS_FILE_NAME = "schematics/schematics.yml";
    private static final YamlConfiguration schematicsConfig = new YamlConfiguration();
    private PrivateMines privateMine;

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("Loading PrivateMinesRewrite...");
        File schematicsFile = new File(getDataFolder(), SCHEMATICS_FILE_NAME);
        Util util = new Util();
        MineFillManager mineFillManager = new MineFillManager();
        BukkitCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new PrivateMinesCommand(util, mineFillManager, this));
        if (!schematicsFile.exists()) {
            saveResource(SCHEMATICS_FILE_NAME, false);
            try {
                schematicsConfig.load(SCHEMATICS_FILE_NAME);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        } else {
            try {
                schematicsConfig.load(SCHEMATICS_FILE_NAME);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
        privateMine = this;
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Disabling PrivateMinesRewrite...");
    }

    public PrivateMines getPrivateMines() {
        return privateMine;
    }
}
