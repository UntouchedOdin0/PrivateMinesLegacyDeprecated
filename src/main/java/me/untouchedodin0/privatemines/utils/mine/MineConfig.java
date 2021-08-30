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

package me.untouchedodin0.privatemines.utils.mine;

import org.bukkit.Material;
import redempt.redlib.configmanager.ConfigManager;
import redempt.redlib.configmanager.annotations.ConfigMappable;
import redempt.redlib.configmanager.annotations.ConfigValue;

import java.util.Map;

/*
    This class handles the mineTypes part of the config.
    Credit to Redempt for making such an amazing system!
 */

@ConfigMappable
public class MineConfig {

    @ConfigValue
    private String file; // reads "file" from the category.

    @ConfigValue
    private int priority; // reads "priority" from the category.

    @ConfigValue
    private Map<Material, Double> materials = ConfigManager.map(Material.class, Double.class); // reads "materials" from the category.

    /*
        This method gives easy access to the "file" value in the minetype in the config.
     */

    public String getFile() {
        return file;
    }

    /*
        This method gives easy access to the "priority" value in the minetype in the config.
    */

    public int getPriority() {
        return priority;
    }

    /*
        This method gives easy access to the "materials" list in the minetype in the config.
    */

    public Map<Material, Double> getMaterials() {
        return materials;
    }
}
