package me.untouchedodin0.privatemines;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.PaperCommandManager;
import me.untouchedodin0.privatemines.commands.PrivateMinesCommand;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class PrivateMines extends JavaPlugin{

    public static final String SCHEMATICS_FILE_NAME = "schematics/schematics.yml";
    private static final YamlConfiguration schematicsConfig = new YamlConfiguration();

    @Override
    public void onEnable() {
        File schematicsFile = new File(getDataFolder(), SCHEMATICS_FILE_NAME);

        BukkitCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new PrivateMinesCommand());
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
    }
}
