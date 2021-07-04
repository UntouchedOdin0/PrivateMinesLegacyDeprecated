package me.untouchedodin0.privatemines;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.PaperCommandManager;
import me.untouchedodin0.privatemines.commands.PrivateMinesCommand;
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
    Connection connection;
    SQLHelper sqlHelper;
    private PrivateMines privateMine;

    File structure = new File("plugins/PrivateMinesRewrite/schematics/structure.dat");

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("Loading PrivateMinesRewrite...");
        File schematicsFile = new File(getDataFolder(), SCHEMATICS_FILE_NAME);
        File minesFolder = new File(getDataFolder(), MINES_FOLDER_NAME);

        Util util = new Util();
        MineFillManager mineFillManager = new MineFillManager();
        MineStorage mineStorage = new MineStorage();
        BukkitCommandManager manager = new PaperCommandManager(this);

        manager.registerCommand(new PrivateMinesCommand(util, mineFillManager, this, mineStorage));
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

        /*
        Bukkit.getLogger().info("Loading the SQL database...");
        connection = SQLHelper.openSQLite(privateMine.getDataFolder().toPath().resolve("mines.db"));
        Bukkit.getLogger().info("Wrapping the SQL database with a wrapper...");
        sqlHelper = new SQLHelper(connection);
        sqlHelper.execute("CREATE TABLE IF NOT EXISTS privatemines");
         */
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Disabling PrivateMinesRewrite...");
    }

    public PrivateMines getPrivateMines() {
        return privateMine;
    }

    public YamlConfiguration getMinesConfig() {
        return mineConfig;
    }
}
