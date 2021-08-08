/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021 Kyle Hicks
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.untouchedodin0.privatemines;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.PaperCommandManager;
import me.untouchedodin0.privatemines.commands.PrivateMinesCommand;
import me.untouchedodin0.privatemines.factory.MineFactory;
import me.untouchedodin0.privatemines.structure.StructureLoader;
import me.untouchedodin0.privatemines.structure.StructureManagers;
import me.untouchedodin0.privatemines.utils.Metrics;
import me.untouchedodin0.privatemines.utils.Util;
import me.untouchedodin0.privatemines.utils.filling.MineFillManager;
import me.untouchedodin0.privatemines.utils.mine.Mine;
import me.untouchedodin0.privatemines.utils.mine.MineType;
import me.untouchedodin0.privatemines.utils.mine.loop.MineLoopUtil;
import me.untouchedodin0.privatemines.utils.mine.util.MineUpgradeUtil;
import me.untouchedodin0.privatemines.utils.mine.util.PrivateMineResetUtil;
import me.untouchedodin0.privatemines.utils.storage.MineStorage;
import me.untouchedodin0.privatemines.world.MineWorldManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import redempt.redlib.commandmanager.Messages;
import redempt.redlib.multiblock.MultiBlockStructure;

import java.io.File;
import java.util.*;

/*
    Thanks to Redempt for creating RedLib what helped me with a lot of the
    creation of this plugin

    RedLib: https://github.com/Redempt/RedLib
    Redempt: https://github.com/Redempt
 */

public class PrivateMines extends JavaPlugin {

    public static final String MINES_FOLDER_NAME = "mines";

    int minesCount;
    int resetDelay;
    File[] structuresList;
    File structureFolder = new File("plugins/PrivateMinesRewrite/structures/");
    MultiBlockStructure multiBlockStructure;

    List<MineType> mineTypes = new ArrayList<>();
    List<Mine> mines = new ArrayList<>();

    List<MultiBlockStructure> multiBlockStructures = new ArrayList<>();

    Map<String, MineType> mineTypeMap = new HashMap<>();

    private PrivateMines privateMine;
    private MineWorldManager mineManager;
    private StructureManagers structureManagers;
    private StructureLoader structureLoader;
    private MineUpgradeUtil mineUpgradeUtil;
    private YamlConfiguration schematicsYml;

    public static String fileNameWithOutExt(String fileName) {
        return Optional.of(fileName.lastIndexOf(".")).filter(i -> i >= 0)
                .map(i -> fileName.substring(0, i)).orElse(fileName);
    }

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("Loading PrivateMinesRewrite...");
        File minesFolder = new File(getDataFolder(), MINES_FOLDER_NAME);

        Util util = new Util();
        saveDefaultConfig();
        saveResource("messages.txt", false);
        saveResource("structures", false);

        Bukkit.getLogger().info("Setting up the Private Mines World...");
        mineManager = new MineWorldManager();
        Bukkit.getLogger().info("Private Mines World has been setup!");
        MineFillManager fillManager = new MineFillManager(this);

        Bukkit.getLogger().info("Setting up the Private Mines Storage and Factory...");
        MineStorage mineStorage = new MineStorage();
        PrivateMineResetUtil privateMineResetUtil = new PrivateMineResetUtil(this);
        MineLoopUtil mineLoopUtil = new MineLoopUtil();
        structureLoader = new StructureLoader(mineLoopUtil);
        MineFactory mineFactory = new MineFactory(
                this,
                mineStorage,
                fillManager,
                privateMineResetUtil,
                mineLoopUtil,
                util,
                structureLoader);

        mineUpgradeUtil = new MineUpgradeUtil();
        structureManagers = new StructureManagers();

        BukkitCommandManager manager = new PaperCommandManager(this);

        if (!minesFolder.exists()) {
            Bukkit.getLogger().info("Creating mines directory...");
            boolean createdMinesDirectorySuccessfully = minesFolder.mkdir();
            if (createdMinesDirectorySuccessfully) {
                Bukkit.getLogger().info("The mines directory was successfully created!");
            }
        }

        if (!structureFolder.exists()) {
            boolean createdStructureFolderSuccessfully = structureFolder.mkdir();
            if (createdStructureFolderSuccessfully) {
                Bukkit.getLogger().info("The structures directory was successfully created!");
            }
        }

