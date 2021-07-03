package me.untouchedodin0.privatemines;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.PaperCommandManager;
import me.untouchedodin0.privatemines.commands.PrivateMinesCommand;
import me.untouchedodin0.privatemines.structure.StructureManagers;
import me.untouchedodin0.privatemines.utils.Util;
import me.untouchedodin0.privatemines.utils.filling.MineFillManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import redempt.redlib.sql.SQLHelper;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;

public class PrivateMines extends JavaPlugin {

    public static final String SCHEMATICS_FILE_NAME = "schematics/schematics.yml";
    private static final YamlConfiguration schematicsConfig = new YamlConfiguration();
    Connection connection;
    SQLHelper sqlHelper;
    private PrivateMines privateMine;

    File structure = new File("plugins/PrivateMinesRewrite/schematics/structure.dat");

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
}
