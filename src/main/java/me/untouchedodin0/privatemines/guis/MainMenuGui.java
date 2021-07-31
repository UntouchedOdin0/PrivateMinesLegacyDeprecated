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

package me.untouchedodin0.privatemines.guis;

import com.cryptomorin.xseries.XMaterial;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.untouchedodin0.privatemines.utils.filling.MineFillManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import redempt.redlib.misc.ChatPrompt;

import java.io.File;
import java.io.IOException;

public class MainMenuGui {

    Gui gui;
    File userFile;
    YamlConfiguration mineConfig;
    Location teleportLocation;
    Location corner1;
    Location corner2;
    Location corner1flat;
    Location corner2flat;
    MineFillManager mineFillManager;
    boolean isClosed = false;
    int size;
    int tax;
    WhitelistedPlayersGui whitelistedPlayersGui;
    BannedPlayersGui bannedPlayersGui;
    PriorityPlayersGui priorityPlayersGui;
    Player coowner;

    ItemStack bedStack;
    ItemMeta bedStackItemMeta;

    ItemStack statusClosed;
    ItemMeta statusClosedMeta;

    ItemStack statusOpen;
    ItemMeta statusOpenMeta;

    ItemStack setTax;
    ItemMeta setTaxMeta;

    ItemStack mineSize;
    ItemMeta mineSizeMeta;

    ItemStack resetMine;
    ItemMeta resetMineMeta;

    ItemStack whitelistedMembers;
    ItemMeta whitelistedMembersMeta;

    ItemStack bannedMembers;
    ItemMeta bannedMembersMeta;

    ItemStack priorityMembers;
    ItemMeta priorityMembersMeta;

    ItemStack coownerstack;
    ItemMeta coownerMeta;

    public MainMenuGui(MineFillManager mineFillManager) {
        this.mineFillManager = mineFillManager;
    }

    public void setIsClosed(boolean toggle) {
        this.isClosed = toggle;
    }

