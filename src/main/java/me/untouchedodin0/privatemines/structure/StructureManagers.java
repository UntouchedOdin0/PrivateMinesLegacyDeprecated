package me.untouchedodin0.privatemines.structure;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import redempt.redlib.multiblock.MultiBlockStructure;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class StructureManagers {

    InputStream inputStream;
    MultiBlockStructure multiBlockStructure;
    List<MultiBlockStructure> multiBlockStructures = new ArrayList<>();
    int[][] corners = new int[2][];

    public void loadStructureData(File file) {
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (inputStream != null) {
            Bukkit.getLogger().info("Loading Structure " + file.getName());
            multiBlockStructure =
                    MultiBlockStructure
                            .create(inputStream,
                                    file.getName(),
                                    false,
                                    false);

            int[] dim = multiBlockStructure.getDimensions();
            int pos = 0;

            for (int x = 0; x < dim[0]; x++) {
                for (int y = 0; y < dim[1]; y++) {
                    for (int z = 0; z < dim[2]; z++) {
                        if (multiBlockStructure.getType(x, y, z) != Material.POWERED_RAIL) {
                            continue;
                        }
                        corners[pos] = new int[]{x, y, z};
                    }
                }
            }
            multiBlockStructures.add(multiBlockStructure);
        }
    }

    public int[][] getCorners() {
        return corners;
    }
}

