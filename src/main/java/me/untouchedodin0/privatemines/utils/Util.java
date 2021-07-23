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
import java.util.HashMap;
import java.util.Map;

public class Util {

    MultiBlockStructure multiBlockStructure;
    File[] mines = new File("plugins/PrivateMinesRewrite/mines/").listFiles();
    Map<String, MultiBlockStructure> structureMap = new HashMap<>();

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

    public MultiBlockStructure loadStructure(String structureName,
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
        return multiBlockStructure;
    }

    public MultiBlockStructure getMultiBlockStructure() {
        if (multiBlockStructure == null) {
            Bukkit.getLogger().info("Failed to load structure" +
                    " due to not existing!");
            return null;
        }
        return multiBlockStructure;
    }

    public void saveToStructureMap(String structureName, MultiBlockStructure multiBlockStructure) {
        structureMap.putIfAbsent(structureName, multiBlockStructure);
    }

    public Map<String, MultiBlockStructure> getStructureMap() {
        return structureMap;
    }

    public static ItemStack getPlayerSkull() {
        // Got this base64 string from minecraft-heads.com
        String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L" +
                "3RleHR1cmUvNTIyODRlMTMyYmZkNjU5YmM2YWRhNDk3YzRmYTMwOTRjZDkzMjMxYTZiNTA1YTEyY2U3Y2Q1MTM1YmE4ZmY5MyJ9fX0=";

        return SkullCreator.itemFromBase64(base64);
    }
}
