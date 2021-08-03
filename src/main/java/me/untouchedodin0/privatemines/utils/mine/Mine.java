package me.untouchedodin0.privatemines.utils.mine;

import com.cryptomorin.xseries.XMaterial;
import me.untouchedodin0.privatemines.utils.mine.loop.MineLoopUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import redempt.redlib.multiblock.MultiBlockStructure;

import java.util.UUID;

public class Mine {

    Location mineLocation;
    UUID mineOwner;
    MineType type;

    MultiBlockStructure structure;
    MineLoopUtil mineLoopUtil;

    private final Material cornerMaterial = XMaterial.POWERED_RAIL.parseMaterial();
    private final Material npcMaterial = XMaterial.WHITE_WOOL.parseMaterial();
    private final Material spawnMaterial = XMaterial.CHEST.parseMaterial();
    private final Material expandMaterial = XMaterial.SPONGE.parseMaterial();

    int[][] cornerLocations;
    int[] spawnLocation;
    int[] npcLocation;

    public Mine(MineType mineType) {
        this.mineLoopUtil = new MineLoopUtil();
        this.type = mineType;
        this.structure = type.getStructure();

        this.cornerLocations = mineLoopUtil.findCornerLocations(structure, cornerMaterial);
        this.spawnLocation = mineLoopUtil.findSpawnPointLocation(structure, spawnMaterial);
        this.npcLocation = mineLoopUtil.findNpcLocation(structure, npcMaterial);

        type.setType(mineType);
    }

    public void setMineLocation(Location mineLocation) {
        this.mineLocation = mineLocation;
    }

    public void setMineOwner(UUID mineOwner) {
        this.mineOwner = mineOwner;
    }
}
