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

package me.untouchedodin0.privatemines.utils;

import me.byteful.lib.blockedit.BlockEditAPI;
import me.untouchedodin0.privatemines.PrivateMines;
import me.untouchedodin0.privatemines.utils.mine.util.region.utils.WorldEditRegion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.codemc.worldguardwrapper.WorldGuardWrapper;
import org.codemc.worldguardwrapper.flag.WrappedState;
import org.codemc.worldguardwrapper.region.IWrappedRegion;
import org.jetbrains.annotations.NotNull;
import redempt.redlib.multiblock.MultiBlockStructure;
import redempt.redlib.region.Region;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Optional;
import java.util.stream.Stream;

import static org.bukkit.plugin.java.JavaPlugin.getPlugin;

public class Util {

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
        return MultiBlockStructure.create(inputStream, structureName, false, true);
    }

    public MultiBlockStructure loadStructure(String structureName) {
        File file = new File(getPlugin(PrivateMines.class).getDataFolder(), "structures/" + structureName);
        return loadStructure(structureName, file);
    }

    @NotNull
    public IWrappedRegion createMainWorldGuardRegion(Player owner, WorldEditRegion r) {
        IWrappedRegion region = WorldGuardWrapper.getInstance().addCuboidRegion(
                owner.getUniqueId().toString(),
                r.getMinimumLocation(),
                r.getMaximumLocation())
                .orElseThrow(() -> new RuntimeException("Could not create Main WorldGuard region"));

        region.getOwners().addPlayer(owner.getUniqueId());

        setMainFlags(region);
        return region;
    }

    public void setMainFlags(IWrappedRegion region) {
        final WorldGuardWrapper w = WorldGuardWrapper.getInstance();
        Stream.of(
                w.getFlag("mob-spawning", WrappedState.class)
        ).filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(flag -> region.setFlag(flag, WrappedState.DENY));
        Stream.of(
                w.getFlag("block-place", WrappedState.class),
                w.getFlag("block-break", WrappedState.class)
        ).filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(flag -> region.setFlag(flag, WrappedState.ALLOW));
    }

    public void setAirBlocks(Region region) {
        region.forEachBlock(block -> {
            BlockState state = block.getState();
            BlockEditAPI.setBlock(block, Material.AIR, state.getData(), false);
        });
    }
}