    public void openMainMenuGui(Player player) {

        userFile = new File("plugins/PrivateMinesRewrite/mines/" + player.getUniqueId() + ".yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
        this.teleportLocation = mineConfig.getSerializable("spawnLocation", Location.class);
//        start = mineConfig.getSerializable(CORNER_2_STRING, Location.class);

//        this.teleportLocation = mineConfig.getLocation("spawnLocation");
        corner1 = mineConfig.getSerializable("Corner1", Location.class);
        corner2 = mineConfig.getSerializable("Corner2", Location.class);

//        corner1 = mineConfig.getLocation("Corner1");
//        corner2 = mineConfig.getLocation("Corner2");
//        corner1flat = new Location(corner1.getWorld(), corner1.getX(), corner1.getY(), corner1.getZ());
//        corner2flat = new Location(corner2.getWorld(), corner2.getX(), corner1.getY(), corner2.getZ());

        size = mineConfig.getInt("mineSize");
        whitelistedPlayersGui = new WhitelistedPlayersGui();
        bannedPlayersGui = new BannedPlayersGui();
        priorityPlayersGui = new PriorityPlayersGui();

        if (XMaterial.RED_BED.parseMaterial() != null) {
            bedStack = new ItemStack(XMaterial.RED_BED.parseMaterial());
            bedStackItemMeta = bedStack.getItemMeta();
            if (bedStackItemMeta != null) {
                bedStackItemMeta.setDisplayName(ChatColor.GREEN + "Go to your mine");
            }
            bedStack.setItemMeta(bedStackItemMeta);
        }

        if (XMaterial.RED_WOOL.parseMaterial() != null) {
            statusClosed = new ItemStack(XMaterial.RED_WOOL.parseMaterial());
            statusClosedMeta = statusClosed.getItemMeta();
            if (statusClosedMeta != null) {
                statusClosedMeta.setDisplayName("" + ChatColor.GRAY + "Mine Status: " + ChatColor.RED + "Closed");
            }
            statusClosed.setItemMeta(statusClosedMeta);
        }

        if (XMaterial.LIME_WOOL.parseMaterial() != null) {
            statusOpen = new ItemStack(XMaterial.LIME_WOOL.parseMaterial());
            statusOpenMeta = statusOpen.getItemMeta();
            if (statusOpenMeta != null) {
                statusOpenMeta.setDisplayName("" + ChatColor.GRAY + "Mine Status: " + ChatColor.GREEN + "Open");
            }
            statusOpen.setItemMeta(statusOpenMeta);
        }

        if (XMaterial.OAK_SIGN.parseMaterial() != null) {
            setTax = new ItemStack(XMaterial.OAK_SIGN.parseMaterial());
            setTaxMeta = setTax.getItemMeta();
            if (setTaxMeta != null) {
                setTaxMeta.setDisplayName(ChatColor.GREEN + "Set Tax");
            }
            setTax.setItemMeta(setTaxMeta);
        }

        if (XMaterial.LAVA_BUCKET.parseMaterial() != null) {
            mineSize = new ItemStack(XMaterial.LAVA_BUCKET.parseMaterial());
            mineSizeMeta = mineSize.getItemMeta();
            if (mineSizeMeta != null) {
                mineSizeMeta.setDisplayName(ChatColor.GREEN + "Mine Size " + ChatColor.YELLOW + size + "x" + size);
            }
            mineSize.setItemMeta(mineSizeMeta);
        }

        if (XMaterial.MINECART.parseMaterial() != null) {
            resetMine = new ItemStack(XMaterial.MINECART.parseMaterial());
            resetMineMeta = resetMine.getItemMeta();
            if (resetMineMeta != null) {
                resetMineMeta.setDisplayName(ChatColor.GREEN + "Reset Mine");
            }
            resetMine.setItemMeta(resetMineMeta);
        }

        if (XMaterial.WATER_BUCKET.parseMaterial() != null) {
            whitelistedMembers = new ItemStack(XMaterial.WATER_BUCKET.parseMaterial());
            whitelistedMembersMeta = whitelistedMembers.getItemMeta();
            if (whitelistedMembersMeta != null) {
                whitelistedMembersMeta.setDisplayName(ChatColor.GREEN + "Whitelisted Members");
            }
            whitelistedMembers.setItemMeta(whitelistedMembersMeta);
        }

        if (XMaterial.LAVA_BUCKET.parseMaterial() != null) {
            bannedMembers = new ItemStack(XMaterial.LAVA_BUCKET.parseMaterial());
            bannedMembersMeta = bannedMembers.getItemMeta();
            if (bannedMembersMeta != null) {
                bannedMembersMeta.setDisplayName(ChatColor.GREEN + "Banned Members");
            }
            bannedMembers.setItemMeta(bedStackItemMeta);
        }

        if (XMaterial.EMERALD.parseMaterial() != null) {
            priorityMembers = new ItemStack(XMaterial.EMERALD.parseMaterial());
            priorityMembersMeta = priorityMembers.getItemMeta();
            if (priorityMembersMeta != null) {
                priorityMembersMeta.setDisplayName(ChatColor.GREEN + "Priority Members");
            }
            priorityMembers.setItemMeta(priorityMembersMeta);
        }

        if (XMaterial.DRAGON_EGG.parseMaterial() != null) {
            coownerstack = new ItemStack(XMaterial.DRAGON_EGG.parseMaterial());
            coownerMeta = coownerstack.getItemMeta();
            if (coownerMeta != null) {
                coownerMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Co-Owner");
            }
            coownerstack.setItemMeta(coownerMeta);
        }

        /*
        ItemStack statusClosed = new ItemStack(Material.RED_WOOL);
        ItemMeta statusClosedItemMeta = statusClosed.getItemMeta();
        if (statusClosedItemMeta != null) {
            statusClosedItemMeta.setDisplayName("" + ChatColor.GRAY + "Mine Status: " + ChatColor.RED + "Closed");
        }
        statusClosed.setItemMeta(statusClosedItemMeta);

        ItemStack statusOpen = new ItemStack(Material.GREEN_WOOL);
        ItemMeta statusOpenItemMeta = statusOpen.getItemMeta();
        if (statusOpenItemMeta != null) {
            statusOpenItemMeta.setDisplayName("" + ChatColor.GRAY + "Mine Status: " + ChatColor.GREEN + "Open");
        }
        statusOpen.setItemMeta(statusOpenItemMeta);

        ItemStack setTax = null;
        if (XMaterial.OAK_SIGN.parseMaterial() != null) {
            setTax = new ItemStack(XMaterial.OAK_SIGN.parseMaterial());
        }
        ItemMeta setTaxItemMeta = setTax.getItemMeta();
        if (setTaxItemMeta != null) {
            setTaxItemMeta.setDisplayName(ChatColor.GREEN + "Set Tax");
        }
        setTax.setItemMeta(setTaxItemMeta);

        ItemStack mineSize = new ItemStack(Material.LAVA_BUCKET);
        ItemMeta mineSizeItemMeta = mineSize.getItemMeta();
        if (mineSizeItemMeta != null) {
            mineSizeItemMeta.setDisplayName(ChatColor.GREEN + "Mine Size " + ChatColor.YELLOW + size + "x" + size);
        }
        mineSize.setItemMeta(mineSizeItemMeta);

        ItemStack resetMine = new ItemStack(Material.MINECART);
        ItemMeta resetMineItemMeta = resetMine.getItemMeta();
        if (resetMineItemMeta != null) {
            resetMineItemMeta.setDisplayName(ChatColor.GREEN + "Reset Mine");
        }
        resetMine.setItemMeta(resetMineItemMeta);

        ItemStack whitelistedMembers = new ItemStack(Material.WATER_BUCKET);
        ItemMeta whitelistedMembersItemMeta = whitelistedMembers.getItemMeta();
        if (whitelistedMembersItemMeta != null) {
            whitelistedMembersItemMeta.setDisplayName(ChatColor.GREEN + "Whitelisted Members");
        }
        whitelistedMembers.setItemMeta(whitelistedMembersItemMeta);

        ItemStack bannedMembers = new ItemStack(Material.LAVA_BUCKET);
        ItemMeta bannedMembersItemMeta = bannedMembers.getItemMeta();
        if (bannedMembersItemMeta != null) {
            bannedMembersItemMeta.setDisplayName(ChatColor.GREEN + "Banned Members");
        }

        bannedMembers.setItemMeta(bannedMembersItemMeta);

        ItemStack priorityMembers = new ItemStack(Material.EMERALD);
        ItemMeta priorityMembersItemMeta = priorityMembers.getItemMeta();
        if (priorityMembersItemMeta != null) {
            priorityMembersItemMeta.setDisplayName(ChatColor.GREEN + "Priority Members");
        }

        priorityMembers.setItemMeta(priorityMembersItemMeta);

        ItemStack coowner = new ItemStack(Material.DRAGON_EGG);
        ItemMeta coownerItemMeta = coowner.getItemMeta();
        if (coownerItemMeta != null) {
            coownerItemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Co-Owner");
        }
        coowner.setItemMeta(coownerItemMeta);
         */

        gui = Gui.gui().title(Component.text("Private Mine")).rows(1).create();

        GuiItem teleportToMine = ItemBuilder.from(bedStack).asGuiItem(event -> {
            event.setCancelled(true);
            player.sendMessage(ChatColor.GREEN + "Teleporting to your mine!");
            player.teleport(teleportLocation);
        });

        GuiItem openItem = ItemBuilder.from(statusOpen).asGuiItem(event -> {
            event.setCancelled(true);
            setIsClosed(false);
        });

        GuiItem closeItem = ItemBuilder.from(statusClosed).asGuiItem(event -> {
            event.setCancelled(true);
            setIsClosed(true);
        });

        GuiItem openCloseItem = ItemBuilder.from(statusClosed).asGuiItem(event -> {
            event.setCancelled(true);
            if (isClosed) {
                player.sendMessage(ChatColor.GREEN + "Opened your mine!");
                setIsClosed(false);
                gui.removeItem(event.getSlot());
                gui.setItem(event.getSlot(), openItem);
            } else {
                player.sendMessage(ChatColor.GREEN + "Closed your mine your mine!");
                gui.removeItem(event.getSlot());
                gui.setItem(event.getSlot(), closeItem);
//                gui.updateItem(event.getSlot(), closeItem);
                setIsClosed(true);
            }
        });

        GuiItem setTaxItem = ItemBuilder.from(setTax).asGuiItem(event -> {
            event.setCancelled(true);
            player.closeInventory();
            ChatPrompt.prompt(player, "Please enter a tax amount",
                    response -> {
                        tax = Integer.parseInt(response);
                        if (tax < 0) {
                            // less than 0...
                            player.sendMessage(ChatColor.RED + "It's impossible to set your tax below 0%!");
                        } else if (tax > 100) {
                            // more than 100...
                            player.sendMessage(ChatColor.RED + "If you taxed yourself more than 100% you'd be taxing yourself!");
                        } else {
                            player.sendMessage(ChatColor.GREEN + "Setting your mine tax to " + response + "%");
                        }
                    });
        });

        GuiItem mineSizeItem = ItemBuilder.from(mineSize).asGuiItem(event -> {
            event.setCancelled(true);
            player.sendMessage(ChatColor.GREEN + "Your mine size is " + size + "x" + size);
        });

        GuiItem resetMineItem = ItemBuilder.from(resetMine).asGuiItem(event -> {
            player.closeInventory();
            event.setCancelled(true);
            mineFillManager.fillPlayerMine(player);
            player.sendMessage(ChatColor.GREEN + "Resetting your mine!");
        });

        GuiItem whitelistItem = ItemBuilder.from(whitelistedMembers).asGuiItem(event -> {
            player.closeInventory();
            event.setCancelled(true);
            whitelistedPlayersGui.openWhitelistedPlayersMenu(player);
        });

        GuiItem bannedMembersItem = ItemBuilder.from(bannedMembers).asGuiItem(event -> {
            player.closeInventory();
            event.setCancelled(true);
            player.closeInventory();
            bannedPlayersGui.openBannedPlayersMenu(player);
        });

        GuiItem priorityMembersItem = ItemBuilder.from(priorityMembers).asGuiItem(event -> {
            player.closeInventory();
            event.setCancelled(true);
            player.closeInventory();
            priorityPlayersGui.openPriorityPlayersMenu(player);
        });

        GuiItem coOwnerItem = ItemBuilder.from(coownerstack).asGuiItem(event -> {
            player.closeInventory();
            event.setCancelled(true);
            ChatPrompt.prompt(player, ChatColor.RED + "Please specify a player name",
                    response -> {
                        Player target = Bukkit.getPlayer(response);
                        if (target != null) {
                            target.sendMessage(ChatColor.GREEN + "You've been set as a co-owner in "
                                    + player.getName()
                                    + "'s private mine!");
                            mineConfig.set("coowner", player.getName());
                            try {
                                mineConfig.save(userFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            player.sendMessage(ChatColor.GREEN + player.getName() + " has set you as co-owner of your mine!");
                        }
                    });
        });

        gui.setItem(0, teleportToMine);
        gui.setItem(1, openCloseItem);
        gui.setItem(2, setTaxItem);
        gui.setItem(3, mineSizeItem);
        gui.setItem(4, resetMineItem);
        gui.setItem(5, whitelistItem);
        gui.setItem(6, bannedMembersItem);
        gui.setItem(7, priorityMembersItem);
        gui.setItem(8, coOwnerItem);

        gui.open(player);
    }

    public void teleportToMine(Player player) {
        player.teleport(teleportLocation);
    }
}
