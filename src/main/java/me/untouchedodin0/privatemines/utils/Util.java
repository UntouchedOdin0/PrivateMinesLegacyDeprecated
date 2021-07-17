package me.untouchedodin0.privatemines.utils;

import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.multiblock.MultiBlockStructure;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Util {

    MultiBlockStructure multiBlockStructure;
    File[] mines = new File("plugins/PrivateMinesRewrite/mines/").listFiles();

    /*
    holy fuck it's empty here, i'll put more stuff later
     */

    // stops it from saying the class is empty.

    public static float getYaw(BlockFace blockFace) {
        switch (blockFace) {
            case NORTH:
                return 180f;
            case EAST:
                return -90f;
            case SOUTH:
                return -180f;
            case WEST:
                return 90f;
            default:
                return 0f;
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public void loadStructure(String structureName,
                              File file) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (inputStream == null) {
            Bukkit.broadcastMessage("Failed to load structure," +
                    " inputStream was null!");
        }
        multiBlockStructure = MultiBlockStructure.create(inputStream, structureName);
        Bukkit.getLogger().info("loadStructureMultiBlockStructure: " + multiBlockStructure.getName());
    }

    public MultiBlockStructure getMultiBlockStructure() {
        if (multiBlockStructure == null) {
            Bukkit.getLogger().info("Failed to load structure" +
                    " due to not existing!");
            return null;
        }
        return multiBlockStructure;
    }

    public static ItemStack getPlayerSkull() {
        // Got this base64 string from minecraft-heads.com
        String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L" +
                "3RleHR1cmUvNTIyODRlMTMyYmZkNjU5YmM2YWRhNDk3YzRmYTMwOTRjZDkzMjMxYTZiNTA1YTEyY2U3Y2Q1MTM1YmE4ZmY5MyJ9fX0=";

        return SkullCreator.itemFromBase64(base64);
    }
}
