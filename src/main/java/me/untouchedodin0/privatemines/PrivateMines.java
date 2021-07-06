package me.untouchedodin0.privatemines;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.PaperCommandManager;
import me.untouchedodin0.privatemines.commands.PrivateMinesCommand;
import me.untouchedodin0.privatemines.factory.MineFactory;
import me.untouchedodin0.privatemines.guis.MainMenuGui;
import me.untouchedodin0.privatemines.structure.StructureManagers;
import me.untouchedodin0.privatemines.utils.Util;
import me.untouchedodin0.privatemines.utils.filling.MineFillManager;
import me.untouchedodin0.privatemines.utils.storage.MineStorage;
import me.untouchedodin0.privatemines.world.MineWorldManager;
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
    int minesCount;
    File structure = new File("plugins/PrivateMinesRewrite/schematics/structure.dat");
    private PrivateMines privateMine;
    private MineFillManager fillManager;
    private MineWorldManager mineManager;
    private MineStorage mineStorage = new MineStorage();
    private MineFactory mineFactory = new MineFactory(mineStorage, mineManager, fillManager);

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("Loading PrivateMinesRewrite...");
        File schematicsFile = new File(getDataFolder(), SCHEMATICS_FILE_NAME);
        File minesFolder = new File(getDataFolder(), MINES_FOLDER_NAME);

        Util util = new Util();

        Bukkit.getLogger().info("Setting up the Private Mines World...");
        mineManager = new MineWorldManager();
        Bukkit.getLogger().info("Private Mines World has been setup!");
        fillManager = new MineFillManager(this);

        Bukkit.getLogger().info("Setting up the Private Mines Storage and Factory...");
        mineStorage = new MineStorage();
        mineFactory = new MineFactory(mineStorage, mineManager, fillManager);
        Bukkit.getLogger().info("Private Mines storage and factory has been setup!");

        BukkitCommandManager manager = new PaperCommandManager(this);
        MainMenuGui mainMenuGui = new MainMenuGui(fillManager);

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

        Bukkit.getLogger().info("Loading mines...");
        minesCount = minesFolder.list().length;
        Bukkit.getLogger().info(String.format("Found a total of %d mines!", minesCount));

        Bukkit.getLogger().info("Registering the command...");

        manager.registerCommand(new PrivateMinesCommand(
                util,
                fillManager,
                this,
                mineStorage,
                mineFactory,
                mainMenuGui,
                mineManager));
        Bukkit.getLogger().info("Command registered!");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Disabling PrivateMinesRewrite...");
    }

    public PrivateMines getPrivateMines() {
        return privateMine;
    }
}
