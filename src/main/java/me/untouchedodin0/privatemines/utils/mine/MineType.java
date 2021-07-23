package me.untouchedodin0.privatemines.utils.mine;

import org.bukkit.Material;
import redempt.redlib.configmanager.ConfigManager;
import redempt.redlib.multiblock.MultiBlockStructure;

import java.io.File;
import java.util.Map;

public class MineType {

    private MultiBlockStructure multiBlockStructure;
    private final String type;
    private final Map<Material, Double> materials;
    private final File file;

    public MineType(MultiBlockStructure multiBlockStructure, String mineType, File file) {
        this.multiBlockStructure = multiBlockStructure;
        this.type = mineType;
        this.materials = ConfigManager.map(Material.class, Double.class);
        this.file = file;
    }

    public MultiBlockStructure getMultiBlockStructure() {
        return multiBlockStructure;
    }

    public void setMultiBlockStructure(MultiBlockStructure multiBlockStructure) {
        this.multiBlockStructure = multiBlockStructure;
    }

    public String getMineType() {
        return type;
    }

    public Map<Material, Double> getMaterials() {
        return materials;
    }

    public File getFile() {
        return file;
    }
}
