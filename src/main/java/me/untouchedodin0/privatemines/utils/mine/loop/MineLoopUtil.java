/*
 * MIT License
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
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.untouchedodin0.privatemines.utils.mine.loop;

import org.bukkit.Material;
import redempt.redlib.multiblock.MultiBlockStructure;

public class MineLoopUtil {

    public int[][] findCornerLocations(MultiBlockStructure structure, Material cornerMaterial) {

        int[] dimensions = structure.getDimensions();
        int dimX = dimensions[0];
        int dimY = dimensions[1];
        int dimZ = dimensions[2];

        int[][] locations = new int[2][];
        int corners = 0;

        for (int x = 0; x < dimX; x++) {
            for (int y = 0; y < dimY; y++) {
                for (int z = 0; z < dimZ; z++) {
                    if (structure.getType(x, y, z) != cornerMaterial) {
                        continue;
                    }
                    locations[corners] = new int[]{x, y, z};
                    corners++;
                    if (corners >= 2) break;
                }
            }
        }
        return locations;
    }

    public int[] findLocation(MultiBlockStructure structure, Material material) {
        int[] dimensions = structure.getDimensions();
        int dimX = dimensions[0];
        int dimY = dimensions[1];
        int dimZ = dimensions[2];

        for (int x = 0; x < dimX; x++) {
            for (int y = 0; y < dimY; y++) {
                for (int z = 0; z < dimZ; z++) {
                    if (structure.getType(x, y, z) != material) {
                        continue;
                    }
                    return new int[]{x, y, z};
                }
            }
        }
        return new int[]{0, 0, 0};
    }
}