        privateMine = this;
        structuresList = structureFolder.listFiles();

        if (structuresList != null) {
            for (File file : structuresList) {
                String name = file.getName().replace(".dat", "");
                multiBlockStructure = util.loadStructure(file.getName(), file);
                MineType mineType = new MineType(name, multiBlockStructure, this);
                mineTypeMap.putIfAbsent(name, mineType);
                multiBlockStructures.add(multiBlockStructure); // a fix?
            }
        }

        mineTypeMap.forEach(((name, mineType) -> {
            Bukkit.getLogger().info("FOREACH: Name: " + name + " Type: " + mineType);
        }));

        int test = 0;
        for (MultiBlockStructure structure : multiBlockStructures) {
            structureLoader.loadStructure(structure);
            test++;
            MineType mineType = new MineType(String.valueOf(test), structure, this);

//            cornerLocations = mineLoopUtil.findCornerLocations(structure, cornerMaterial);
//            spawnLocation = mineLoopUtil.findSpawnPointLocation(structure, spawnMaterial);
//            npcLocation = mineLoopUtil.findNpcLocation(structure, npcMaterial);
//
//            mineType.setCornerLocations(cornerLocations);
//            mineType.setSpawnLocation(spawnLocation);
//            mineType.setNpcLocation(npcLocation);

            mineTypes.add(mineType);
            Bukkit.getLogger().info("MINETYPE: " + mineType);
        }

        for (MineType type : mineTypes) {
//            Mine mine = new Mine(type);
            Bukkit.getLogger().info("Type: " + type);
            Bukkit.getLogger().info("Structure: " + type.getStructure());
//            Bukkit.getLogger().info("Spawn Location: " + mine.getSpawnLocation());
//            Bukkit.getLogger().info("Corner Locations: " + mine.getCornerLocations());
//            Bukkit.getLogger().info("NPC Location: " + mine.getNpcLocation());
//            mines.add(mine);
            mineTypeMap.putIfAbsent(type.getStructureName(), type);
        }

        Bukkit.getLogger().info("Loading mines...");
        if (!minesFolder.exists()) {
            minesFolder.mkdir();
        } else {
            minesCount = minesFolder.list().length;
        }

        Bukkit.getLogger().info(String.format("Found a total of %d mines!", minesCount));

        Bukkit.getLogger().info("Registering the command...");
        manager.registerCommand(new PrivateMinesCommand(
                util,
                fillManager,
                this,
                mineStorage,
                mineFactory));

        Bukkit.getLogger().info("Command registered!");

        Bukkit.getLogger().info("Setting up the private mine util...");

        resetDelay = getConfig().getInt("resetDelay");

        Bukkit.getLogger().info("Starting the private mine reset task...");

        if (!mineStorage.getMineFolder().exists()) {
            mineStorage.getMineFolder().mkdir();
        }

        if (mineStorage.getMineFiles() == null) {
            Bukkit.getLogger().info("No mine files to load!");
        } else {
            for (File file : mineStorage.getMineFiles()) {
                UUID uuid = UUID.fromString(fileNameWithOutExt(file.getName()));
                if (getConfig().getBoolean("autoResets")) {
                    privateMineResetUtil.startResetTask(uuid, resetDelay);
                } else {
                    return;
                }
            }
        }

        Bukkit.getLogger().info("Loading messages...");
        Messages.load(this);
        Bukkit.getLogger().info("Loaded messages!");

        // Metrics ID.
        int pluginId = 11413;
        Metrics metrics = new Metrics(this, pluginId);
        Bukkit.getLogger().info("Loaded metrics!");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Disabling PrivateMinesRewrite v" + getDescription().getVersion());
    }

    public PrivateMines getPrivateMines() {
        return privateMine;
    }

    public String getStructureFileName() {
        return getConfig().getString("structureFile");
    }

    public MineWorldManager getMineWorldManagerManager() {
        return mineManager;
    }

    public StructureLoader getStructureLoader() {
        return structureLoader;
    }

    public List<MineType> getMineTypes() {
        return mineTypes;
    }

    public Map<String, MineType> getMineTypeMap() {
        return mineTypeMap;
    }
}