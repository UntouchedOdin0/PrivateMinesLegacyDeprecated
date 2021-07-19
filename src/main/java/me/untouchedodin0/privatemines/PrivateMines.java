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
import me.untouchedodin0.privatemines.utils.Metrics;
import me.untouchedodin0.privatemines.utils.Util;
import me.untouchedodin0.privatemines.utils.filling.MineFillManager;
import me.untouchedodin0.privatemines.utils.mine.PrivateMineResetUtil;
import me.untouchedodin0.privatemines.utils.storage.MineStorage;
import me.untouchedodin0.privatemines.world.MineWorldManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import redempt.redlib.commandmanager.Messages;

import java.io.File;
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
    int minesCount;
    int resetDelay;
    private final int pluginId = 11413; // Metrics ID.


    File[] structuresList;
    File structureFolder = new File("plugins/PrivateMinesRewrite/schematics/");
    File structure;

//    File structure = new File("plugins/PrivateMinesRewrite/schematics/structure.dat");
    private PrivateMines privateMine;
    private PrivateMineResetUtil privateMineResetUtil;
    private MineFillManager fillManager;
    private MineWorldManager mineManager;
    private MineStorage mineStorage = new MineStorage();
    private MineFactory mineFactory;

    public static String fileNameWithOutExt(String fileName) {
        return Optional.of(fileName.lastIndexOf(".")).filter(i -> i >= 0)
                .map(i -> fileName.substring(0, i)).orElse(fileName);
    }

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("Loading PrivateMinesRewrite...");
        File minesFolder = new File(getDataFolder(), MINES_FOLDER_NAME);

        Util util = new Util();

        Bukkit.getLogger().info("Setting up the Private Mines World...");
        mineManager = new MineWorldManager();
        Bukkit.getLogger().info("Private Mines World has been setup!");
        fillManager = new MineFillManager(this);

        Bukkit.getLogger().info("Setting up the Private Mines Storage and Factory...");
        mineStorage = new MineStorage();
        privateMineResetUtil = new PrivateMineResetUtil(this);
        mineFactory = new MineFactory(
                this,
                mineStorage,
                mineManager,
                fillManager,
                privateMineResetUtil,
                util);

        Bukkit.getLogger().info("Private Mines storage and factory has been setup!");

        BukkitCommandManager manager = new PaperCommandManager(this);

        if (!minesFolder.exists()) {
            Bukkit.getLogger().info("Creating mines directory...");
            minesFolder.mkdir();
        }
        privateMine = this;
        Bukkit.getLogger().info("Loading structures...");
        structuresList = structureFolder.listFiles();

        for (File file : structuresList) {
            Bukkit.getLogger().info("Loading structure " + file.getName());
            util.loadStructure(file.getName(), file);
        }

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
                mineManager));
        Bukkit.getLogger().info("Command registered!");

        Bukkit.getLogger().info("Setting up the private mine util...");

        resetDelay = getConfig().getInt("resetDelay");

        Bukkit.getLogger().info("Starting the private mine reset task...");
        for (File file : mineStorage.getMineFiles()) {
            UUID uuid = UUID.fromString(fileNameWithOutExt(file.getName()));
            privateMineResetUtil.startResetTask(uuid, resetDelay);
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
}