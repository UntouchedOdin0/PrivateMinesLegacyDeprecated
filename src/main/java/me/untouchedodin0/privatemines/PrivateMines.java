package me.untouchedodin0.privatemines;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.PaperCommandManager;
import me.untouchedodin0.privatemines.commands.PrivateMinesCommand;
import me.untouchedodin0.privatemines.guis.MainMenuGui;
import me.untouchedodin0.privatemines.structure.StructureManagers;
import me.untouchedodin0.privatemines.utils.Util;
import me.untouchedodin0.privatemines.utils.filling.MineFillManager;
import me.untouchedodin0.privatemines.utils.storage.MineStorage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class PrivateMines extends JavaPlugin {

    public static final String SCHEMATICS_FILE_NAME = "schematics/schematics.yml";
    public static final String MINES_FOLDER_NAME = "mines";
    private static final YamlConfiguration schematicsConfig = new YamlConfiguration();

    private PrivateMines privateMine;
    private MineFillManager fillManager;

    File structure = new File("plugins/PrivateMinesRewrite/schematics/structure.dat");

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("Loading PrivateMinesRewrite...");
        File schematicsFile = new File(getDataFolder(), SCHEMATICS_FILE_NAME);
        File minesFolder = new File(getDataFolder(), MINES_FOLDER_NAME);

        Util util = new Util();
        MineStorage mineStorage = new MineStorage();
        fillManager = new MineFillManager(this);
        BukkitCommandManager manager = new PaperCommandManager(this);
        MainMenuGui mainMenuGui = new MainMenuGui(fillManager);

        manager.registerCommand(new PrivateMinesCommand(util, fillManager, this, mineStorage, mainMenuGui));
//        manager.registerCommand(new PrivateMinesCommand(util,this, mineStorage, mainMenuGui);
        if (!schematicsFile.exists()) {
            Bukkit.getLogger().info("Creating and loading schematics.yml...");
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

        if (!minesFolder.exists()) {
            Bukkit.getLogger().info("Creating mines directory...");
            minesFolder.mkdir();
        }
        privateMine = this;
        StructureManagers structureManagers = new StructureManagers();

        Bukkit.getLogger().info("Loading structures...");
        structureManagers.loadStructureData(structure);
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Disabling PrivateMinesRewrite...");
    }

    public PrivateMines getPrivateMines() {
        return privateMine;
    }
}
