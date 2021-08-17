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

package me.untouchedodin0.privatemines.utils.storage;

import me.untouchedodin0.privatemines.utils.mine.Mine;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MineStorage {

    File mineFolder = new File("plugins/PrivateMinesRewrite/mines");
    File[] mineFiles = new File("plugins/PrivateMinesRewrite/mines").listFiles();

    Map<UUID, Mine> mines = new HashMap<>();

    public void addMine(UUID uuid, Mine mine) {
        if (mines.containsKey(uuid)) {
            Bukkit.getLogger().warning("User already had a mine, if you believe this is an error please report it!");
        } else {
            mines.put(uuid, mine);
        }
    }

    public boolean hasMine(Player player) {
        return mines.containsKey(player.getUniqueId());
    }

    public File[] getMineFiles() {
        return mineFiles;
    }

    public File getMineFolder() {
        return mineFolder;
    }

    public Map<UUID, Mine> getMines() {
        return mines;
    }

    public Mine getMine(Player player) {
        return mines.get(player.getUniqueId());
    }
}

