package me.untouchedodin0.privatemines.utils.mine;

import me.untouchedodin0.privatemines.PrivateMines;
import me.untouchedodin0.privatemines.utils.Util;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import redempt.redlib.multiblock.MultiBlockStructure;
import redempt.redlib.multiblock.Structure;

import java.io.File;

public class MineUpgradeUtil {

    MultiBlockStructure multiBlockStructure;
    Structure oldStructure;
    Structure upgradeStructure;
    File userFile;
    YamlConfiguration mineConfig;
    PrivateMines privateMines;
    Util util;

    //TODO Implement a working version of this.
    public void upgradeMine(Player player, File newMineFile, Location location, Util util) {
        this.util = util;

        player.sendMessage(newMineFile.toString());
        oldStructure = multiBlockStructure.assumeAt(location);
        if (oldStructure != null) {
            player.sendMessage("Found a structure: " + oldStructure);
            player.sendMessage("Structure Location: " + oldStructure.getLocation());
        }
    }
}
