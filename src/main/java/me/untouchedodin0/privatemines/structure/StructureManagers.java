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

package me.untouchedodin0.privatemines.structure;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import redempt.redlib.multiblock.MultiBlockStructure;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StructureManagers {

    InputStream inputStream;
    MultiBlockStructure multiBlockStructure;
    List<MultiBlockStructure> multiBlockStructures = new ArrayList<>();
    int[][] corners = new int[2][];
    int[] npcLocation = null; //new int[1];
    int[] spawnLocation = null; //new int[1];

    public void loadStructureData(File file) {
        try {
            inputStream = new FileInputStream(file);
            Bukkit.getLogger().info("Loading Structure " + file.getName());
            multiBlockStructure =
                    MultiBlockStructure
                            .create(inputStream,
                                    file.getName(),
                                    false,
                                    false);

            int[] dimensions = multiBlockStructure.getDimensions();
            int position = 0;
//            int npctest = npcLocation[1];

            for (int x = 0; x < dimensions[0]; x++) {
                for (int y = 0; y < dimensions[1]; y++) {
                    for (int z = 0; z < dimensions[2]; z++) {
                        if (multiBlockStructure.getType(x, y, z) == Material.POWERED_RAIL) {
                            corners[position] = new int[]{x, y, z};
                            position++;
                        } else if (multiBlockStructure.getType(x, y, z) == Material.WHITE_WOOL) {
                            npcLocation = new int[]{x, y, z};
                            continue;
                        } else if (multiBlockStructure.getType(x, y, z) == Material.CHEST) {
                            spawnLocation = new int[]{x, y, z};
//                            spawnLocation[position] = new int[]{x, y, z};
                        }
                    }
                }
            }
            multiBlockStructures.add(multiBlockStructure);
            Bukkit.getLogger().info("===============");
            Bukkit.getLogger().info("STRUCTURE MANAGERS DEBUG STUFF");
            Bukkit.getLogger().info("MultiBlockStructure: " + multiBlockStructure.getName());
            Bukkit.getLogger().info("inputStream: " + inputStream.toString());
            Bukkit.getLogger().info("dim: " + dimensions[0] + " " + dimensions[1] + " " + dimensions[2]);
            Bukkit.getLogger().info("corners[]: " + corners[0] + " " + corners[1]);
            Bukkit.getLogger().info("position: " + position);
            Bukkit.getLogger().info("npcLocation: " + npcLocation.toString());
            Bukkit.getLogger().info("spawnLocation: " + spawnLocation.toString());
            Bukkit.getLogger().info("===============");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int[][] getCorners() {
        return corners;
    }
}

