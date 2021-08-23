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

package me.untouchedodin0.privatemines.utils.mine.util;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public class PrivateMineUtil extends Event implements Cancellable {

    private final Player player;
    private final File mineFile;
    private boolean cancelled;
    private final List<ItemStack> mineBlocks;
    private final List<UUID> whitelistedPlayers;
    private final List<UUID> bannedPlayers;
    private final List<UUID> priorityPlayers;
    private final UUID coowner;

    public static final HandlerList handlers = new HandlerList();

    public Player getPlayer() {
        return player;
    }

    public File getMineFile() {
        return mineFile;
    }

    public void setCancelled(boolean paramCancelled) {
        this.cancelled = paramCancelled;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public List<ItemStack> getMineBlocks() {
        return mineBlocks;
    }

    public List<UUID> getWhitelistedPlayers() {
        return whitelistedPlayers;
    }

    public List<UUID> getBannedPlayers() {
        return bannedPlayers;
    }

    public List<UUID> getPriorityPlayers() {
        return priorityPlayers;
    }

    public UUID getCoOwner() {
        return coowner;
    }

    public PrivateMineUtil(Player player,
                       File file,
                       List<ItemStack> mineBlocks,
                       List<UUID> whitelistedPlayers,
                       List<UUID> bannedPlayers,
                       List<UUID> priorityPlayers,
                       UUID coowner) {
        this.player = player;
        this.mineFile = file;
        this.mineBlocks = mineBlocks;
        this.whitelistedPlayers = whitelistedPlayers;
        this.bannedPlayers = bannedPlayers;
        this.priorityPlayers = priorityPlayers;
        this.coowner = coowner;
    }

    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
