package me.untouchedodin0.privatemines.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.untouchedodin0.privatemines.utils.filling.MineFillManager;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import redempt.redlib.misc.ChatPrompt;

import java.io.File;

public class MainMenuGui {

    Gui gui;
    File userFile;
    YamlConfiguration mineConfig;
    Location teleportLocation;
    Location corner1;
    Location corner2;
    MineFillManager mineFillManager;
    boolean isClosed = false;
    double size;

    public MainMenuGui(MineFillManager mineFillManager) {
        this.mineFillManager = mineFillManager;
    }

    public void setIsClosed(boolean toggle) {
        this.isClosed = toggle;
    }

    public void openMainMenuGui(Player player) {

        userFile = new File("plugins/PrivateMinesRewrite/mines/" + player.getUniqueId() + ".yml");
        mineConfig = YamlConfiguration.loadConfiguration(userFile);
        this.teleportLocation = mineConfig.getLocation("spawnLocation");
        this.corner1 = mineConfig.getLocation("corner1");
        this.corner2 = mineConfig.getLocation("corner2");
        size = corner2.distance(corner1);

        ItemStack bedStack = new ItemStack(Material.RED_BED);
        ItemMeta bedStackItemMeta = bedStack.getItemMeta();
        bedStackItemMeta.setDisplayName(ChatColor.GREEN + "Go to your mine");
        bedStack.setItemMeta(bedStackItemMeta);

        ItemStack statusClosed = new ItemStack(Material.RED_WOOL);
        ItemMeta statusClosedItemMeta = statusClosed.getItemMeta();
        statusClosedItemMeta.setDisplayName("" + ChatColor.GRAY + "Mine Status: " + ChatColor.RED + "Closed");
        statusClosed.setItemMeta(statusClosedItemMeta);

        ItemStack statusOpen = new ItemStack(Material.GREEN_WOOL);
        ItemMeta statusOpenItemMeta = statusOpen.getItemMeta();
        statusOpenItemMeta.setDisplayName("" + ChatColor.GRAY + "Mine Status: " + ChatColor.GREEN + "Open");
        statusOpen.setItemMeta(statusOpenItemMeta);

        ItemStack setTax = new ItemStack(Material.OAK_SIGN);
        ItemMeta setTaxItemMeta = setTax.getItemMeta();
        setTaxItemMeta.setDisplayName(ChatColor.GREEN + "Set Tax");
        setTax.setItemMeta(setTaxItemMeta);

        ItemStack mineSize = new ItemStack(Material.LAVA_BUCKET);
        ItemMeta mineSizeItemMeta = mineSize.getItemMeta();
        mineSizeItemMeta.setDisplayName(ChatColor.GREEN + "Mine Size " + ChatColor.YELLOW + size);
        mineSize.setItemMeta(mineSizeItemMeta);

        ItemStack resetMine = new ItemStack(Material.MINECART);
        ItemMeta resetMineItemMeta = resetMine.getItemMeta();
        resetMineItemMeta.setDisplayName(ChatColor.GREEN + "Reset Mine");
        resetMine.setItemMeta(resetMineItemMeta);

        ItemStack whitelistedMembers = new ItemStack(Material.WATER_BUCKET);
        ItemMeta whitelistedMembersItemMeta = whitelistedMembers.getItemMeta();
        whitelistedMembersItemMeta.setDisplayName(ChatColor.GREEN + "Whitelisted Members");
        whitelistedMembers.setItemMeta(whitelistedMembersItemMeta);

        ItemStack bannedMembers = new ItemStack(Material.LAVA_BUCKET);
        ItemMeta bannedMembersItemMeta = bannedMembers.getItemMeta();
        bannedMembersItemMeta.setDisplayName(ChatColor.GREEN + "Banned Members");
        bannedMembers.setItemMeta(bannedMembersItemMeta);

        ItemStack priorityMembers = new ItemStack(Material.EMERALD);
        ItemMeta priorityMembersItemMeta = priorityMembers.getItemMeta();
        priorityMembersItemMeta.setDisplayName(ChatColor.GREEN + "Priority Members");
        priorityMembers.setItemMeta(priorityMembersItemMeta);

        ItemStack coowner = new ItemStack(Material.DRAGON_EGG);
        ItemMeta coownerItemMeta = coowner.getItemMeta();
        coownerItemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Co-Owner");
        coowner.setItemMeta(coownerItemMeta);

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
            //TODO close mine logic...
        });

        GuiItem openCloseItem = ItemBuilder.from(statusClosed).asGuiItem(event -> {
            event.setCancelled(true);
            if (isClosed) {
                player.sendMessage(ChatColor.GREEN + "Opened your mine!");
                gui.updateItem(event.getSlot(), openItem);
                setIsClosed(false);
            } else {
                player.sendMessage(ChatColor.GREEN + "Closed your mine your mine!");
                gui.setItem(event.getSlot(), closeItem);
                gui.update();
                setIsClosed(true);
            }
        });

        GuiItem setTaxItem = ItemBuilder.from(setTax).asGuiItem(event -> {
            event.setCancelled(true);
            ChatPrompt.prompt(player, "Please enter a tax amount",
                    response -> {
                player.sendMessage("Your response was: " + response);
            });
        });

        GuiItem mineSizeItem = ItemBuilder.from(mineSize).asGuiItem(event -> {
            event.setCancelled(true);
            player.sendMessage(ChatColor.GREEN + "Your mine size is...");
        });

        GuiItem resetMineItem = ItemBuilder.from(resetMine).asGuiItem(event -> {
            event.setCancelled(true);
            mineFillManager.fillPlayerMine(player);
            player.sendMessage(ChatColor.GREEN + "Resetting your mine!");
            player.closeInventory();
        });

        GuiItem whitelistItem = ItemBuilder.from(whitelistedMembers).asGuiItem(event -> {
            event.setCancelled(true);
            player.sendMessage(ChatColor.GREEN + "Lets manage those whitelisted players!");
        });

        GuiItem bannedMembersItem = ItemBuilder.from(bannedMembers).asGuiItem(event -> {
            event.setCancelled(true);
            player.sendMessage(ChatColor.GREEN + "Lets manage those banned players >:(");
        });

        GuiItem priorityMembersItem = ItemBuilder.from(priorityMembers).asGuiItem(event -> {
            event.setCancelled(true);
            player.sendMessage(ChatColor.GREEN + "oooh fancy...");
        });

        GuiItem coOwnerItem = ItemBuilder.from(coowner).asGuiItem(event -> {
            event.setCancelled(true);
            player.sendMessage(ChatColor.GREEN + "someone's lucky to be the co-owner");
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
