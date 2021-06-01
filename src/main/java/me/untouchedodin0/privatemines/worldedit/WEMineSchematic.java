package me.untouchedodin0.privatemines.worldedit;

import me.untouchedodin0.privatemines.data.MineSchematic;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;

public class WEMineSchematic extends MineSchematic {

    protected WEMineSchematic(String name, List<String> description, File file, ItemStack icon, Integer tier, Integer resetDelay) {
        super(name, description, file, icon, tier, resetDelay);
    }
}
