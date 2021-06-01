package me.untouchedodin0.privatemines.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.untouchedodin0.privatemines.worldedit.factory.MineFactory;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;

@SuppressWarnings("unused")
@CommandAlias("privatemines|privatemine|pm|pmine")
public class PrivateMinesCommand extends BaseCommand {

    private final MineFactory mineFactory = new MineFactory();

    @Default
    @Description("Manage privatemines")
    public void main(Player p) {
        if (p.hasPermission("privatemines.owner")) {
            p.sendMessage("opening gui.");
        }
    }

    @Subcommand("give")
    @Description("Gives a privatemines to a player (only pastes at the moment)")
    @CommandPermission("privatemines.give")
    public void give(Player p) {
        File file = new File("plugins/PrivateMinesRewrite/schematics/test.schem");
        Location location = p.getLocation();
        if (!file.exists()) {
            p.sendMessage("The file doesn't exist!");
        }
        mineFactory.createPrivateMine(p, file, location);
        p.sendMessage("Created your privatemine");
    }
}

