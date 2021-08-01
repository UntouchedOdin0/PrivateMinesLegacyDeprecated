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
import com.cryptomorin.xseries.XMaterial;
import me.untouchedodin0.privatemines.commands.PrivateMinesCommand;
import me.untouchedodin0.privatemines.factory.MineFactory;
import me.untouchedodin0.privatemines.structure.StructureLoader;
import me.untouchedodin0.privatemines.structure.StructureManagers;
import me.untouchedodin0.privatemines.utils.Metrics;
import me.untouchedodin0.privatemines.utils.Util;
import me.untouchedodin0.privatemines.utils.filling.MineFillManager;
import me.untouchedodin0.privatemines.utils.mine.MineType;
import me.untouchedodin0.privatemines.utils.mine.MineUpgradeUtil;
import me.untouchedodin0.privatemines.utils.mine.PrivateMineResetUtil;
import me.untouchedodin0.privatemines.utils.mine.loop.MineLoopUtil;
import me.untouchedodin0.privatemines.utils.mine.paste.PasteBuilder;
import me.untouchedodin0.privatemines.utils.storage.MineHandler;
import me.untouchedodin0.privatemines.utils.storage.MineStorage;
import me.untouchedodin0.privatemines.world.MineWorldManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import redempt.redlib.commandmanager.Messages;
import redempt.redlib.multiblock.MultiBlockStructure;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/*
    Thanks to Redempt for creating RedLib what helped me with a lot of the
    creation of this plugin

    RedLib: https://github.com/Redempt/RedLib
    Redempt: https://github.com/Redempt
 */

public class PrivateMines extends JavaPlugin {

    public static final String MINES_FOLDER_NAME = "mines";
    private final int pluginId = 11413; // Metrics ID.
    int minesCount;
    int resetDelay;
    File[] structuresList;
    File structureFolder = new File("plugins/PrivateMinesRewrite/schematics/");
    File schematicsFile = new File("plugins/PrivateMinesRewrite/schematics/schematics.yml");
    MultiBlockStructure multiBlockStructure;

    List<MineType> mineTypes = new ArrayList<>();
    List<MultiBlockStructure> multiBlockStructures = new ArrayList<>();

    private PrivateMines privateMine;
    private MineWorldManager mineManager;
    private StructureManagers structureManagers;
    private StructureLoader structureLoader;
    private MineUpgradeUtil mineUpgradeUtil;
    private YamlConfiguration schematicsYml;

    private Material cornerMaterial = XMaterial.POWERED_RAIL.parseMaterial();
    private Material npcMaterial = XMaterial.WHITE_WOOL.parseMaterial();
    private Material spawnMaterial = XMaterial.CHEST.parseMaterial();
    private Material expandMaterial = XMaterial.SPONGE.parseMaterial();

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
        saveResource("schematics", false);

        Bukkit.getLogger().info("Setting up the Private Mines World...");
        mineManager = new MineWorldManager();
        Bukkit.getLogger().info("Private Mines World has been setup!");
        MineFillManager fillManager = new MineFillManager(this);

        Bukkit.getLogger().info("Setting up the Private Mines Storage and Factory...");
        MineStorage mineStorage = new MineStorage();
        PrivateMineResetUtil privateMineResetUtil = new PrivateMineResetUtil(this);
        PasteBuilder pasteBuilder = new PasteBuilder();
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
                MineType mineType = new MineType(name);
                multiBlockStructure = util.loadStructure(file.getName(), file);
                util.saveToStructureMap(name, multiBlockStructure);
                mineType.setFile(file);
                mineType.setMultiBlockStructure(multiBlockStructure);
                mineTypes.add(mineType);
                multiBlockStructures.add(multiBlockStructure);
            }
        }

        for (MineType mineType : mineTypes) {
            MineHandler.createMineType(mineType);
        }

        List<MineType> types = MineHandler.getMineTypes();

        for (MineType type : types) {
            structureManagers.loadStructureData(type.getFile());
        }

        for (MultiBlockStructure structure : multiBlockStructures) {
            structureLoader.loadStructure(structure);
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
                privateMineResetUtil.startResetTask(uuid, resetDelay);
            }
        }

        Bukkit.getLogger().info("Loading messages...");
        Messages.load(this);
        Bukkit.getLogger().info("Loaded messages!");

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
}