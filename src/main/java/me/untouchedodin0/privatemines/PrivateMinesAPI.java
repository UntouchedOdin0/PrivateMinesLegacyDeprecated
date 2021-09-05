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

package me.untouchedodin0.privatemines;

import me.untouchedodin0.privatemines.factory.MineFactory;
import me.untouchedodin0.privatemines.utils.mine.Mine;
import me.untouchedodin0.privatemines.utils.storage.MineStorage;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class PrivateMinesAPI {

    PrivateMines privateMines;
    MineStorage mineStorage;
    MineFactory mineFactory;
    Mine mine;

    public void givePrivateMine(Player player) {
        privateMines = PrivateMines.getPlugin(PrivateMines.class);
        mineFactory = privateMines.getMineFactory();
        mineStorage = privateMines.getMineStorage();
        if (mineStorage.hasMine(player)) return;
        mineFactory.createMine(player);
    }

    public void deletePrivateMine(Player player) {
        privateMines = PrivateMines.getPlugin(PrivateMines.class);
        mineFactory = privateMines.getMineFactory();
        mineStorage = privateMines.getMineStorage();
        if (!mineStorage.hasMine(player)) return;
        mine = mineStorage.getMine(player);
        mine.deleteMineStructure(player);
        mineStorage.deleteMine(player.getUniqueId());
    }

    public void resetMine(Player player) {
        privateMines = PrivateMines.getPlugin(PrivateMines.class);
        mineFactory = privateMines.getMineFactory();
        mineStorage = privateMines.getMineStorage();
        if (!mineStorage.hasMine(player)) return;
        mine = mineStorage.getMine(player);
        mine.resetMine();
    }
}
